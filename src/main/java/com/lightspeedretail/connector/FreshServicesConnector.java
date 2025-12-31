package com.lightspeedretail.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightspeedretail.common.CoreCustomConstants;
import com.lightspeedretail.common.FreshServiceConstants;
import com.lightspeedretail.utils.ConnectorHelper;
import io.datalakehouse.config.Config;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.Connector;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FreshServices Connector Implementation
 *
 * Handles FreshServices API responses in the following format:
 * { "entity_name": [ {...}, {...} ] }
 *
 * Example: { "contract_types": [{"id": 1, "name": "Lease"}, {"id": 2, "name": "Maintenance"}] }
 */
public class FreshServicesConnector extends Connector {

    private static final String[] ID_HEADERS = {"DISPLAY_ID", "ID"};
    private static final String[] TIMESTAMP_FIELDS = {"UPDATED_AT", "CREATED_AT"};
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ISO_DATE_TIME,
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    };

    // Thread-safe accumulator for CSV data across multiple parent IDs
    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferConcurrentHashMap = new ConcurrentHashMap<>();

    // Last sync date for filtering non-delta entities
    private final LocalDateTime lastSyncDate;

    public FreshServicesConnector(ConnectionType connectionType, Config config, String outputPath) {
        this(connectionType, config, outputPath, null);
    }

    public FreshServicesConnector(ConnectionType connectionType, Config config, String outputPath, LocalDateTime lastSyncDate) {
        super(connectionType, config, outputPath);
        this.lastSyncDate = lastSyncDate;
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        return processData(entity, stream, headers, null, null);
    }

    @Override
    protected List<String> processData(
            String entity,
            InputStream stream,
            List<String> headers,
            String parentPlaceholderKey,
            String parentId
    ) throws IOException {
        String normalizedEntity = entity.replace("/", "-");

        try {
            // Check if this is part of multi-parent processing
            boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);

            if (isMultiParentProcessing) {
                // Accumulate data across parents, will be flushed later
                return processJsonStreamWithAccumulation(normalizedEntity, entity, stream, headers,
                    parentPlaceholderKey, parentId);
            } else {
                // Single entity processing - log and write immediately
                downloadHelper.logStartHistory(normalizedEntity,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name());

                if (stream == null) {
                    return writeEmptyFile(normalizedEntity, headers);
                }

                return processJsonStream(normalizedEntity, entity, stream, headers, parentPlaceholderKey, parentId);
            }

        } catch (IOException e) {
            System.err.println("Error processing entity " + entity + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Processes JSON stream and accumulates data across multiple parent IDs.
     * CSV will be written when all parents are processed.
     */
    private List<String> processJsonStreamWithAccumulation(String normalizedEntity, String entity,
                                                            InputStream stream, List<String> headers,
                                                            String parentPlaceholderKey, String parentId) throws IOException {
        List<String> entityIds = new ArrayList<>();

        // Get or create accumulator for this entity
        CsvDataBuffer accumulator = csvDataBufferConcurrentHashMap.computeIfAbsent(
            normalizedEntity,
            k -> new CsvDataBuffer(headers)
        );

        JsonFactory factory = MAPPER.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = MAPPER.readTree(parser);
            JsonNode dataArray = extractDataArray(rootNode, entity);

            // Process and accumulate data with timestamp filtering
            for (JsonNode record : dataArray) {
//                // Apply timestamp filtering if lastSyncDate is set
//                if (!shouldIncludeRecord(record, headers)) {
//                    continue; // Skip this record
//                }

                List<String> rowValues = ConnectorHelper.mapValues(record, headers);

                // Inject parent ID
                if (parentPlaceholderKey != null && parentId != null) {
                    int idx = headers.indexOf(parentPlaceholderKey.toUpperCase());
                    if (idx >= 0 && idx < rowValues.size()) {
                        rowValues.set(idx, parentId);
                    }
                }

                String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, ID_HEADERS);
                if (idValue != null) {
                    entityIds.add(idValue);
                }

                // Add to accumulator
                accumulator.addRow(rowValues.toArray(new String[0]));
            }
        }

        return entityIds;
    }

    /**
     * Processes JSON stream and extracts data based on FreshServices schema
     */
    private List<String> processJsonStream(String normalizedEntity, String entity,
                                           InputStream stream, List<String> headers,
                                           String parentPlaceholderKey, String parentId) throws IOException {
        List<String> entityIds = new ArrayList<>();
        List<String[]> csvChunk = new ArrayList<>();
        csvChunk.add(headers.toArray(new String[0]));

        JsonFactory factory = MAPPER.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = MAPPER.readTree(parser);

            // Extract data array using configured response key
            JsonNode dataArray = extractDataArray(rootNode, entity);

            System.out.println("Processing " + dataArray.size() + " records for entity: " + normalizedEntity);

            int lineCount = processDataArray(dataArray, csvChunk, headers, entityIds, normalizedEntity,
                parentPlaceholderKey, parentId);

            // Flush remaining data
            if (!csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(normalizedEntity, csvChunk, lineCount, config.getConnectorType());
            }

            downloadHelper.logEndHistory(normalizedEntity,
                CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name(), lineCount);

            System.out.println("Successfully processed " + lineCount + " records for entity: " + normalizedEntity);
        }

        return entityIds;
    }

    /**
     * Checks if a record should be included based on timestamp filtering.
     * Returns true if no lastSyncDate is set (full sync) or if the record was created/updated after lastSyncDate.
     * If no timestamp headers exist in the entity, returns true (include all records).
     */
    private boolean shouldIncludeRecord(JsonNode record, List<String> headers) {
        if (lastSyncDate == null) {
            return true; // No filtering, include all records
        }

        // Check if entity has any timestamp fields
        boolean hasTimestampFields = false;
        for (String timestampField : TIMESTAMP_FIELDS) {
            if (headers.contains(timestampField)) {
                hasTimestampFields = true;
                break;
            }
        }

        // If entity has no timestamp fields, include all records
        if (!hasTimestampFields) {
            return true;
        }

        // Check UPDATED_AT and CREATED_AT fields
        for (String timestampField : TIMESTAMP_FIELDS) {
            if (headers.contains(timestampField)) {
                JsonNode timestampNode = record.get(timestampField.toLowerCase());
                if (timestampNode == null) {
                    timestampNode = record.get(timestampField);
                }

                if (timestampNode != null && !timestampNode.isNull()) {
                    String timestampStr = timestampNode.asText();
                    LocalDateTime recordTimestamp = parseTimestamp(timestampStr);

                    if (recordTimestamp != null && recordTimestamp.isAfter(lastSyncDate)) {
                        return true; // Record was created/updated after lastSyncDate
                    }
                }
            }
        }

        // If we have lastSyncDate and timestamp fields exist but couldn't find valid timestamps, exclude the record
        return false;
    }

    /**
     * Parses timestamp string using multiple formats.
     */
    private LocalDateTime parseTimestamp(String timestampStr) {
        if (timestampStr == null || timestampStr.trim().isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDateTime.parse(timestampStr, formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }

        System.err.println("Warning: Could not parse timestamp: " + timestampStr);
        return null;
    }

    /**
     * Processes all records in the data array with timestamp filtering support
     */
    private int processDataArray(JsonNode dataArray, List<String[]> csvChunk, List<String> headers,
                                 List<String> entityIds, String normalizedEntity,
                                 String parentPlaceholderKey, String parentId) throws IOException {
        int lineCount = 0;
//        int filteredCount = 0;

        for (JsonNode record : dataArray) {
//            // Apply timestamp filtering if lastSyncDate is set
//            if (!shouldIncludeRecord(record, headers)) {
//                filteredCount++;
//                continue; // Skip this record
//            }

            List<String> rowValues = ConnectorHelper.mapValues(record, headers);

            // Inject parent ID if parent context exists
            if (parentPlaceholderKey != null && parentId != null) {
                int idx = headers.indexOf(parentPlaceholderKey.toUpperCase());
                if (idx >= 0 && idx < rowValues.size()) {
                    rowValues.set(idx, parentId);
                }
            }

            String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, ID_HEADERS);

            if (idValue != null) {
                entityIds.add(idValue);
            }

            csvChunk.add(rowValues.toArray(new String[0]));
            lineCount++;

            // Flush chunk when size limit reached
            if (lineCount % config.getCsvRowLimit() == 0) {
                downloadHelper.writeChunkToCsv(normalizedEntity, csvChunk, lineCount, config.getConnectorType());
                csvChunk.clear();
            }
        }

//        if (filteredCount > 0 && lastSyncDate != null) {
//            System.out.println("Filtered out " + filteredCount + " records (before " + lastSyncDate + ") for entity: " + normalizedEntity);
//        }

        return lineCount;
    }

    /**
     * Extracts data array from FreshServices response.
     * Schema: { "entity_name": [...] }
     */
    private JsonNode extractDataArray(JsonNode rootNode, String entity) {
        // Get the response key for this entity
        String responseKey = FreshServiceConstants.getEntityResponseKey(entity);

        // If specific key is configured, use it
        if (responseKey != null && rootNode.has(responseKey)) {
            return rootNode.get(responseKey);
        }

        // Try common response keys
        String[] possibleKeys = FreshServiceConstants.getPossibleResponseKeys(entity);
        for (String key : possibleKeys) {
            if (rootNode.has(key)) {
                JsonNode node = rootNode.get(key);
                if (node.isArray()) {
                    return node;
                }
            }
        }

        // Fallback: return root if it's an array
        if (rootNode.isArray()) {
            return rootNode;
        }

        // Return empty array if no data found
        return MAPPER.createArrayNode();
    }

    /**
     * Writes empty CSV file with headers only
     */
    private List<String> writeEmptyFile(String normalizedEntity, List<String> headers) throws IOException {
        List<String[]> csvChunk = new ArrayList<>();
        csvChunk.add(headers.toArray(new String[0]));
        downloadHelper.writeChunkToCsv(normalizedEntity, csvChunk, 0, config.getConnectorType());
        return new ArrayList<>();
    }

    /**
     * Flushes accumulated data for an entity to CSV.
     * Should be called after all parents have been processed.
     */
    @Override
    protected void flushEntityData(String entity) throws IOException {
        String normalizedEntity = entity.replace("/", "-");
        CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.remove(normalizedEntity);

        if (csvDataBuffer != null) {
            downloadHelper.logStartHistory(normalizedEntity,
                CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name());

            List<List<String[]>> chunks = csvDataBuffer.getChunks(config.getCsvRowLimit());
            int totalRecords = csvDataBuffer.getTotalRecords();

            int lineCount = 0;
            for (List<String[]> chunk : chunks) {
                lineCount += chunk.size() - 1; // Exclude header
                downloadHelper.writeChunkToCsv(normalizedEntity, chunk, lineCount, config.getConnectorType());
            }

            downloadHelper.logEndHistory(normalizedEntity,
                CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name(), totalRecords);

            System.out.println("Successfully flushed " + totalRecords + " records for entity: " + normalizedEntity);
        }
    }

    /**
     * Inner class to accumulate CSV data across multiple parent IDs
     */
    private static class CsvDataBuffer {
        private final List<String> headers;
        private final List<String[]> rows = new ArrayList<>();

        public CsvDataBuffer(List<String> headers) {
            this.headers = headers;
        }

        public synchronized void addRow(String[] row) {
            rows.add(row);
        }

        public synchronized int getTotalRecords() {
            return rows.size();
        }

        public synchronized List<List<String[]>> getChunks(int chunkSize) {
            List<List<String[]>> chunks = new ArrayList<>();
            List<String[]> currentChunk = new ArrayList<>();

            // Add headers to first chunk
            currentChunk.add(headers.toArray(new String[0]));

            int rowCount = 0;
            for (String[] row : rows) {
                currentChunk.add(row);
                rowCount++;

                if (rowCount % chunkSize == 0) {
                    chunks.add(currentChunk);
                    currentChunk = new ArrayList<>();
                }
            }

            // Add remaining rows
            if (!currentChunk.isEmpty()) {
                chunks.add(currentChunk);
            }

            return chunks;
        }
    }
}

