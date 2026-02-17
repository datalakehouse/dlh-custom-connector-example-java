package io.datalakehouse.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.FreshDeskConstants;
import io.datalakehouse.common.GustoConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.utils.ConnectorHelper;
import io.datalakehouse.utils.CsvDataBuffer;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FreshDeskConnector extends DLHIngest {

    ObjectMapper mapper = new ObjectMapper();
    // Thread-safe accumulator for CSV data across multiple parent IDs
    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferConcurrentHashMap = new ConcurrentHashMap<>();

    public FreshDeskConnector(ConnectionType connectionType, DLHIngestConfig config, String outputPath) {
        super(connectionType, config, outputPath);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        try {
            return processData(entity, stream, headers, null, null);
        } catch (IOException e) {
            downloadHelper.logWarning(entity, e.getCause().getLocalizedMessage(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());
            throw e;
        }
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers,
                                       String parentPlaceholderKey, String parentId) throws IOException {

        downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());
        Instant startTime = Instant.now();

        // Response doesn't return any data
        if (stream == null) {
            return List.of();
        }

        boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);
        CsvDataBuffer csvDataBuffer = isMultiParentProcessing
                ? csvDataBufferConcurrentHashMap.computeIfAbsent(entity,
                k -> new CsvDataBuffer(headers.toArray(new String[0]),
                        config.getCsvRowLimit(), k, config.getConnectorType()))
                : null;

        int lineCount = 0;
        List<String[]> csvChunk = isMultiParentProcessing ? null : initHeaderChunk(headers);
        List<String> entityIds = new ArrayList<>();

        String idValue;
        JsonNode rootNode = mapper.readTree(stream);

        // Check if root is an array or has nested data
        JsonNode dataNode = normalizeRootNode(rootNode);

        // Process array data
        System.out.println("Processing " + dataNode.size() + " records from array");

        // If array is empty, write CSV with just headers and return
        if (dataNode.isEmpty()) {
            if (!isMultiParentProcessing) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                downloadHelper.addBridgeStats(Map.of(entity, 0), startTime,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());
                downloadHelper.logEndHistory(entity,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name(), 0);
            }
            return entityIds;
        }

        for (JsonNode recordNode : dataNode) {

            // Skip empty arrays - common in responses like [[], [], {...}]
            if (recordNode.isArray() && recordNode.isEmpty()) {
                continue;
            }

            // Check if the record itself is an array containing a single object
            JsonNode actualRecord = recordNode;
            if (recordNode.isArray() && !recordNode.isEmpty()) {
                // If it's an array with a single element, unwrap it
                if (recordNode.size() == 1) {
                    actualRecord = recordNode.get(0);
                } else {
                    // If it's an array with multiple elements, process each one
                    for (JsonNode innerNode : recordNode) {
                        // Skip empty arrays within nested arrays
                        if (innerNode.isArray() && innerNode.isEmpty()) {
                            continue;
                        }

                        List<String> rowValues = ConnectorHelper.mapValues(innerNode, headers);

                        // Inject parent ID
                        if (parentPlaceholderKey != null && parentId != null) {
                            int idx = headers.indexOf(parentPlaceholderKey.toUpperCase());
                            if (idx >= 0 && idx < rowValues.size()) {
                                rowValues.set(idx, parentId);
                            }
                        }


                        idValue = ConnectorHelper.getValueForHeader(headers, rowValues, "ID");

                        if (null != idValue) {
                            entityIds.add(idValue);
                        }

                        if (isMultiParentProcessing) {
                            csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
                        } else {
                            csvChunk.add(rowValues.toArray(new String[0]));
                            lineCount++;

                            // When we hit chunk size, flush to CSV
                            if (lineCount % config.getCsvRowLimit() == 0) {
                                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                                csvChunk.clear();
                            }
                        }
                    }
                    continue; // Skip the normal processing below
                }
            }

            List<String> rowValues = ConnectorHelper.mapValues(actualRecord, headers);
            // Inject parent ID
            if (parentPlaceholderKey != null && parentId != null) {
                int idx = headers.indexOf(parentPlaceholderKey.toUpperCase());
                if (idx >= 0 && idx < rowValues.size()) {
                    rowValues.set(idx, parentId);
                }
            }

            idValue = ConnectorHelper.getValueForHeader(headers, rowValues,
                    FreshDeskConstants.ID_HEADERS);

            if (null != idValue) {
                entityIds.add(idValue);
            }

            if (isMultiParentProcessing) {
                csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
            } else {
                csvChunk.add(rowValues.toArray(new String[0]));
                lineCount++;

                // When we hit chunk size, flush to CSV
                if (lineCount % config.getCsvRowLimit() == 0) {
                    downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                    csvChunk.clear();
                }
            }
        }

        processMappingTables(entity, rootNode);

        // Flush any remaining rows for single-parent processing
        if (Objects.nonNull(csvChunk) && !csvChunk.isEmpty()) {
            downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
            downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());
            csvChunk.clear();
        }

        if (!isMultiParentProcessing) {
            downloadHelper.logEndHistory(entity,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name(), lineCount);
        }

        if (entity.equals(FreshDeskConstants.FreshDeskEntityNames.SURVEYS)) {
            String mappingEntity = FreshDeskConstants.MAPPING_TABLES_MAP.get(entity);
            csvDataBufferConcurrentHashMap.get(mappingEntity).flushRemaining(downloadHelper);
            downloadHelper.addBridgeStats(Map.of(mappingEntity, csvDataBufferConcurrentHashMap.get(mappingEntity).getTotalRecordsCount()),
                    csvDataBufferConcurrentHashMap.get(mappingEntity).getEntityProcessingStartTime(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());
        }

        return entityIds;
    }

    private JsonNode normalizeRootNode(JsonNode rootNode) {
        if (rootNode.isArray()) {
            return (rootNode.size() == 1 && rootNode.get(0).isArray())
                    ? rootNode.get(0)
                    : rootNode;
        }
        return mapper.createArrayNode().add(rootNode);
    }


    private List<String[]> initHeaderChunk(List<String> headers) {
        List<String[]> chunk = new ArrayList<>();
        chunk.add(headers.toArray(new String[0]));
        return chunk;
    }

    private void processMappingTables(String entity, JsonNode rootNode) throws IOException {
        String mappingEntity = FreshDeskConstants.MAPPING_TABLES_MAP.get(entity);
        if (mappingEntity == null || mappingEntity.isEmpty()) {
            return;
        }
        JsonNode dataNode = normalizeRootNode(rootNode);
        for (JsonNode recordNode : dataNode) {
            if (recordNode.isArray()) {
                for (JsonNode inner : recordNode) {
                    extractMappingData(inner, mappingEntity, entity);
                }
            } else {
                extractMappingData(recordNode, mappingEntity, entity);
            }
        }
    }

    private void extractMappingData(JsonNode recordNode, String mappingEntity, String entity) throws IOException {

        String parentId = extractIdFromNode(recordNode);
        String[] headers = FreshDeskConstants.CHILD_TABLE_HEADERS_BY_ENTITY.get(mappingEntity);
        if (headers == null) {
            return;
        }

        // Create or get existing buffer, and log start history if it's newly created
        boolean isNewBuffer = !csvDataBufferConcurrentHashMap.containsKey(mappingEntity);
        CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(mappingEntity,
                k -> new CsvDataBuffer(headers, config.getCsvRowLimit(), mappingEntity, config.getConnectorType()));

        if (isNewBuffer) {
            downloadHelper.logStartHistory(mappingEntity, CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());
        }

        // Extract the field name from the mapping entity (e.g., "SURVEYS_QUESTIONS" -> "questions")
        String fieldName = StringUtils.substringAfterLast(mappingEntity, "_").toLowerCase();
        JsonNode nestedArrayNode = recordNode.get(fieldName);

        // Check if the nested field exists and is an array
        if (nestedArrayNode == null || !nestedArrayNode.isArray()) {
            return;
        }

        // Process the nested array
        List<String> headersList = List.of(headers);
        for (JsonNode jsonNode : nestedArrayNode) {
            // Skip empty nodes
            if (jsonNode.isNull() || (jsonNode.isArray() && jsonNode.isEmpty())) {
                continue;
            }

            List<String> rowValues = ConnectorHelper.mapValues(jsonNode, headersList);

            // Inject parent ID
            if (parentId != null) {
                int idx = headersList.indexOf(entity.toUpperCase() + "_ID");
                if (idx >= 0 && idx < rowValues.size()) {
                    rowValues.set(idx, parentId);
                }
            }
            csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
        }

    }

    private String extractIdFromNode(JsonNode node) {
        for (String idHeader : FreshDeskConstants.ID_HEADERS) {
            if (node.has(idHeader)) {
                return node.get(idHeader).asText();
            }
            String lowerCase = idHeader.toLowerCase();
            if (node.has(lowerCase)) {
                return node.get(lowerCase).asText();
            }
        }
        return null;
    }

    /**
         * Flushes accumulated data for an entity to CSV.
         * Should be called after all parents have been processed.
         */
    @Override
    protected void flushEntityData(String entity) throws IOException {
        CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.remove(entity);
        if (csvDataBuffer != null) {
            csvDataBuffer.flushRemaining(downloadHelper);
            downloadHelper.addBridgeStats(Map.of(entity, csvDataBuffer.getTotalRecordsCount()), csvDataBuffer.getEntityProcessingStartTime(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());
            if (FreshDeskConstants.MAPPING_TABLES_MAP.containsKey(entity)) {
                String childMappingEntity = FreshDeskConstants.MAPPING_TABLES_MAP.get(entity);
                CsvDataBuffer childEntityBuffer = csvDataBufferConcurrentHashMap.remove(childMappingEntity);
                if (childEntityBuffer != null) {
                    childEntityBuffer.flushRemaining(downloadHelper);
                    downloadHelper.addBridgeStats(Map.of(childMappingEntity, childEntityBuffer.getTotalRecordsCount()),
                            childEntityBuffer.getEntityProcessingStartTime(),
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());
                }
            }
        }
    }
}
