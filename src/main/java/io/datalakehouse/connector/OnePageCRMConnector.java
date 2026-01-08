package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.GustoConstants;
import io.datalakehouse.common.OnePageCRMConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.utils.ConnectorHelper;
import io.datalakehouse.utils.MD5Helper;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnePageCRMConnector extends DLHIngest {


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
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.node.ArrayNode aggregatedData = mapper.createArrayNode();

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
        downloadHelper.logStartHistory(normalizedEntity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());

        // State that needs to persist across pages
        List<String> allEntityIds = new ArrayList<>();
        List<String[]> csvChunk = new ArrayList<>();
        int[] totalLineCount = {0};  // Using array to make it effectively final for lambda
        boolean[] headerWritten = {false};

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

                    System.out.println("Processing page " + state.page() + " for entity: " + normalizedEntity);

                    ObjectMapper mapper = new ObjectMapper();
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
                            List<String> rowValues = mapValues(recordNode, allHeaders);
                            String idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);

                            if (idValue != null) {
                                allEntityIds.add(idValue);
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
        System.out.println("Completed entity " + normalizedEntity + " with " + totalLineCount[0] + " total records");

        return allEntityIds;
    }


    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        try {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name());

            entity = entity.replace("/", "-");
            int lineCount = 0;
            List<String[]> csvChunk = new ArrayList<>();
            List<String> entityIds = new ArrayList<>();

            // Add header row
            List<String> allHeaders = new ArrayList<>(headers);
            csvChunk.add(allHeaders.toArray(new String[0]));

            if (stream != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getFactory();

                try (JsonParser parser = factory.createParser(stream)) {
                    JsonNode rootNode = mapper.readTree(parser);

                    // Extract unwrapped data from OnePageCRM's paginated response
                    JsonNode dataNode = extractDataFromOnePageCRMResponse(rootNode, entity);

                    if (dataNode.isEmpty()) {
                        System.out.println("No records to process for entity: " + entity);
                        downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                        downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name(), 0);
                        return entityIds;
                    }

                    System.out.println("Processing " + dataNode.size() + " records");

                    // Process each record (already unwrapped)
                    for (JsonNode recordNode : dataNode) {
                        List<String> rowValues = mapValues(recordNode, allHeaders);
                        String idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);

                        if (idValue != null) {
                            entityIds.add(idValue);
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

                    downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.ONE_PAGE_CRM_ENTITY.name(), lineCount);
                }
            } else {
                // Write empty CSV file
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
            }

            return entityIds;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<String> mapValues(JsonNode recordNode, List<String> headers) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> rowValues = new ArrayList<>();

        // Build case-insensitive lookup map ONCE per record
        Map<String, JsonNode> fieldMap = new HashMap<>();
        if (recordNode.isObject()) {
            var iterator = recordNode.fields();
            while (iterator.hasNext()) {
                var entry = iterator.next();
                fieldMap.put(entry.getKey().toLowerCase(), entry.getValue());
            }
        } else {
            System.out.println("WARNING: RecordNode is NOT an object! Type: " + recordNode.getNodeType());
        }

        for (String header : headers) {
            switch (header) {
                case "__ROW_MD5" -> rowValues.add(MD5Helper.getMD5FromArguments(rowValues.toArray(new String[0])));
                case "__DLH_IS_DELETED" -> rowValues.add("false");
                case "__DLH_IS_ACTIVE" -> rowValues.add("true");

                default -> {
                    if (CoreCustomConstants.DLH_TS_COLUMNS.contains(header)) {
                        rowValues.add(Instant.now().toString());
                        continue;
                    }

                    JsonNode valueNode = ConnectorHelper.resolveValue(recordNode, fieldMap, header);

                    if (valueNode == null || valueNode.isNull()) {
                        rowValues.add("");
                    } else if (valueNode.isObject()) {
                        // Special handling for nested objects like address
                        String flattenedValue = flattenNestedObject(valueNode, header);
                        rowValues.add(flattenedValue);
                    } else if (valueNode.isArray()) {
                        try {
                            rowValues.add(mapper.writeValueAsString(valueNode));
                        } catch (Exception e) {
                            rowValues.add(valueNode.toString());
                        }
                    } else {
                        rowValues.add(valueNode.asText());
                    }
                }
            }
        }

        return rowValues;
    }

    /**
     * Flattens nested objects by extracting the sub-field that matches the header.
     * For example, if header is "ADDRESS_CITY" and we have an address object,
     * extract the "city" field from it.
     *
     * If the header matches a nested object exactly (e.g., "ADDRESS"),
     * returns the first non-null sub-field value or concatenated values.
     */
    private String flattenNestedObject(JsonNode objectNode, String header) {
        // Check if header contains underscore indicating a sub-field
        // e.g., ADDRESS_CITY, ADDRESS_STATE, etc.
        if (header.contains("_")) {
            String[] parts = header.split("_", 2);
            if (parts.length == 2) {
                String subField = parts[1].toLowerCase();

                // Check if this nested object contains the sub-field
                if (objectNode.has(subField)) {
                    JsonNode subValue = objectNode.get(subField);
                    if (subValue != null && !subValue.isNull()) {
                        return subValue.asText();
                    }
                }
            }
        }

        // If header exactly matches "ADDRESS" and it's a nested object
        // Try to extract sub-fields in order of preference
        if ("ADDRESS".equalsIgnoreCase(header)) {
            return extractAddressFields(objectNode);
        }

        // For other nested objects, try to find a meaningful value
        // Return first non-null field value
        var iterator = objectNode.fields();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            JsonNode value = entry.getValue();
            if (value != null && !value.isNull() && !value.asText().isEmpty()) {
                return value.asText();
            }
        }

        return "";
    }

    /**
     * Extracts address fields in a readable format.
     * Checks common address sub-fields and returns the first non-null value,
     * or a concatenated string of available fields.
     */
    private String extractAddressFields(JsonNode addressNode) {
        List<String> addressParts = new ArrayList<>();

        // Check for common address fields in order
        String[] addressFields = {"address", "street", "line1", "city", "state", "zip_code", "zipcode", "postal_code", "country_code", "country"};

        for (String field : addressFields) {
            if (addressNode.has(field)) {
                JsonNode fieldValue = addressNode.get(field);
                if (fieldValue != null && !fieldValue.isNull()) {
                    String text = fieldValue.asText();
                    if (!text.isEmpty() && !"null".equalsIgnoreCase(text)) {
                        addressParts.add(text);
                    }
                }
            }
        }

        // Return concatenated address parts or empty string
        return addressParts.isEmpty() ? "" : String.join(", ", addressParts);
    }

}
