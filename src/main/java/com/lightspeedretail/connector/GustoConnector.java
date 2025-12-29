package com.lightspeedretail.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightspeedretail.common.CoreCustomConstants;
import com.lightspeedretail.common.GustoConstants;
import com.lightspeedretail.utils.ConnectorHelper;
import io.datalakehouse.common.JsonUtils;
import io.datalakehouse.config.Config;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.Connector;
import com.lightspeedretail.utils.MD5Helper;
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
                        List<String> rowValues = ConnectorHelper.mapValues(rootNode, allHeaders);
                        idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);
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
                            rowValues = ConnectorHelper.mapValues(innerNode, allHeaders);
                            idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);

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
                        rowValues = ConnectorHelper.mapValues(recordNode, allHeaders);
                        idValue = ConnectorHelper.getValueForHeader(allHeaders, rowValues, GustoConstants.ID_HEADERS);

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
}
