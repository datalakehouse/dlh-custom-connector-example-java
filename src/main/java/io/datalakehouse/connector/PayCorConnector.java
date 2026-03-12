package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.PayCorConstants;
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

public class PayCorConnector extends DLHIngest {

    private static final String RECORDS = "records";
    private static final String PAY_COR_ENTITY = CoreCustomConstants.HISTORY_ENTITY_TYPE.PAY_COR_ENTITY.name();

    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferConcurrentHashMap = new ConcurrentHashMap<>();

    public PayCorConnector(RestConnectionType type, DLHIngestConfig config, String outputPath) {
        super(type, config, outputPath);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        return processData(entity, stream, headers, null, null);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers,
                                       String parentPlaceholderKey, String parentId) throws IOException {

        if (stream == null) {
            return List.of();
        }

        boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);
        boolean isNewBuffer = isMultiParentProcessing && !csvDataBufferConcurrentHashMap.containsKey(entity);

        CsvDataBuffer csvDataBuffer = isMultiParentProcessing
                ? csvDataBufferConcurrentHashMap.computeIfAbsent(entity,
                k -> new CsvDataBuffer(headers.toArray(new String[0]), config.getCsvRowLimit(), k, config.getConnectorType()))
                : null;

        if (isNewBuffer) {
            downloadHelper.logStartHistory(entity, PAY_COR_ENTITY);
        }

        if (!isMultiParentProcessing) {
            downloadHelper.logStartHistory(entity, PAY_COR_ENTITY);
        }

        Instant startTime = Instant.now();
        int lineCount = 0;
        List<String[]> csvChunk = isMultiParentProcessing ? null : new ArrayList<>();
        List<String> entityIds = new ArrayList<>();
        if (!isMultiParentProcessing) {
            csvChunk.add(headers.toArray(new String[0]));
        }

        JsonFactory factory = mapper.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = mapper.readTree(parser);
            JsonNode dataNode = resolveDataArray(rootNode);

            if (dataNode.isEmpty()) {
                if (!isMultiParentProcessing) {
                    downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                    downloadHelper.addBridgeStats(Map.of(entity, 0), startTime, PAY_COR_ENTITY);
                    downloadHelper.logEndHistory(entity, PAY_COR_ENTITY, 0);
                }
                return entityIds;
            }

            for (JsonNode recordNode : dataNode) {
                Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                for (JsonNode node : nodes) {
                    List<String> rowValues = ConnectorHelper.mapValues(node, headers);
                    applyParentContext(headers, rowValues, parentPlaceholderKey, parentId);
                    rowValues = ConnectorHelper.setDlhHeaderValues(headers, rowValues);

                    String idValue;
                    if (entity.equalsIgnoreCase(PayCorConstants.PayCorEntityNames.LEGAL_ENTITY_TENANTS)) {
                        idValue = ConnectorHelper.getValueForHeader(headers, rowValues, "LEGAL_ENTITY_ID");
                    } else {
                        idValue = ConnectorHelper.getValueForHeader(headers, rowValues, PayCorConstants.ID_HEADERS);
                    }

                    if (idValue != null) {
                        entityIds.add(idValue);
                    }

                    if (isMultiParentProcessing) {
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

            if (!isMultiParentProcessing) {
                downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime, PAY_COR_ENTITY);
                downloadHelper.logEndHistory(entity, PAY_COR_ENTITY, lineCount);
            }
            return entityIds;
        } catch (IOException e) {
            downloadHelper.logWarning(entity, Objects.nonNull(e.getCause()) ? e.getCause().getLocalizedMessage() : e.getMessage(),
                    PAY_COR_ENTITY);
            throw e;
        }
    }

