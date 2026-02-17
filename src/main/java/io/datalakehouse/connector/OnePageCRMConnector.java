package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.GustoConstants;
import io.datalakehouse.common.OnePageCRMConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.utils.ConnectorHelper;
import io.datalakehouse.utils.CsvDataBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnePageCRMConnector extends DLHIngest {

    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferConcurrentHashMap = new ConcurrentHashMap<>();
    ObjectMapper mapper = new ObjectMapper();

    public OnePageCRMConnector(ConnectionType connectionType, DLHIngestConfig config, String outputPath) {
        super(connectionType, config, outputPath);
    }

    /**
     * Extracts data from OnePageCRM's response structure.
     *
     * Handles three formats:
     *
     * Format 1 - Paginated with nested entity arrays (wrapped records):
     * [
     *   {"status": 0, "data": {"companies": [{"company": {...}}, {"company": {...}}], "total_count": 29}},
     *   {"status": 0, "data": {"companies": [{"company": {...}}], "total_count": 29}}
     * ]
     *
     * Format 2 - Direct data array with wrapped records:
     * {
     *   "status": 0,
     *   "message": "OK",
     *   "data": [
     *     {"user": {...}},
     *     {"user": {...}}
     *   ]
     * }
     *
     * Format 3 - Direct data array with unwrapped records (e.g., leadsources):
     * {
     *   "status": 0,
     *   "message": "OK",
     *   "data": [
     *     {"id": "advertisement", "text": "Advertisement", "counts": 0},
     *     {"id": "referral", "text": "Referral", "counts": 5}
     *   ]
     * }
     *
     * Process:
     * 1. Detect if root is array (pagination) or object (single page)
     * 2. Extract "data" from each page
     * 3. Check if data is object with plural entity key OR direct array
     * 4. Try to unwrap records by singular name, if not wrapped add as-is
     */
    private JsonNode extractDataFromOnePageCRMResponse(JsonNode rootNode, String entity) {
        ArrayNode aggregatedData = mapper.createArrayNode();

        String pluralEntity = entity.toLowerCase().replace("-", "_").replace("/", "_");
        String singularEntity = OnePageCRMConstants.getSingularForm(pluralEntity);

        System.out.println("Processing entity - Plural: '" + pluralEntity + "', Singular: '" + singularEntity + "'");

        // Handle single page response: {"status": 0, "data": [...]}
        if (rootNode.isObject() && rootNode.has("data")) {
            JsonNode dataWrapper = rootNode.get("data");
            processDataWrapper(dataWrapper, pluralEntity, singularEntity, aggregatedData);
            System.out.println("Extracted " + aggregatedData.size() + " records from single page response");
            return aggregatedData;
        }

        // Handle paginated response (array of pages)
        if (rootNode.isArray()) {
            for (JsonNode pageElement : rootNode) {
                if (pageElement.has("data")) {
                    JsonNode dataWrapper = pageElement.get("data");
                    processDataWrapper(dataWrapper, pluralEntity, singularEntity, aggregatedData);
                }
            }
            System.out.println("Extracted " + aggregatedData.size() + " records from paginated response");
        }

        return aggregatedData;
    }

    /**
     * Processes the data wrapper which can be either:
     * 1. Object with plural entity key: {"companies": [{"company": {...}}]}
     * 2. Direct array with wrapped records: [{"user": {...}}, {"user": {...}}]
     * 3. Direct array with unwrapped records: [{"id": "...", "text": "..."}, {...}]
     */
    private void processDataWrapper(JsonNode dataWrapper, String pluralEntity, String singularEntity,
                                    com.fasterxml.jackson.databind.node.ArrayNode aggregatedData) {
        // Format 1: data is object with plural entity key
        if (dataWrapper.isObject() && dataWrapper.has(pluralEntity)) {
            JsonNode entityArray = dataWrapper.get(pluralEntity);
            if (entityArray.isArray()) {
                for (JsonNode wrappedRecord : entityArray) {
                    if (wrappedRecord.has(singularEntity)) {
                        // Record is wrapped, unwrap it
                        aggregatedData.add(wrappedRecord.get(singularEntity));
                    } else {
                        // Record is not wrapped, add as-is
                        aggregatedData.add(wrappedRecord);
                    }
                }
            }
        }
        // Format 2 & 3: data is direct array
        else if (dataWrapper.isArray()) {
            for (JsonNode record : dataWrapper) {
                if (record.has(singularEntity)) {
                    // Record is wrapped (e.g., {"user": {...}}), unwrap it
                    aggregatedData.add(record.get(singularEntity));
                } else {
                    // Record is not wrapped (e.g., {"id": "...", "text": "..."}), add as-is
                    aggregatedData.add(record);
                }
            }
        }
    }


    /**
     * Override to support incremental page-by-page processing.
     * This method processes pages one at a time, writing CSV chunks as we go
     */
    @Override
    protected List<String> processDataPaginated(
            String entity,
            String apiPath,
            Map<String, String> queryParams,
            Map<String, String> customHeaders,
            List<String> entityIds,
            PaginationInfo paginationInfo,
            List<String> headers) throws Exception {

        String normalizedEntity = entity.replace("/", "-");
        Instant startTime = Instant.now();
        downloadHelper.logStartHistory(normalizedEntity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());

        // State that needs to persist across pages
        List<String> allEntityIds = new ArrayList<>();
        List<String[]> csvChunk = new ArrayList<>();
        int[] totalLineCount = {0};  // Using array to make it effectively final for lambda
        boolean[] headerWritten = {false};

        try {

            List<String> allHeaders = new ArrayList<>(headers);

            // Process pages incrementally using callback
            connectionType.fetchDataPaginated(
                    entity,
                    apiPath,
                    queryParams,
                    customHeaders,
                    entityIds,
                    paginationInfo,
                    (pageStream, state) -> {
                        try {

                            JsonFactory factory = mapper.getFactory();

                            try (JsonParser parser = factory.createParser(pageStream)) {
                                JsonNode rootNode = mapper.readTree(parser);

                                // Extract data from this single page
                                JsonNode dataNode = extractDataFromOnePageCRMResponse(rootNode, normalizedEntity);

                                if (dataNode.isEmpty()) {
                                    System.out.println("No records in page " + state.page() + " for entity: " + normalizedEntity);
                                    return true; // Continue to next page
                                }

                                System.out.println("Page " + state.page() + " has " + dataNode.size() + " records");

                                // Add header only once on first page
                                if (!headerWritten[0]) {
                                    csvChunk.add(allHeaders.toArray(new String[0]));
                                    headerWritten[0] = true;
                                }

                                // Process each record from this page
                                for (JsonNode recordNode : dataNode) {
                                    List<String> rowValues = ConnectorHelper.mapValues(recordNode, allHeaders);
                                    String idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);

                                    if (idValue != null) {
                                        allEntityIds.add(idValue);
                                    }

                                    if (normalizedEntity.equals(OnePageCRMConstants.OnePageCRMEntityNames.DEALS)) {
                                        processDeals(recordNode);
                                    }

                                    csvChunk.add(rowValues.toArray(new String[0]));
                                    totalLineCount[0]++;

                                    // Flush to CSV when chunk size is reached
                                    if (totalLineCount[0] % config.getCsvRowLimit() == 0) {
                                        downloadHelper.writeChunkToCsv(normalizedEntity, csvChunk,
                                                totalLineCount[0], config.getConnectorType());
                                        csvChunk.clear();
                                        // Keep processing but don't add header again
                                    }
                                }

                                System.out.println("Completed page " + state.page() + ", total records so far: " + totalLineCount[0]);
                                return true; // Continue to next page

                            }
                        } catch (Exception e) {
                            System.err.println("Error processing page " + state.page() + " for entity " + normalizedEntity + ": " + e.getMessage());
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
            );

            // Flush any remaining rows
            if (!csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(normalizedEntity, csvChunk, totalLineCount[0], config.getConnectorType());
            }

            // Handle case where no data was fetched at all
            if (!headerWritten[0]) {
                csvChunk.add(allHeaders.toArray(new String[0]));
                downloadHelper.writeChunkToCsv(normalizedEntity, csvChunk, 0, config.getConnectorType());
            }

            downloadHelper.logEndHistory(normalizedEntity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name(), totalLineCount[0]);
            downloadHelper.addBridgeStats(Map.of(entity, totalLineCount[0]), startTime,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());
        } catch (Exception e) {
            downloadHelper.logWarning(entity, e.getCause().getLocalizedMessage(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());
            throw e;
        }

        // Flush any remaining data in CSV buffers for nested entities (e.g., deal_items)
        flushAllCsvBuffers();

        return allEntityIds;
    }


    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        try {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());
            Instant startTime = Instant.now();

            entity = entity.replace("/", "-");
            int lineCount = 0;
            List<String[]> csvChunk = new ArrayList<>();
            List<String> entityIds = new ArrayList<>();

            // Add header row
            List<String> allHeaders = new ArrayList<>(headers);
            csvChunk.add(allHeaders.toArray(new String[0]));

            if (stream != null) {
                JsonFactory factory = mapper.getFactory();

                try (JsonParser parser = factory.createParser(stream)) {
                    JsonNode rootNode = mapper.readTree(parser);

                    // Extract unwrapped data from OnePageCRM's paginated response
                    JsonNode dataNode = extractDataFromOnePageCRMResponse(rootNode, entity);

                    if (dataNode.isEmpty()) {
                        System.out.println("No records to process for entity: " + entity);
                        downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                        downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name(), 0);
                        downloadHelper.addBridgeStats(Map.of(entity, 0), startTime,
                                CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());
                        return entityIds;
                    }

                    System.out.println("Processing " + dataNode.size() + " records");

                    // Process each record (already unwrapped)
                    for (JsonNode recordNode : dataNode) {
                        List<String> rowValues = ConnectorHelper.mapValues(recordNode, allHeaders);
                        String idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);

                        if (idValue != null) {
                            entityIds.add(idValue);
                        }

                        if (entity.equals(OnePageCRMConstants.OnePageCRMEntityNames.DEALS)) {
                            processDeals(recordNode);
                        }

                        csvChunk.add(rowValues.toArray(new String[0]));
                        lineCount++;

                        // Flush to CSV when chunk size is reached
                        if (lineCount % config.getCsvRowLimit() == 0) {
                            downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                            csvChunk.clear();
                        }
                    }

                    // Flush remaining rows
                    if (!csvChunk.isEmpty()) {
                        downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                    }

                    downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());
                    downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name(), lineCount);
                }
            } else {
                // Write empty CSV file
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
            }

            // Flush any remaining data in CSV buffers for nested entities (e.g., deal_items)
            flushAllCsvBuffers();

            return entityIds;
        } catch (IOException e) {
            downloadHelper.logWarning(entity, e.getCause().getLocalizedMessage(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());
            throw e;
        }
    }

    private void processDeals(JsonNode recordNode) throws IOException {
        String entity = OnePageCRMConstants.OnePageCRMEntityNames.DEALS_ITEMS;
        CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(
                entity,
                k -> new CsvDataBuffer(OnePageCRMConstants.OnePageCRMHeaders.DEALS_ITEMS, config.getCsvRowLimit(),
                        entity, config.getConnectorType())
        );

        if (recordNode.has("deal_items")) {
            JsonNode dealItemsNode = recordNode.get("deal_items");
            if (dealItemsNode.isArray()) {
                for (JsonNode itemNode : dealItemsNode) {
                    List<String> rowValues = ConnectorHelper.mapValues(
                            itemNode, List.of(OnePageCRMConstants.OnePageCRMHeaders.DEALS_ITEMS));
                    csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
                }
            }
        }
    }

    /**
     * Flushes all remaining data in CSV buffers for nested entities.
     * This should be called after all entities and pages have been processed.
     */
    private void flushAllCsvBuffers() throws IOException {
        for (Map.Entry<String, CsvDataBuffer> entry : csvDataBufferConcurrentHashMap.entrySet()) {
            CsvDataBuffer buffer = entry.getValue();
            buffer.flushRemaining(downloadHelper);
            downloadHelper.addBridgeStats(Map.of(entry.getKey(), buffer.getTotalRecordsCount()), buffer.getEntityProcessingStartTime(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());
        }
        csvDataBufferConcurrentHashMap.clear();
    }

}
