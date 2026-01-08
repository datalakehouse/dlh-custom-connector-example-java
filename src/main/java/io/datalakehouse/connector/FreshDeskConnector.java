package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.FreshDeskConstants;
import io.datalakehouse.utils.ConnectorHelper;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.DLHIngest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FreshDeskConnector extends DLHIngest {

    public FreshDeskConnector(ConnectionType connectionType, DLHIngestConfig config, String outputPath) {
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
                        List<String> rowValues = ConnectorHelper.mapValues(rootNode, allHeaders);
                        idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, "ID");
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

                                    List<String> rowValues = ConnectorHelper.mapValues(innerNode, allHeaders);
                                    idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, "ID");

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

                        List<String> rowValues = ConnectorHelper.mapValues(actualRecord, allHeaders);
                        idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues,
                                FreshDeskConstants.ID_HEADER);

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
}
