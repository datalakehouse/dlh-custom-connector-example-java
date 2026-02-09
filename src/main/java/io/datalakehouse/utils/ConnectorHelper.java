package io.datalakehouse.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.JsonUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Handles camelCase to snake_case conversion for nested fields.
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

        // 2. Nested resolution for underscore-separated fields and camelCase support
        if (key.contains("_")) {
            // Try explicit nested resolution with camelCase support first
            JsonNode nestedResult = resolveNestedCamelCase(fieldMap, key);
            if (nestedResult != null) {
                return nestedResult;
            }

            // Fallback to JsonUtils for other nested patterns
            return JsonUtils.findNestedValue(recordNode, key);
        }

        return null;
    }

    /**
     * Resolves nested fields by checking if the first part(s) of the underscore-separated
     * key correspond to a parent object in the fieldMap, then navigates into it.
     * This Basically combines the logic of nested resolution with camelCase handling.
     * This handles camelCase fields like "hiringLead" properly matching to "hiring_lead_employee_id".
     */
    private static JsonNode resolveNestedCamelCase(Map<String, JsonNode> fieldMap, String key) {
        String[] parts = key.split("_");

        // Try progressively longer prefixes as potential parent objects
        for (int i = 1; i < parts.length; i++) {
            String parentKey = String.join("_", Arrays.copyOfRange(parts, 0, i));

            // Check if this parent exists in fieldMap and is an object
            if (fieldMap.containsKey(parentKey)) {
                JsonNode parentNode = fieldMap.get(parentKey);
                if (parentNode != null && parentNode.isObject()) {
                    // Build the remaining path
                    String remainingPath = String.join("_", Arrays.copyOfRange(parts, i, parts.length));

                    // Try to find the remaining path in the parent object
                    JsonNode result = findInObject(parentNode, remainingPath);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Finds a field in a JSON object using case-insensitive and underscore-to-camelCase matching.
     * Supports both flat and nested lookups within the object.
     */
    private static JsonNode findInObject(JsonNode objectNode, String path) {
        if (objectNode == null || !objectNode.isObject()) {
            return null;
        }

        // First try direct case-insensitive match
        String pathLower = path.toLowerCase();
        var fields = objectNode.fields();
        while (fields.hasNext()) {
            var entry = fields.next();
            String fieldKey = entry.getKey();
            // Match both direct lowercase and camelCase-to-snake_case converted
            if (fieldKey.toLowerCase().equals(pathLower) ||
                camelToSnake(fieldKey).equals(pathLower)) {
                return entry.getValue();
            }
        }

        // If path contains underscores, try nested resolution within this object
        if (path.contains("_")) {
            return JsonUtils.findNestedValue(objectNode, path);
        }

        return null;
    }

    /**
     * Maps JSON node values to CSV row values based on headers.
     * Handles nested fields, arrays, objects, and special DLH columns.
     */
    public static List<String> mapValues(JsonNode recordNode, List<String> headers) {
       return mapValues(recordNode, headers, new ArrayList<>());
    }

    /**
     * Maps JSON node values to CSV row values based on headers.
     * Handles nested fields, arrays, objects, and special DLH columns.
     */
    public static List<String> mapValues(JsonNode recordNode, List<String> headers, List<String> rowValues) {
        ObjectMapper mapper = new ObjectMapper();

        // Build case-insensitive lookup map ONCE per record
        Map<String, JsonNode> fieldMap = new HashMap<>();
        if (recordNode.isObject()) {
            var iterator = recordNode.fields();
            while (iterator.hasNext()) {
                var entry = iterator.next();
                fieldMap.put(camelToSnake(entry.getKey()), entry.getValue());
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
                    } else if (valueNode.isObject()) {
                        JsonNode resolved = JsonUtils.findNestedValue(recordNode, header);
                        rowValues.add(resolved == null || resolved.isNull() ? "" : resolved.isValueNode() ? resolved.asText() : resolved.toString());
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
     * Sets DLH metadata header values in the row values list.
     * Calculates MD5 based on non-DLH columns.
     */
    public static List<String> setDlhHeaderValues(List<String> headers, List<String> rowValues) {
        if (headers == null || rowValues == null || headers.size() != rowValues.size()) {
            return rowValues;
        }

        // First, collect non-DLH column values for MD5 calculation
        List<String> nonDlhValues = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            if (!isDlhMetadataColumn(header)) {
                nonDlhValues.add(rowValues.get(i));
            }
        }

        // Calculate MD5 from non-DLH values
        String md5Value = MD5Helper.getMD5FromArguments(nonDlhValues.toArray(new String[0]));

        // Now set the DLH metadata column values at their correct indices
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            switch (header) {
                case "__ROW_MD5" -> rowValues.set(i, md5Value);
                case "__DLH_IS_DELETED" -> rowValues.set(i, "false");
                case "__DLH_IS_ACTIVE" -> rowValues.set(i, "true");
                default -> {
                    if (CoreCustomConstants.DLH_TS_COLUMNS.contains(header)) {
                        rowValues.set(i, Instant.now().toString());
                    }
                }
            }
        }

        return rowValues;
    }

    /**
     * Converts CamelCase to snake_case.
     * If input already contains underscores, it is returned as-is.
     * Examples:
     *  "firstName" -> "first_name"
     *  "FirstName" -> "first_name"
     *  "first_name" -> "first_name"
     */
    private static String camelToSnake(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder(input.length() + 5);

        char prev = 0;
        for (int i = 0; i < input.length(); i++) {
            char curr = input.charAt(i);

            if (Character.isUpperCase(curr)) {
                if (i > 0 &&
                        (Character.isLowerCase(prev) || Character.isDigit(prev))) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(curr));
            } else {
                result.append(curr);
            }
            prev = curr;
        }

        return result.toString();
    }


    /**
     * Checks if a header is a DLH metadata column.
     */
    private static boolean isDlhMetadataColumn(String header) {
        return header.equals("__ROW_MD5") ||
                header.equals("__DLH_IS_DELETED") ||
                header.equals("__DLH_IS_ACTIVE") ||
                CoreCustomConstants.DLH_TS_COLUMNS.contains(header);
    }

    private ConnectorHelper(){}
}