    @Override
    protected List<String> processDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, List<String> headers, String parentPlaceholderKey,
            String parentId) throws Exception {

        boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);
        boolean isNewBuffer = isMultiParentProcessing && !csvDataBufferConcurrentHashMap.containsKey(entity);

        CsvDataBuffer csvDataBuffer = isMultiParentProcessing
                ? csvDataBufferConcurrentHashMap.computeIfAbsent(entity,
                k -> new CsvDataBuffer(headers.toArray(new String[0]), config.getCsvRowLimit(), k, config.getConnectorType()))
                : null;

        if (isNewBuffer) {
            downloadHelper.logStartHistory(entity, PAY_COR_ENTITY);
        }

        if (!isMultiParentProcessing) {
            downloadHelper.logStartHistory(entity, PAY_COR_ENTITY);
        }

        Instant startTime = Instant.now();
        List<String> allEntityIds = new ArrayList<>();
        List<String[]> csvChunk = isMultiParentProcessing ? null : new ArrayList<>();
        int[] totalLineCount = {0};
        boolean[] headerWritten = {false};

        try {
            connectionType.fetchDataPaginated(entity, apiPath, queryParams, customHeaders, entityIds, paginationInfo,
                    (pageStream, state) -> {
                        try {
                            if (pageStream == null) {
                                return true;
                            }

                            JsonFactory factory = mapper.getFactory();
                            try (JsonParser parser = factory.createParser(pageStream)) {
                                JsonNode rootNode = mapper.readTree(parser);
                                JsonNode dataNode = resolveDataArray(rootNode);

                                if (!isMultiParentProcessing && !headerWritten[0]) {
                                    csvChunk.add(headers.toArray(new String[0]));
                                    headerWritten[0] = true;
                                }

                                if (dataNode.isEmpty()) {
                                    return true;
                                }

                                for (JsonNode recordNode : dataNode) {
                                    Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                                    for (JsonNode node : nodes) {
                                        List<String> rowValues = ConnectorHelper.mapValues(node, headers);
                                        applyParentContext(headers, rowValues, parentPlaceholderKey, parentId);
                                        rowValues = ConnectorHelper.setDlhHeaderValues(headers, rowValues);

                                        String idValue;
                                        if (entity.equalsIgnoreCase(PayCorConstants.PayCorEntityNames.LEGAL_ENTITY_TENANTS)) {
                                            idValue = ConnectorHelper.getValueForHeader(headers, rowValues, "LEGAL_ENTITY_ID");
                                        } else {
                                            idValue = ConnectorHelper.getValueForHeader(headers, rowValues, PayCorConstants.ID_HEADERS);
                                        }

                                        if (idValue != null) {
                                            allEntityIds.add(idValue);
                                        }

                                        if (isMultiParentProcessing) {
                                            csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
                                        } else {
                                            csvChunk.add(rowValues.toArray(new String[0]));
                                            totalLineCount[0]++;

                                            if (totalLineCount[0] % config.getCsvRowLimit() == 0) {
                                                downloadHelper.writeChunkToCsv(entity, csvChunk, totalLineCount[0], config.getConnectorType());
                                                csvChunk.clear();
                                            }
                                        }
                                    }
                                }
                            }
                            return true;
                        } catch (Exception e) {
                            throw new RuntimeException("Error processing page " + state.page() + " for entity " + entity, e);
                        }
                    }
            );

            if (!isMultiParentProcessing && !csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, totalLineCount[0], config.getConnectorType());
            }

            if (!isMultiParentProcessing && !headerWritten[0]) {
                csvChunk.add(headers.toArray(new String[0]));
                downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
            }

            if (!isMultiParentProcessing) {
                downloadHelper.addBridgeStats(Map.of(entity, totalLineCount[0]), startTime, PAY_COR_ENTITY);
                downloadHelper.logEndHistory(entity, PAY_COR_ENTITY, totalLineCount[0]);
            }
            return allEntityIds;
        } catch (RuntimeException e) {
            downloadHelper.logWarning(entity, e.getCause().getCause().getLocalizedMessage(), PAY_COR_ENTITY);
            throw e;
        }
    }

    private JsonNode resolveDataArray(JsonNode rootNode) {
        if (rootNode == null || rootNode.isNull()) {
            return mapper.createArrayNode();
        }

        if (rootNode.isArray() && rootNode.size() > 1 && !rootNode.get(0).has(RECORDS)) {
            return rootNode;
        } else if (rootNode.isArray() && rootNode.size() == 1 &&
                rootNode.get(0).has(RECORDS) && rootNode.get(0).get(RECORDS).isArray()) {
            return rootNode.get(0).get(RECORDS);
        }

        if (rootNode.has(RECORDS) && rootNode.get(RECORDS).isArray()) {
            return rootNode.get(RECORDS);
        }

        if (rootNode.isArray() && rootNode.size() == 1 &&
                rootNode.get(0).has("policies") && rootNode.get(0).get("policies").isArray()) {
            return rootNode.get(0).get("policies");
        }

        String[] fallbackKeys = {"data", "items", "results", "result", "userLegalEntities"};
        for (String key : fallbackKeys) {
            if (rootNode.has(key) && rootNode.get(key).isArray()) {
                return rootNode.get(key);
            }
        }

        return mapper.createArrayNode().add(rootNode);
    }

    private void applyParentContext(List<String> headers, List<String> rowValues,
                                    String parentPlaceholderKey, String parentId) {
        if (parentPlaceholderKey == null || parentId == null) {
            return;
        }

        int idx = findParentHeaderIndex(headers, parentPlaceholderKey);
        if (idx >= 0 && idx < rowValues.size()) {
            rowValues.set(idx, parentId);
        }
    }

    private int findParentHeaderIndex(List<String> headers, String parentPlaceholderKey) {
        String normalizedParentKey = normalizeHeader(parentPlaceholderKey);
        for (int i = 0; i < headers.size(); i++) {
            if (normalizeHeader(headers.get(i)).equals(normalizedParentKey)) {
                return i;
            }
        }
        return -1;
    }

    private String normalizeHeader(String value) {
        return value == null ? "" : value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    @Override
    protected void flushEntityData(String entity) throws IOException {
        CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.remove(entity);
        if (csvDataBuffer != null) {
            csvDataBuffer.flushRemaining(downloadHelper);
            int totalRecords = csvDataBuffer.getTotalRecordsCount();
            downloadHelper.addBridgeStats(Map.of(entity, totalRecords), csvDataBuffer.getEntityProcessingStartTime(), PAY_COR_ENTITY);
            downloadHelper.logEndHistory(entity, PAY_COR_ENTITY, totalRecords);
        }
    }
}
