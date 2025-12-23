package com.lightspeedretail.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightspeedretail.common.CoreCustomConstants;
import com.lightspeedretail.common.GustoConstants;
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
import org.apache.commons.lang3.StringUtils;

public class GustoConnector extends Connector {

    public GustoConnector(ConnectionType connectionType, Config config, String outputPath) {
        super(connectionType, config, outputPath);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {

        try{
        downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());

        // parsing logic
        entity = entity.replace("/", "-");
        int lineCount = 0;
        List<String[]> csvChunk = new ArrayList<>();
        List<String> entityIds = new ArrayList<>();
        // Add header row for new file
        List<String> allHeaders = new ArrayList<>();
        allHeaders.addAll(headers);
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
                JsonNode dataNode = rootNode;

                // If root is already an array, use it directly
                if (rootNode.isArray()) {
                    System.out.println("Root is array with " + rootNode.size() + " elements");
                    dataNode = rootNode;
                }
                // If not an array, try to find the data array within the object
                else {
                    // Check common keys where array data might be nested
                    if (rootNode.has("data") && rootNode.get("data").isArray()) {
                        dataNode = rootNode.get("data");
                        System.out.println("Found 'data' array in JSON response");
                    } else if (rootNode.has("items") && rootNode.get("items").isArray()) {
                        dataNode = rootNode.get("items");
                        System.out.println("Found 'items' array in JSON response");
                    } else if (rootNode.has("results") && rootNode.get("results").isArray()) {
                        dataNode = rootNode.get("results");
                        System.out.println("Found 'results' array in JSON response");
                    } else if (rootNode.has(StringUtils.lowerCase(entity)) &&
                            rootNode.get(StringUtils.lowerCase(entity)).isArray()) {
                        dataNode = rootNode.get(StringUtils.lowerCase(entity));
                    } else {
                        // Treat the entire object as a single record
                        System.out.println("No array found, treating root as single record");
                        List<String> rowValues = mapValues(rootNode, allHeaders);
                        idValue = getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);
                        if (null != idValue) {
                            entityIds.add(idValue);
                        }
                        csvChunk.add(rowValues.toArray(new String[0]));
                        lineCount++;
                        downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                        csvChunk.clear();
                        return entityIds;
                    }
                }

                // Process array data
                System.out.println("Processing " + dataNode.size() + " records from array");

                // If array is empty, write CSV with just headers and return
                if (dataNode.isEmpty()) {
                    System.out.println("Empty array - no records to process for entity: " + entity);
                    downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                    downloadHelper.logEndHistory(entity,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name(), 0);
                    return entityIds;
                }

                List<String> rowValues = null;
                int recordIndex = 0;
                for (JsonNode recordNode : dataNode) {

                    // If recordNode is an array (double-wrapped), unwrapping it and process ALL objects inside
                    if (recordNode.isArray()) {
                        // Check if it's an empty array - skip it
                        if (recordNode.isEmpty()) {
                            System.out.println("Skipping empty array at index " + recordIndex);
                            recordIndex++;
                            continue;
                        }

                        System.out.println("Unwrapping array record - contains " + recordNode.size() + " object(s)");

                        // Process each object in the wrapped array
                        for (JsonNode innerNode : recordNode) {
                            rowValues = mapValues(innerNode, allHeaders);
                            idValue = getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);

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
                    } else {
                        // Not an array, process directly as an object
                        rowValues = mapValues(recordNode, allHeaders);
                        idValue = getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);

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
                    recordIndex++;
                }

                // Flush any remaining rows
                if (!csvChunk.isEmpty()) {
                    downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                    csvChunk.clear();
                }
                downloadHelper.logEndHistory(entity,
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name(), lineCount);

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
                case "__ROW_MD5" -> rowValues.add("MD5");
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
