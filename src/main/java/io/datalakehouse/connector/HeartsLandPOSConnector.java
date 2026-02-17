package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.HeartsLandPosConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.utils.ConnectorHelper;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HeartsLandPOSConnector extends DLHIngest {

    ObjectMapper mapper = new ObjectMapper();

    public HeartsLandPOSConnector(ConnectionType connectionType, DLHIngestConfig config, String outputPath) {
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

        boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);

        if (!isMultiParentProcessing) {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.HEARTS_LAND_POS_ENTITY.name());
        }

        Instant startTime = Instant.now();
        int lineCount = 0;
        List<String[]> csvChunk = initHeaderChunk(headers);
        List<String> entityIds = new ArrayList<>();

        JsonFactory factory = mapper.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = mapper.readTree(parser);
            JsonNode dataNode = resolveDataArray(entity, rootNode);

            // If array is empty, write CSV with just headers and return
            if (dataNode.isEmpty()) {
                if (!isMultiParentProcessing) {
                    downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                    downloadHelper.addBridgeStats(Map.of(entity, 0), startTime,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.HEARTS_LAND_POS_ENTITY.name());
                    downloadHelper.logEndHistory(entity,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.HEARTS_LAND_POS_ENTITY.name(), 0);
                }
                return entityIds;
            }

            for (JsonNode recordNode : dataNode) {
                Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                for (JsonNode node : nodes) {
                    List<String> rowValues = ConnectorHelper.mapValues(node, headers);

                    // Inject parent ID if this is child entity processing
                    if (parentPlaceholderKey != null && parentId != null) {
                        int idx = headers.indexOf(parentPlaceholderKey.toUpperCase());
                        if (idx >= 0 && idx < rowValues.size()) {
                            rowValues.set(idx, parentId);
                        }
                    }

                    rowValues = ConnectorHelper.setDlhHeaderValues(headers, rowValues);

                    String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, HeartsLandPosConstants.ID_HEADERS);
                    if (idValue != null) {
                        entityIds.add(idValue);
                    }

                    csvChunk.add(rowValues.toArray(new String[0]));
                    lineCount++;

                    // When we hit chunk size, flush to CSV
                    if (lineCount % config.getCsvRowLimit() == 0) {
                        downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                        csvChunk.clear();
                    }
                }
            }

            // Flush any remaining rows
            if (!csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                csvChunk.clear();
            }

            if (!isMultiParentProcessing) {
                downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.HEARTS_LAND_POS_ENTITY.name());
                downloadHelper.logEndHistory(entity,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.HEARTS_LAND_POS_ENTITY.name(), lineCount);
            }
        }

        return entityIds;
    }

    /**
     * Override to support incremental page-by-page processing for Heartland POS pagination.
     */
    @Override
    protected List<String> processDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, List<String> headers, String parentPlaceholderKey,
            String parentId) throws Exception {

        downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.HEARTS_LAND_POS_ENTITY.name());
        Instant startTime = Instant.now();

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

                        if (dataNode.isEmpty()) {
                            return true; // Continue to next page
                        }

                        // Add header only once on first page
                        if (!headerWritten[0]) {
                            csvChunk.add(headers.toArray(new String[0]));
                            headerWritten[0] = true;
                        }

                        // Process each record from this page
                        for (JsonNode recordNode : dataNode) {
                            Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                            for (JsonNode node : nodes) {
                                List<String> rowValues = ConnectorHelper.mapValues(node, headers);
                                rowValues = ConnectorHelper.setDlhHeaderValues(headers, rowValues);

                                String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, HeartsLandPosConstants.ID_HEADERS);
                                if (idValue != null) {
                                    allEntityIds.add(idValue);
                                }

                                csvChunk.add(rowValues.toArray(new String[0]));
                                totalLineCount[0]++;

                                // Flush to CSV when chunk size is reached, NOT per page
                                if (totalLineCount[0] % config.getCsvRowLimit() == 0) {
                                    downloadHelper.writeChunkToCsv(entity, csvChunk,
                                        totalLineCount[0], config.getConnectorType());
                                    csvChunk.clear();
                                    // Keep processing but don't add header again
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

        // Flush any remaining rows
        if (!csvChunk.isEmpty()) {
            downloadHelper.writeChunkToCsv(entity, csvChunk, totalLineCount[0], config.getConnectorType());
        }

        // Handle case where no data was fetched at all
        if (!headerWritten[0]) {
            csvChunk.add(headers.toArray(new String[0]));
            downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
        }

        downloadHelper.addBridgeStats(Map.of(entity, totalLineCount[0]), startTime,
                CoreCustomConstants.HISTORY_ENTITY_TYPE.HEARTS_LAND_POS_ENTITY.name());
        downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.HEARTS_LAND_POS_ENTITY.name(), totalLineCount[0]);

        return allEntityIds;
    }

    /**
     * Resolve the data array from the response.
     * Heartland POS response structure varies by entity:
     * - Most entities: Array at root level (e.g., "accounts": [...])
     */
    private JsonNode resolveDataArray(String entity, JsonNode rootNode) {
        if (rootNode.isArray()) {
            return rootNode;
        }

        if (rootNode.isObject()) {
            // Map entity names to their response keys
            String dataKey = getDataKeyForEntity(entity);
            if (dataKey != null && rootNode.has(dataKey)) {
                JsonNode dataNode = rootNode.get(dataKey);
                if (dataNode.isArray()) {
                    return dataNode;
                }
            }

            // Fallback: look for common data keys
            String[] commonKeys = {"data", "results"};
            for (String key : commonKeys) {
                if (rootNode.has(key)) {
                    JsonNode node = rootNode.get(key);
                    if (node.isArray()) {
                        return node;
                    }
                }
            }
        }

        // Return empty array if no data found
        return mapper.createArrayNode();
    }

    /**
     * Map entity names to their response data keys
     */
    private String getDataKeyForEntity(String entity) {
        return switch (entity) {
            case HeartsLandPosConstants.HeartsLandPosEntityNames.TRANSACTION -> "transactions";
            case HeartsLandPosConstants.HeartsLandPosEntityNames.AUTHENTICATION -> "authentications";
            default -> entity.toLowerCase();
        };
    }

    private List<String[]> initHeaderChunk(List<String> headers) {
        List<String[]> chunk = new ArrayList<>();
        chunk.add(headers.toArray(new String[0]));
        return chunk;
    }
}
