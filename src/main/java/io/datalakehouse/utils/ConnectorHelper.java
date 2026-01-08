package io.datalakehouse.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.JsonUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectorHelper {

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

    /**
     * Resolves a value from the record node using case-insensitive lookup
     * and supports nested field resolution using underscore notation.
     */
    public static JsonNode resolveValue(
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
     * Maps JSON node values to CSV row values based on headers.
     * Handles nested fields, arrays, objects, and special DLH columns.
     */
    public static List<String> mapValues(JsonNode recordNode, List<String> headers) {
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

    private ConnectorHelper(){}
}
