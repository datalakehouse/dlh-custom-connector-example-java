package com.lightspeedretail.utils;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;

public class JsonUtils {
    /**
     * Find nested value using underscore-separated path
     * Example: "resource_type" -> parent "resource", child "type"
     */
    public static JsonNode findNestedValue(JsonNode recordNode, String header) {

        if (recordNode == null || header == null || header.isBlank()) {
            return null;
        }

        String[] parts = header.toLowerCase().split("_");

        // Abort nested resolution if any part is purely numeric (e.g. street_1)
        for (String part : parts) {
            if (part.matches("\\d+")) {
                return null;
            }
        }

        // Flat match first
        JsonNode flatMatch = findFieldCaseInsensitive(recordNode, header.toLowerCase());
        if (flatMatch != null) {
            return flatMatch;
        }

        // Try nested resolution using combined parents
        for (int parentEnd = 1; parentEnd < parts.length; parentEnd++) {

            String parentKey = String.join("_", Arrays.copyOfRange(parts, 0, parentEnd));
            JsonNode parentNode = findFieldCaseInsensitive(recordNode, parentKey);

            if (parentNode == null || !parentNode.isObject()) {
                continue;
            }

            // Remaining tokens become child path
            JsonNode current = parentNode;

            for (int i = parentEnd; i < parts.length; i++) {

                // Skip duplicated names (payroll.payroll_uuid)
                if (i > parentEnd && parts[i].equals(parts[i - 1])) {
                    continue;
                }

                String remainingKey = String.join("_", Arrays.copyOfRange(parts, i, parts.length));
                JsonNode directChild = findFieldCaseInsensitive(current, remainingKey);
                if (directChild != null) {
                    return directChild;
                }

                JsonNode next = findFieldCaseInsensitive(current, parts[i]);
                if (next == null) {
                    break;
                }

                current = next;
            }
        }

        return null;
    }

    /**
     * Find a field in the JSON node using case-insensitive matching
     */
    public static JsonNode findFieldCaseInsensitive(JsonNode node, String fieldName) {
        if (node == null || !node.isObject()) {
            return null;
        }

        String fieldNameLower = fieldName.toLowerCase();
        var fields = node.fields();
        while (fields.hasNext()) {
            var entry = fields.next();
            if (entry.getKey().toLowerCase().equals(fieldNameLower)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private JsonUtils() {}
}
