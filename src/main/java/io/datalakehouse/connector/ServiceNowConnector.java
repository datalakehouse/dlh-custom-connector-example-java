package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.ServiceNowConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;
import io.datalakehouse.utils.ConnectorHelper;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceNowConnector extends DLHIngest {

    ObjectMapper mapper = new ObjectMapper();

    public ServiceNowConnector(RestConnectionType type, DLHIngestConfig config, String outputPath) {
        super(type, config, outputPath);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {

        downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.SERVICE_NOW_ENTITY.name());
        Instant startTime = Instant.now();
        int lineCount = 0;
        List<String[]> csvChunk = new ArrayList<>();
        List<String> entityIds = new ArrayList<>();

        // Response doesn't return any data
        if (stream == null) {
            return entityIds;
        }

        JsonFactory factory = mapper.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = mapper.readTree(parser);

            JsonNode dataNode = resolveDataArray(rootNode);

            // Add header row for single-parent processing
            csvChunk.add(headers.toArray(new String[0]));
            if (dataNode.isEmpty()) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                downloadHelper.addBridgeStats(Map.of(entity, 0), startTime,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.SERVICE_NOW_ENTITY.name());
                downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.SERVICE_NOW_ENTITY.name(), 0);
                return entityIds;
            }
            for (JsonNode recordNode : dataNode) {
                Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                for (JsonNode node : nodes) {
                    List<String> rowValues = ConnectorHelper.mapValues(node, headers);
                    String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, ServiceNowConstants.ID_HEADERS);
                    if (idValue != null) entityIds.add(idValue);

                    csvChunk.add(rowValues.toArray(new String[0]));
                    lineCount++;
                    if (lineCount % config.getCsvRowLimit() == 0) {
                        downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                        csvChunk.clear(); // DO NOT re-add header
                    }
                }
            }

            // Write remaining rows
            if (csvChunk.size() > 1) { // More than just header
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
            }
            downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.SERVICE_NOW_ENTITY.name());
            downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.SERVICE_NOW_ENTITY.name(), lineCount);
        }

        return entityIds;
    }

    /**
     * Override to support incremental page-by-page processing for ServiceNow pagination.
     */
    @Override
    protected List<String> processDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, List<String> headers, String parentPlaceholderKey,
            String parentId) throws Exception {

        downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.SERVICE_NOW_ENTITY.name());
        Instant startTime = Instant.now();

        // State that needs to persist across pages
        List<String> allEntityIds = new ArrayList<>();
        List<String[]> csvChunk = new ArrayList<>();
        int[] totalLineCount = {0};
        boolean[] headerWritten = {false};

        // Process pages incrementally using callback
        connectionType.fetchDataPaginated(entity, apiPath, queryParams, customHeaders, entityIds, paginationInfo,
            (pageStream, state) -> {
                try {
                    JsonFactory factory = mapper.getFactory();

                    try (JsonParser parser = factory.createParser(pageStream)) {
                        JsonNode rootNode = mapper.readTree(parser);
                        JsonNode dataNode = resolveDataArray(rootNode);

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
                                String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, ServiceNowConstants.ID_HEADERS);

                                if (idValue != null) {
                                    allEntityIds.add(idValue);
                                }

                                csvChunk.add(rowValues.toArray(new String[0]));
                                totalLineCount[0]++;

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

        downloadHelper.addBridgeStats(Map.of(entity, totalLineCount[0]), startTime,
                CoreCustomConstants.HISTORY_ENTITY_TYPE.SERVICE_NOW_ENTITY.name());
        downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.SERVICE_NOW_ENTITY.name(), totalLineCount[0]);

        return allEntityIds;
    }

    private JsonNode resolveDataArray(JsonNode rootNode) {

        if (rootNode.isArray()) {
            return rootNode;
        }

        // ServiceNow returns data in a "result" field
        String[] keys = {"result"};
        for (String key : keys) {
            if (rootNode.has(key) && rootNode.get(key).isArray()) {
                return rootNode.get(key);
            }
        }

        return new ObjectMapper().createArrayNode().add(rootNode);
    }
}
