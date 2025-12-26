package com.lightspeedretail.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightspeedretail.common.CoreCustomConstants;
import com.lightspeedretail.common.LightSpeedRetailXConstants;
import io.datalakehouse.config.Config;
import io.datalakehouse.connectors.core.ConnectionTypeOptions;
import io.datalakehouse.connectors.core.Connector;
import com.lightspeedretail.common.MD5Helper;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LightSpeedRetailXConnector extends Connector {
    public LightSpeedRetailXConnector(ConnectionTypeOptions type, Config config, String outputPath) {
        super(type, config, outputPath);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        downloadHelper.logStartHistory(entity,
                CoreCustomConstants.HISTORY_ENTITY_TYPE.LIGHTSPEED_RETAIL_X_ENTITY.name());
        // parsing logic
        entity = entity.replace("/", "-");
        int lineCount = 0;
        List<String[]> csvChunk = new ArrayList<>();
        List<String> entityIds = new ArrayList<>();
        // Add header row for new file
        List<String> allHeaders = new ArrayList<>();
        allHeaders.addAll(headers);
        allHeaders.addAll(List.of(LightSpeedRetailXConstants.dlhCommonColumns));
        csvChunk.add(allHeaders.toArray(new String[0]));

        String targetHeader = "ID";
        String idValue = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory(); // factory is bound to mapper
        if (stream != null) {
            try (JsonParser parser = factory.createParser(stream)) {
                // Parse the top-level object
                JsonNode rootNode = mapper.readTree(parser);
                // Extract the "data" array
                JsonNode dataArray = rootNode.path("data");
                if (!dataArray.isArray()) {
                    System.out.println("Expected 'data' array in JSON response is not found so reading data key");
                    List<String> rowValues = mapValues(dataArray, headers);
                    idValue = getValueForHeader(allHeaders, rowValues, targetHeader);
                    if (null != idValue) {
                        entityIds.add(idValue);
                    }
                    csvChunk.add(rowValues.toArray(new String[0]));
                    lineCount++;
                    downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                    csvChunk.clear();
                    return entityIds;
                }

                List<String> rowValues = null;
                for (JsonNode recordNode : dataArray) {
                    rowValues = mapValues(recordNode, headers);
                    idValue = getValueForHeader(allHeaders, rowValues, targetHeader);
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
                        CoreCustomConstants.HISTORY_ENTITY_TYPE.LIGHTSPEED_RETAIL_X_ENTITY.name(), lineCount);

            }
        } else {
            // Write empty csv file
            downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
        }
        return entityIds;
    }

    public List<String> mapValues(JsonNode recordNode, List<String> headers) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> record = mapper.convertValue(recordNode, Map.class);
        List<String> rowValues = new ArrayList<>();

        if (record != null) {
            Instant current_ts = Instant.now();
            for (String header : headers) {
                switch (header) {
                    case "__ROW_MD5" -> rowValues.add(MD5Helper.getMD5FromArguments(rowValues.toArray(new String[0])));
                    case "__DLH_IS_DELETED" -> rowValues.add("false");
                    case "__DLH_IS_ACTIVE" -> rowValues.add("true");
                    default -> {
                        if (header.endsWith("_TS")) {
                            rowValues.add(current_ts.toString());
                        } else {
                            header = header.toLowerCase();
                            Object value = record.getOrDefault(header, "");
                            rowValues.add(value == null ? "" : value.toString());
                        }
                    }
                }
            }
        }
        return rowValues;
    }

    public static String getValueForHeader(List<String> allHeaders, List<String> rowValues, String headerName) {
        int index = allHeaders.indexOf(headerName);
        if (index != -1 && index < rowValues.size()) {
            return rowValues.get(index);
        }
        return null; // header not found or index out of bounds
    }
}
