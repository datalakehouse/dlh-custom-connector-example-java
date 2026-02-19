package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CloverConstants;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;
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

public class CloverConnector extends DLHIngest {

    ObjectMapper mapper = new ObjectMapper();
    // Thread-safe accumulator for CSV data across multiple parent IDs
    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferConcurrentHashMap = new ConcurrentHashMap<>();

    public CloverConnector(RestConnectionType type, DLHIngestConfig config, String outputPath) {
        super(type, config, outputPath);
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

        boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);

        CsvDataBuffer csvDataBuffer = null;
        List<String[]> csvChunk = null;
        int lineCount = 0;
        List<String> entityIds = new ArrayList<>();
        Instant startOfLoopForEntity = Instant.now();

        // For multi-parent processing, use shared buffer; otherwise, use regular CSV chunks
        if (isMultiParentProcessing) {
            boolean isNewBuffer = !csvDataBufferConcurrentHashMap.containsKey(entity);
            csvDataBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(entity,
                    k -> new CsvDataBuffer(headers.toArray(new String[0]), config.getCsvRowLimit(), entity, config.getConnectorType()));

            if (isNewBuffer) {
                downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name());
            }
        } else {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name());
            csvChunk = new ArrayList<>();
            csvChunk.add(headers.toArray(new String[0]));
        }

        try {

            JsonFactory factory = mapper.getFactory();
            try (JsonParser parser = factory.createParser(stream)) {
                JsonNode rootNode = mapper.readTree(parser);
                JsonNode dataNode = resolveDataArray(entity, rootNode);

                // Handle empty data
                if (dataNode.isEmpty() && !isMultiParentProcessing) {
                    downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                    downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name(), 0);
                    return entityIds;
                }

                for (JsonNode recordNode : dataNode) {
                    Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                    for (JsonNode node : nodes) {
                        List<String> rowValues = ConnectorHelper.mapValues(node, headers);

                        // Apply DLH common headers
                        if (parentPlaceholderKey != null && parentId != null) {
                            // Replace parent placeholder with actual parent ID
                            for (int i = 0; i < headers.size(); i++) {
                                if (headers.get(i).equals(parentPlaceholderKey)) {
                                    rowValues.set(i, parentId);
                                    break;
                                }
                            }
                        }

                        rowValues = ConnectorHelper.setDlhHeaderValues(headers, rowValues);

                        String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, CloverConstants.ID_HEADERS);
                        if (idValue != null) entityIds.add(idValue);

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

                if (!isMultiParentProcessing) {
                    downloadHelper.logEndHistory(entity,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name(), lineCount);

                    // Add Bridge stats
                    Map<String, Integer> recordCounts = new java.util.HashMap<>();
                    recordCounts.put(entity, lineCount);
                    downloadHelper.addBridgeStats(recordCounts, startOfLoopForEntity, config.getConnectorType());
                }
            }

            return entityIds;
        } catch (IOException e) {
            downloadHelper.logWarning(entity, e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getMessage(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name());
            throw e;
        }
    }

    /**
     * Override to support incremental page-by-page processing for Clover pagination.
     */
    @Override
    protected List<String> processDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, List<String> headers, String parentPlaceholderKey,
            String parentId) throws Exception {

        boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);

        // For multi-parent processing, log start history only once when buffer is created
        boolean isNewBuffer = isMultiParentProcessing && !csvDataBufferConcurrentHashMap.containsKey(entity);

        CsvDataBuffer csvDataBuffer = isMultiParentProcessing
                ? csvDataBufferConcurrentHashMap.computeIfAbsent(entity,
                k -> new CsvDataBuffer(headers.toArray(new String[0]),
                        config.getCsvRowLimit(), k, config.getConnectorType()))
                : null;

        if (isNewBuffer) {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name());
        }

        // For single entity processing, log start history normally
        if (!isMultiParentProcessing) {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name());
        }

        Instant startOfLoopForEntity = Instant.now();
        // State that needs to persist across pages
        List<String> allEntityIds = new ArrayList<>();
        List<String[]> csvChunk = isMultiParentProcessing ? null : new ArrayList<>();
        int[] totalLineCount = {0};  // Using array to make it effectively final for lambda
        boolean[] headerWritten = {false};
        try {

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

                                if (dataNode.isEmpty()) {
                                    return true; // Continue to next page
                                }

                                // Add header only once on first page for single-parent processing
                                if (!isMultiParentProcessing && !headerWritten[0]) {
                                    csvChunk.add(headers.toArray(new String[0]));
                                    headerWritten[0] = true;
                                }

                                // Process each record from this page
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

                                        String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, CloverConstants.ID_HEADERS);
                                        if (idValue != null) {
                                            allEntityIds.add(idValue);
                                        }

                                        if (isMultiParentProcessing) {
                                            csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
                                        } else {
                                            csvChunk.add(rowValues.toArray(new String[0]));
                                            totalLineCount[0]++;

                                            // Flush to CSV when chunk size is reached
                                            if (totalLineCount[0] % config.getCsvRowLimit() == 0) {
                                                downloadHelper.writeChunkToCsv(entity, csvChunk,
                                                        totalLineCount[0], config.getConnectorType());
                                                csvChunk.clear();
                                                // Keep processing but don't add header again
                                            }
                                        }
                                    }
                                }

                                return true; // Continue to next page

                            }
                        } catch (Exception e) {
                            throw new RuntimeException("Error processing page " + state.page() + " for entity " + entity, e);
                        }
                    }

            );

            // Flush any remaining rows for single-parent processing
            if (!isMultiParentProcessing && !csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, totalLineCount[0], config.getConnectorType());
            }

            // Handle case where no data was fetched at all for single-parent processing
            if (!isMultiParentProcessing && !headerWritten[0]) {
                csvChunk.add(headers.toArray(new String[0]));
                downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
            }

            if (!isMultiParentProcessing) {
                downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name(), totalLineCount[0]);

                // Add Bridge stats
                downloadHelper.addBridgeStats(Map.of(entity, totalLineCount[0]), startOfLoopForEntity,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name());
            }

            return allEntityIds;
        } catch (IOException e) {
            downloadHelper.logWarning(entity, e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getMessage(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name());
            throw e;
        }
    }

    private JsonNode resolveDataArray(String entity, JsonNode rootNode) {

        // For single object responses
        if (!rootNode.isArray() && !rootNode.has(CloverConstants.ELEMENTS) && !rootNode.has("result")) {
            // Single object - wrap in array
            return new ObjectMapper().createArrayNode().add(rootNode);
        }

        // For array responses
        if (rootNode.isArray() && rootNode.size() > 1) {
            return rootNode;
        }

        if (rootNode.isArray() && rootNode.size() == 1) {
            JsonNode singleNode = rootNode.get(0);
            if (singleNode.has(CloverConstants.ELEMENTS) && singleNode.get(CloverConstants.ELEMENTS).isArray()) {
                return singleNode.get(CloverConstants.ELEMENTS);
            }
            return new ObjectMapper().createArrayNode().add(singleNode);
        }

        // For responses with "elements" array (most Clover APIs)
        if (rootNode.has(CloverConstants.ELEMENTS) && rootNode.get(CloverConstants.ELEMENTS).isArray()) {
            return rootNode.get(CloverConstants.ELEMENTS);
        }

        // Try common keys (fallback)
        String[] keys = {"data", "items", "results", StringUtils.lowerCase(entity)};
        for (String key : keys) {
            if (rootNode.has(key) && rootNode.get(key).isArray()) {
                return rootNode.get(key);
            }
        }

        return new ObjectMapper().createArrayNode().add(rootNode);
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
            downloadHelper.addBridgeStats(Map.of(entity, csvDataBuffer.getTotalRecordsCount()),
                    csvDataBuffer.getEntityProcessingStartTime(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.CLOVER_ENTITY.name());
        }
    }
}
