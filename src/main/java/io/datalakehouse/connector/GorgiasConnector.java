package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.GorgiasConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;
import io.datalakehouse.utils.ConnectorHelper;
import io.datalakehouse.utils.CsvDataBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class GorgiasConnector extends DLHIngest {

    private static final String GORGIAS_ENTITY = CoreCustomConstants.HISTORY_ENTITY_TYPE.GORGIAS_ENTITY.name();
    private static final String DATA_FIELD = "data";

    private final ObjectMapper mapper = new ObjectMapper();

    /** Thread-safe accumulator used when multiple parent IDs feed the same child entity. */
    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferMap = new ConcurrentHashMap<>();

    public GorgiasConnector(RestConnectionType type, DLHIngestConfig config, String outputPath) {
        super(type, config, outputPath);
    }


    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers)
            throws IOException {
        return processData(entity, stream, headers, null, null);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers,
                                       String parentPlaceholderKey, String parentId) throws IOException {
        if (stream == null) {
            return List.of();
        }

        boolean isMultiParent = (parentPlaceholderKey != null && parentId != null);

        CsvDataBuffer csvDataBuffer = null;
        List<String[]> csvChunk = null;
        int lineCount = 0;
        List<String> entityIds = new ArrayList<>();
        Instant startTime = Instant.now();

        if (isMultiParent) {
            boolean isNewBuffer = !csvDataBufferMap.containsKey(entity);
            csvDataBuffer = csvDataBufferMap.computeIfAbsent(entity,
                    k -> new CsvDataBuffer(headers.toArray(new String[0]),
                            config.getCsvRowLimit(), k, config.getConnectorType()));
            if (isNewBuffer) {
                downloadHelper.logStartHistory(entity, GORGIAS_ENTITY);
            }
        } else {
            downloadHelper.logStartHistory(entity, GORGIAS_ENTITY);
            csvChunk = new ArrayList<>();
            csvChunk.add(headers.toArray(new String[0]));
        }

        JsonFactory factory = mapper.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = mapper.readTree(parser);
            JsonNode dataNode = resolveDataArray(rootNode);

            if (dataNode.isEmpty() && !isMultiParent) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                downloadHelper.logEndHistory(entity, GORGIAS_ENTITY, 0);
                return entityIds;
            }

            for (JsonNode recordNode : dataNode) {
                Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                for (JsonNode node : nodes) {
                    List<String> rowValues = ConnectorHelper.mapValues(node, headers);
                    applyParentContext(headers, rowValues, parentPlaceholderKey, parentId);
                    rowValues = ConnectorHelper.setDlhHeaderValues(headers, rowValues);

                    String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, GorgiasConstants.ID_HEADERS);
                    if (idValue != null) entityIds.add(idValue);

                    if (isMultiParent) {
                        csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
                    } else {
                        csvChunk.add(rowValues.toArray(new String[0]));
                        lineCount++;
                        if (lineCount % config.getCsvRowLimit() == 0) {
                            downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                            csvChunk.clear();
                        }
                    }
                }
            }

            if (Objects.nonNull(csvChunk) && !csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
            }

            if (!isMultiParent) {
                downloadHelper.logEndHistory(entity, GORGIAS_ENTITY, lineCount);
                downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime, GORGIAS_ENTITY);
            }

            return entityIds;
        } catch (IOException e) {
            downloadHelper.logWarning(entity,
                    e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getMessage(),
                    GORGIAS_ENTITY);
            throw e;
        }
    }

    @Override
    protected List<String> processDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams,
            Map<String, String> customHeaders, List<String> entityIds,
            PaginationInfo paginationInfo, List<String> headers,
            String parentPlaceholderKey, String parentId) throws Exception {

        boolean isMultiParent = (parentPlaceholderKey != null && parentId != null);
        boolean isNewBuffer = isMultiParent && !csvDataBufferMap.containsKey(entity);

        CsvDataBuffer csvDataBuffer = isMultiParent
                ? csvDataBufferMap.computeIfAbsent(entity,
                        k -> new CsvDataBuffer(headers.toArray(new String[0]),
                                config.getCsvRowLimit(), k, config.getConnectorType()))
                : null;

        if (isNewBuffer) {
            downloadHelper.logStartHistory(entity, GORGIAS_ENTITY);
        }
        if (!isMultiParent) {
            downloadHelper.logStartHistory(entity, GORGIAS_ENTITY);
        }

        Instant startTime = Instant.now();
        List<String> allEntityIds = new ArrayList<>();
        List<String[]> csvChunk = isMultiParent ? null : new ArrayList<>();
        int[] totalLineCount = {0};
        boolean[] headerWritten = {false};

        try {
            connectionType.fetchDataPaginated(entity, apiPath, queryParams, customHeaders, entityIds, paginationInfo,
                    (pageStream, state) -> {
                        try {
                            if (pageStream == null) return true;

                            JsonFactory factory = mapper.getFactory();
                            try (JsonParser parser = factory.createParser(pageStream)) {
                                JsonNode rootNode = mapper.readTree(parser);
                                JsonNode dataNode = resolveDataArray(rootNode);

                                if (!isMultiParent && !headerWritten[0]) {
                                    csvChunk.add(headers.toArray(new String[0]));
                                    headerWritten[0] = true;
                                }

                                if (dataNode.isEmpty()) return true;

                                for (JsonNode recordNode : dataNode) {
                                    Iterable<JsonNode> nodes = recordNode.isArray()
                                            ? recordNode : List.of(recordNode);
                                    for (JsonNode node : nodes) {
                                        List<String> rowValues = ConnectorHelper.mapValues(node, headers);
                                        applyParentContext(headers, rowValues, parentPlaceholderKey, parentId);
                                        rowValues = ConnectorHelper.setDlhHeaderValues(headers, rowValues);

                                        String idValue = ConnectorHelper.getValueForHeader(
                                                headers, rowValues, GorgiasConstants.ID_HEADERS);
                                        if (idValue != null) allEntityIds.add(idValue);

                                        if (isMultiParent) {
                                            csvDataBuffer.addRowValues(
                                                    rowValues.toArray(new String[0]), downloadHelper);
                                        } else {
                                            csvChunk.add(rowValues.toArray(new String[0]));
                                            totalLineCount[0]++;
                                            if (totalLineCount[0] % config.getCsvRowLimit() == 0) {
                                                downloadHelper.writeChunkToCsv(entity, csvChunk,
                                                        totalLineCount[0], config.getConnectorType());
                                                csvChunk.clear();
                                            }
                                        }
                                    }
                                }
                            }
                            return true;
                        } catch (Exception e) {
                            throw new RuntimeException(
                                    "Error processing page " + state.page() + " for entity " + entity, e);
                        }
                    });

            // Flush remaining rows for single-entity processing
            if (!isMultiParent && csvChunk != null && !csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, totalLineCount[0], config.getConnectorType());
            }

            // Write empty file if no data was received
            if (!isMultiParent && !headerWritten[0]) {
                List<String[]> emptyChunk = new ArrayList<>();
                emptyChunk.add(headers.toArray(new String[0]));
                downloadHelper.writeChunkToCsv(entity, emptyChunk, 0, config.getConnectorType());
            }

            if (!isMultiParent) {
                downloadHelper.logEndHistory(entity, GORGIAS_ENTITY, totalLineCount[0]);
                downloadHelper.addBridgeStats(Map.of(entity, totalLineCount[0]), startTime, GORGIAS_ENTITY);
            }

            return allEntityIds;
        } catch (RuntimeException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            downloadHelper.logWarning(entity,
                    cause.getLocalizedMessage() != null ? cause.getLocalizedMessage() : cause.getMessage(),
                    GORGIAS_ENTITY);
            throw e;
        }
    }


    @Override
    protected void flushEntityData(String entity) throws IOException {
        CsvDataBuffer buffer = csvDataBufferMap.remove(entity);
        if (buffer != null) {
            buffer.flushRemaining(downloadHelper);
            downloadHelper.addBridgeStats(
                    Map.of(entity, buffer.getTotalRecordsCount()),
                    buffer.getEntityProcessingStartTime(),
                    GORGIAS_ENTITY);
            downloadHelper.logEndHistory(entity, GORGIAS_ENTITY, buffer.getTotalRecordsCount());
        }
    }

    private JsonNode resolveDataArray(JsonNode rootNode) {
        if (rootNode == null || rootNode.isNull()) {
            return mapper.createArrayNode();
        }

        if (rootNode.isArray() && rootNode.size() == 1 && rootNode.get(0).has(DATA_FIELD) &&
                rootNode.get(0).get(DATA_FIELD).isArray()) {
            return rootNode.get(0).get(DATA_FIELD);
        }

        // Envelope response with "data" array
        if (rootNode.isObject() && rootNode.has(DATA_FIELD) && rootNode.get(DATA_FIELD).isArray()) {
            return rootNode.get(DATA_FIELD);
        }

        // Raw array (e.g. voice-call-recordings)
        if (rootNode.isArray()) {
            return rootNode;
        }

        // Single-object response (e.g. account endpoint)
        if (rootNode.isObject()) {
            return mapper.createArrayNode().add(rootNode);
        }

        return mapper.createArrayNode();
    }

    private void applyParentContext(List<String> headers, List<String> rowValues,
                                    String parentPlaceholderKey, String parentId) {
        if (parentPlaceholderKey == null || parentId == null) return;

        String normalizedKey = normalize(parentPlaceholderKey);
        for (int i = 0; i < headers.size(); i++) {
            if (normalize(headers.get(i)).equals(normalizedKey)) {
                rowValues.set(i, parentId);
                return;
            }
        }
    }

    private static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }
}

