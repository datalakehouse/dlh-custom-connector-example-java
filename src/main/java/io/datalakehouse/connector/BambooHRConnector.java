package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.datalakehouse.common.BambooHRConstants;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.utils.ConnectorHelper;
import io.datalakehouse.utils.CsvDataBuffer;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BambooHRConnector extends DLHIngest {

    ObjectMapper mapper = new ObjectMapper();
    // Thread-safe accumulator for CSV data across multiple parent IDs
    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferConcurrentHashMap = new ConcurrentHashMap<>();

    public BambooHRConnector(ConnectionType connectionType, DLHIngestConfig config, String outputPath) {
        super(connectionType, config, outputPath);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        return processData(entity, stream, headers, null, null);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers,
                                       String parentPlaceholderKey, String parentId) throws IOException {

        // Response doesn't return any data
        if (stream == null) {
            return List.of();
        }

        Instant startTime = Instant.now();
        boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);

        // For multi-parent processing, log start history only once when buffer is created
        boolean isNewBuffer = isMultiParentProcessing && !csvDataBufferConcurrentHashMap.containsKey(entity);

        CsvDataBuffer csvDataBuffer = isMultiParentProcessing
                ? csvDataBufferConcurrentHashMap.computeIfAbsent(entity,
                k -> new CsvDataBuffer(headers.toArray(new String[0]),
                        config.getCsvRowLimit(), k, config.getConnectorType()))
                : null;

        if (isNewBuffer) {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
        }

        // For single entity processing, log start history normally
        if (!isMultiParentProcessing) {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
        }

        int lineCount = 0;
        List<String[]> csvChunk = isMultiParentProcessing ? null : initHeaderChunk(headers);
        List<String> entityIds = new ArrayList<>();

        JsonFactory factory = mapper.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = mapper.readTree(parser);
            JsonNode dataNode = resolveDataArray(entity, rootNode);

            processMappingTables(entity, dataNode);

            // If array is empty, write CSV with just headers and return
            if (dataNode.isEmpty()) {
                if (!isMultiParentProcessing) {
                    downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                    downloadHelper.addBridgeStats(Map.of(entity, 0), startTime,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
                    downloadHelper.logEndHistory(entity,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name(), 0);
                }
                return entityIds;
            }

            for (JsonNode recordNode : dataNode) {
                Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                for (JsonNode node : nodes) {
                    List<String> rowValues = ConnectorHelper.mapValues(node, headers);

                    // Inject parent ID
                    if (parentPlaceholderKey != null && parentId != null) {
                        int idx = headers.indexOf(parentPlaceholderKey.toUpperCase());
                        if (idx >= 0 && idx < rowValues.size()) {
                            rowValues.set(idx, parentId);
                        }
                    }

                    rowValues = ConnectorHelper.setDlhHeaderValues(headers, rowValues);


                    String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, BambooHRConstants.ID_HEADERS);
                    if (idValue != null) {
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
            }

            // Flush any remaining rows for single-parent processing
            if (Objects.nonNull(csvChunk) && !csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                csvChunk.clear();
            }

            String mappingEntity = BambooHRConstants.MAPPING_TABLES_MAP.get(entity);
            if (mappingEntity != null) {
                CsvDataBuffer childEntityBuffer = csvDataBufferConcurrentHashMap.remove(
                        mappingEntity);
                if (childEntityBuffer != null) {
                    childEntityBuffer.flushRemaining(downloadHelper);
                    downloadHelper.addBridgeStats(Map.of(mappingEntity, childEntityBuffer.getTotalRecordsCount()),
                            childEntityBuffer.getEntityProcessingStartTime(),
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
                }
            }

            if (!isMultiParentProcessing) {
                downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
                downloadHelper.logEndHistory(entity,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name(), lineCount);
            }
        }

        return entityIds;
    }

    /**
     * Override to support incremental page-by-page processing for BambooHR pagination.
     */
    @Override
    protected List<String> processDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, List<String> headers, String parentPlaceholderKey,
            String parentId) throws Exception {

        downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
        Instant entityProcessingStartTime = Instant.now();

        List<String> allEntityIds = new ArrayList<>();
        List<String[]> csvChunk = new ArrayList<>();
        int[] totalLineCount = {0};
        boolean[] headerWritten = {false};

        // Process pages incrementally using callback
        connectionType.fetchDataPaginated(entity, apiPath, queryParams, customHeaders, entityIds, paginationInfo,
            (pageStream, state) -> {
                try {
                    JsonFactory factory = mapper.getFactory();

                    // Response doesn't return any data
                    if (pageStream == null) {
                        return true;
                    }

                    try (JsonParser parser = factory.createParser(pageStream)) {
                        JsonNode rootNode = mapper.readTree(parser);
                        JsonNode dataNode = resolveDataArray(entity, rootNode);

                        // Add header only once on first page
                        if (!headerWritten[0]) {
                            csvChunk.add(headers.toArray(new String[0]));
                            headerWritten[0] = true;
                        }

                        processMappingTables(entity, dataNode);

                        // Process each record from this page
                        for (JsonNode recordNode : dataNode) {
                            Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                            for (JsonNode node : nodes) {
                                List<String> rowValues = ConnectorHelper.mapValues(node, headers);
                                String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, BambooHRConstants.ID_HEADERS);

                                if (idValue != null) {
                                    allEntityIds.add(idValue);
                                }

                                csvChunk.add(rowValues.toArray(new String[0]));
                                totalLineCount[0]++;

                                // Flush to CSV when chunk size is reached
                                if (totalLineCount[0] % config.getCsvRowLimit() == 0) {
                                    downloadHelper.writeChunkToCsv(entity, csvChunk,
                                        totalLineCount[0], config.getConnectorType());
                                    csvChunk.clear();
                                }
                            }
                        }
                        return true;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error processing page " + state.page() + " for entity " + entity, e);
                }
            }
        );

        // Flush any remaining rows
        if (!csvChunk.isEmpty()) {
            downloadHelper.writeChunkToCsv(entity, csvChunk, totalLineCount[0], config.getConnectorType());
        }

        // Handle case where no data was fetched at all
        if (!headerWritten[0]) {
            csvChunk.add(headers.toArray(new String[0]));
            downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
        }

        downloadHelper.addBridgeStats(Map.of(entity, totalLineCount[0]), entityProcessingStartTime,
                CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
        downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name(), totalLineCount[0]);

        return allEntityIds;
    }

    private void processMappingTables(String entity, JsonNode rootNode) throws IOException {
        String mappingEntity = BambooHRConstants.MAPPING_TABLES_MAP.get(entity);
        if (mappingEntity == null || mappingEntity.isEmpty()) {
            return;
        }
        for (JsonNode recordNode : rootNode) {
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

        String[] headers = BambooHRConstants.CHILD_TABLE_HEADERS_BY_ENTITY.get(mappingEntity);
        if (headers == null) {
            return;
        }

        String parentId = extractIdFromNode(recordNode);

        // Create or get existing buffer, and log start history if it's newly created
        boolean isNewBuffer = !csvDataBufferConcurrentHashMap.containsKey(mappingEntity);
        CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(mappingEntity,
                k -> new CsvDataBuffer(headers, config.getCsvRowLimit(), mappingEntity, config.getConnectorType()));

        if (isNewBuffer) {
            downloadHelper.logStartHistory(mappingEntity, CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
        }

        String fieldName = mappingEntity.equals(BambooHRConstants.BambooHREntityNames.COMPANY_FILES)
                ? "files" : "milestones";
        JsonNode nestedArrayNode = recordNode.get(fieldName);

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

            rowValues = ConnectorHelper.setDlhHeaderValues(headersList, rowValues);
            csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
        }

    }

    private JsonNode resolveDataArray(String entity, JsonNode rootNode) {

        String[] keys = {"Benefit Coverages", "companyBenefits", "categories", "options", "Employee Dependents",
                "applications", "data", "items", "results", "milestones", StringUtils.lowerCase(entity)};

        if (rootNode.isArray()) {

            // Pattern: { "123": {...}, "456": {...} } where each value has an "id" field matching the key
            if (isDynamicKeyBasedObject(rootNode)) {
                return toArray(rootNode);
            }

            // Case: [ { options: [...] } ]
            if (!rootNode.isEmpty() && rootNode.get(0).isObject()) {
                JsonNode first = rootNode.get(0);

                // Check if the first element is a dynamic key-based object
                if (isDynamicKeyBasedObject(first)) {
                    return toArray(first);
                }

                // Case: [ { options: [...] } ] - check for known nested array keys
                for (String key : keys) {
                    if (first.has(key) && first.get(key).isArray()) {
                        return first.get(key);
                    }
                }
            }
            return rootNode;
        }

        if (rootNode.isObject()) {

            // First, check for known nested array keys
            for (String key : keys) {
                if (rootNode.has(key) && rootNode.get(key).isArray()) {
                    return rootNode.get(key);
                }
            }

            // Check if this is a dynamic key-based object (keys are IDs, values are record objects)
            // Pattern: { "123": {...}, "456": {...} } where each value has an "id" field matching the key
            if (isDynamicKeyBasedObject(rootNode)) {
                return toArray(rootNode);
            }

            // Single object - wrap it in an array
            ArrayNode wrapper = mapper.createArrayNode();
            wrapper.add(rootNode);
            return wrapper;
        }

        return mapper.createArrayNode();
    }

    private ArrayNode toArray(JsonNode objectNode) {
        ArrayNode array = mapper.createArrayNode();
        objectNode.fields().forEachRemaining(e -> array.add(e.getValue()));
        return array;
    }

    /**
     * Checks if the object follows the dynamic key-based pattern where:
     * - At least one value contains an "id" field
     * - The object has multiple keys OR the single value's "id" matches its key
     * This id for the response like :
     * {
     *   "123": { "id": "123", "name": "John" },
     *   "456": { "id": "456", "name": "Jane" }
     * }
     */
    private boolean isDynamicKeyBasedObject(JsonNode objectNode) {
        if (objectNode == null || !objectNode.isObject() || objectNode.isEmpty()) {
            return false;
        }

        int objectValueCount = 0;
        int idMatchCount = 0;
        int totalFields = 0;

        var fields = objectNode.fields();
        while (fields.hasNext()) {
            var entry = fields.next();
            totalFields++;
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            // All values should be objects
            if (!value.isObject()) {
                return false;
            }

            objectValueCount++;

            // Check if the value has an "id" field that matches the key
            if (value.has("id")) {
                JsonNode idNode = value.get("id");
                if (idNode.isNumber() || idNode.isTextual()) {
                    String idValue = idNode.isNumber() ? String.valueOf(idNode.asLong()) : idNode.asText();
                    if (key.equals(idValue)) {
                        idMatchCount++;
                    }
                }
            }
        }

        // Consider it a dynamic key-based object if:
        // 1. All values are objects
        // 2. Either: we have multiple keys with at least one id match, OR we have a single key with id match
        return objectValueCount > 0 && (idMatchCount > 0 || totalFields > 1);
    }

    private List<String[]> initHeaderChunk(List<String> headers) {
        List<String[]> chunk = new ArrayList<>();
        chunk.add(headers.toArray(new String[0]));
        return chunk;
    }

    private String extractIdFromNode(JsonNode node) {
        for (String idHeader : BambooHRConstants.ID_HEADERS) {
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
            downloadHelper.addBridgeStats(Map.of(entity, csvDataBuffer.getTotalRecordsCount()),
                    csvDataBuffer.getEntityProcessingStartTime(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
            csvDataBuffer.flushRemaining(downloadHelper);
            if (BambooHRConstants.MAPPING_TABLES_MAP.containsKey(entity)) {
                String childEntity = BambooHRConstants.MAPPING_TABLES_MAP.get(entity);
                CsvDataBuffer childEntityBuffer = csvDataBufferConcurrentHashMap.remove(childEntity);
                if (childEntityBuffer != null) {
                    downloadHelper.addBridgeStats(Map.of(childEntity, csvDataBuffer.getTotalRecordsCount()),
                            csvDataBuffer.getEntityProcessingStartTime(),
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.BAMBOO_HR_ENTITY.name());
                    childEntityBuffer.flushRemaining(downloadHelper);
                }
            }
        }
    }
}
