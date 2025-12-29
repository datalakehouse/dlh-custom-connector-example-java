package com.lightspeedretail.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightspeedretail.common.CoreCustomConstants;
import com.lightspeedretail.common.FreshDeskConstants;
import com.lightspeedretail.common.MD5Helper;
import io.datalakehouse.common.JsonUtils;
import io.datalakehouse.config.Config;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.Connector;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreshDeskConnector extends Connector {

    public FreshDeskConnector(ConnectionType connectionType, Config config, String outputPath) {
        super(connectionType, config, outputPath);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {

        try {
            downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name());

            // parsing logic
            entity = entity.replace("/", "-");
            int lineCount = 0;
            List<String[]> csvChunk = new ArrayList<>();
            List<String> entityIds = new ArrayList<>();

            // Add header row for new file
            List<String> allHeaders = new ArrayList<>(headers);
            csvChunk.add(allHeaders.toArray(new String[0]));

            String idValue;
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();

            if (stream != null) {
                try (JsonParser parser = factory.createParser(stream)) {
                    // Parse the top-level object
                    JsonNode rootNode = mapper.readTree(parser);
                    System.out.println("Processing entity: " + entity);

                    // Check if root is an array or has nested data
                    JsonNode dataNode;

                    // If root is already an array, use it directly
                    if (rootNode.isArray()) {
                        System.out.println("Root is array with " + rootNode.size() + " elements");

                        // Check if it's a nested array [[]] - array containing a single array
                        if (rootNode.size() == 1 && rootNode.get(0).isArray()) {
                            System.out.println("Detected nested array [[]], unwrapping inner array");
                            dataNode = rootNode.get(0);
                        } else {
                            dataNode = rootNode;
                        }

                    } else {
                        // Treat the entire object as a single record
                        System.out.println("No array found, treating root as single record");
                        List<String> rowValues = mapValues(rootNode, allHeaders);
                        idValue = getValueForHeader(allHeaders, rowValues, "ID");
                        if (null != idValue) {
                            entityIds.add(idValue);
                        }
                        csvChunk.add(rowValues.toArray(new String[0]));
                        lineCount++;
                        downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                        csvChunk.clear();
                        downloadHelper.logEndHistory(
                                entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name(), lineCount);
                        return entityIds;
                    }

                    // Process array data
                    System.out.println("Processing " + dataNode.size() + " records from array");

                    // Debug: Print structure of array elements
                    for (int i = 0; i < dataNode.size(); i++) {
                        JsonNode elem = dataNode.get(i);
                        System.out.println("Array element [" + i + "]: isArray=" + elem.isArray() + ", isObject=" + elem.isObject() + ", size=" + (elem.isArray() ? elem.size() : "N/A"));
                    }

                    // If array is empty, write CSV with just headers and return
                    if (dataNode.isEmpty()) {
                        System.out.println("Empty array - no records to process for entity: " + entity);
                        downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                        downloadHelper.logEndHistory(entity,
                                CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name(), 0);
                        return entityIds;
                    }

                    // Process each record in the array
                    int recordIndex = 0;
                    for (JsonNode recordNode : dataNode) {
                        System.out.println("Processing record #" + recordIndex + " - Type: " + recordNode.getNodeType() + ", isArray: " + recordNode.isArray() + ", isObject: " + recordNode.isObject());
                        recordIndex++;

                        // Skip empty arrays - common in responses like [[], [], {...}]
                        if (recordNode.isArray() && recordNode.isEmpty()) {
                            System.out.println("Skipping empty array at index " + (recordIndex - 1));
                            continue;
                        }

                        // Check if the record itself is an array containing a single object
                        JsonNode actualRecord = recordNode;
                        if (recordNode.isArray() && recordNode.size() > 0) {
                            // If it's an array with a single element, unwrap it
                            if (recordNode.size() == 1) {
                                actualRecord = recordNode.get(0);
                            } else {
                                // If it's an array with multiple elements, process each one
                                for (JsonNode innerNode : recordNode) {
                                    // Skip empty arrays within nested arrays
                                    if (innerNode.isArray() && innerNode.isEmpty()) {
                                        System.out.println("Skipping empty inner array");
                                        continue;
                                    }

                                    List<String> rowValues = mapValues(innerNode, allHeaders);
                                    idValue = getValueForHeader(allHeaders, rowValues, "ID");

                                    if (null != idValue) {
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
                                continue; // Skip the normal processing below
                            }
                        }

                        // Only process if actualRecord is an object
                        if (!actualRecord.isObject()) {
                            System.out.println("Skipping non-object record at index " + (recordIndex - 1) + ", type: " + actualRecord.getNodeType());
                            continue;
                        }

                        List<String> rowValues = mapValues(actualRecord, allHeaders);
                        idValue = getValueForHeader(allHeaders, rowValues, FreshDeskConstants.ID_HEADER);

                        if (null != idValue) {
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

                    // Flush any remaining rows
                    if (!csvChunk.isEmpty()) {
                        downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                        csvChunk.clear();
                    }
                    downloadHelper.logEndHistory(entity,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHDESK_ENTITY.name(), lineCount);

                }
            } else {
                // Write empty csv file
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
            }
            return entityIds;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Maps JSON node values to CSV row values based on headers.
     * Handles nested fields, arrays, objects, and special DLH columns.
     */
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
            System.out.println(recordNode.toString());
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

                    JsonNode valueNode = resolveValue(recordNode, fieldMap, header);

                    if (valueNode == null || valueNode.isNull()) {
                        rowValues.add("");
                    } else if (valueNode.isArray() || valueNode.isObject()) {
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
     * Resolves a value from the record node using case-insensitive lookup
     * and supports nested field resolution using underscore notation.
     */
    private JsonNode resolveValue(
            JsonNode recordNode,
            Map<String, JsonNode> fieldMap,
            String header) {

        String key = header.toLowerCase();

        // 1. Fast flat lookup
        if (fieldMap.containsKey(key)) {
            return fieldMap.get(key);
        }

        // 2. Nested resolution ONLY if flat not found
        if (key.contains("_")) {
            return JsonUtils.findNestedValue(recordNode, key);
        }

        return null;
    }

    /**
     * Gets the value for a specific header from the row values.
     * Useful for extracting ID fields.
     */
    public static String getValueForHeader(List<String> allHeaders, List<String> rowValues, String... headerNames) {
        if (allHeaders == null || rowValues == null || headerNames == null) {
            return null;
        }
        for (String headerName : headerNames) {
            if (headerName == null) continue;
            int index = allHeaders.indexOf(headerName);
            if (index != -1 && index < rowValues.size()) {
                return rowValues.get(index);
            }
        }
        return null; // no matching header found or index out of bounds
    }
}
