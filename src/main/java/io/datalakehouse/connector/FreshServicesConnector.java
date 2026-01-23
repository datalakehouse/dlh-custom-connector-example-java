package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.FreshServiceConstants;
import io.datalakehouse.common.JsonUtils;
import io.datalakehouse.utils.ConnectorHelper;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.utils.CsvDataBuffer;
import io.datalakehouse.utils.MD5Helper;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FreshServices Connector Implementation
 *
 * Handles FreshServices API responses in the following format:
 * { "entity_name": [ {...}, {...} ] }
 *
 * Example: { "contract_types": [{"id": 1, "name": "Lease"}, {"id": 2, "name": "Maintenance"}] }
 */
public class FreshServicesConnector extends DLHIngest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Thread-safe accumulator for CSV data across multiple parent IDs
    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferConcurrentHashMap = new ConcurrentHashMap<>();

    // Last sync date for filtering non-delta entities
    private final Instant lastSyncDate;

    public FreshServicesConnector(ConnectionType connectionType, DLHIngestConfig config, String outputPath) {
        this(connectionType, config, outputPath, null);
    }

    public FreshServicesConnector(ConnectionType connectionType, DLHIngestConfig config, String outputPath, Instant lastSyncDate) {
        super(connectionType, config, outputPath);
        this.lastSyncDate = lastSyncDate;
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        return processData(entity, stream, headers, null, null);
    }

    @Override
    protected List<String> processData(
            String entity, InputStream stream, List<String> headers, String parentPlaceholderKey, String parentId
    ) throws IOException {

        downloadHelper.logStartHistory(entity,
                CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name());

        try {
            // Check if this is part of multi-parent processing
            boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);

            if (isMultiParentProcessing) {
                // Accumulate data across parents, will be flushed later
                return processJsonStreamWithAccumulation(entity, stream, headers,
                    parentPlaceholderKey, parentId);
            } else {
                // Single entity processing - log and write immediately

                if (stream == null) {
                    return writeEmptyFile(entity, headers);
                }
                return processJsonStream(entity, stream, headers, parentPlaceholderKey, parentId);
            }

        } catch (IOException e) {
            System.err.println("Error processing entity " + entity + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Processes JSON stream and accumulates data across multiple parent IDs.
     * CSV will be written when all parents are processed.
     */
    private List<String> processJsonStreamWithAccumulation(String entity,
                                                            InputStream stream, List<String> headers,
                                                            String parentPlaceholderKey, String parentId) throws IOException {
        List<String> entityIds = new ArrayList<>();

        // Get or create accumulator for this entity
        CsvDataBuffer accumulator = csvDataBufferConcurrentHashMap.computeIfAbsent(
                entity, k -> new CsvDataBuffer(headers.toArray(new String[0]),
                        config.getCsvRowLimit(), entity, config.getConnectorType()));

        JsonFactory factory = MAPPER.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = MAPPER.readTree(parser);
            if (rootNode.isArray() && rootNode.size() == 1) {
                rootNode = rootNode.get(0);
            }
            JsonNode dataArray = extractDataArray(rootNode, entity);

            // Process and accumulate data with timestamp filtering
            for (JsonNode record : dataArray) {
//                Note: Delta sync based on timestamp fields which we are not using so commenting this part
//                // Apply timestamp filtering if lastSyncDate is set
//                if (!shouldIncludeRecord(record, headers)) {
//                    continue; // Skip this record
//                }

                List<String> rowValues = ConnectorHelper.mapValues(record, headers);

                // Inject parent ID
                if (parentPlaceholderKey != null && parentId != null) {
                    int idx = headers.indexOf(parentPlaceholderKey.toUpperCase());
                    if (idx >= 0 && idx < rowValues.size()) {
                        rowValues.set(idx, parentId);
                    }
                }

                String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, FreshServiceConstants.ID_HEADERS);
                if (idValue != null) {
                    entityIds.add(idValue);
                }

                // Add to accumulator
                accumulator.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
            }
        }

        return entityIds;
    }

    /**
     * Processes JSON stream and extracts data based on FreshServices schema
     */
    private List<String> processJsonStream(String entity,
                                           InputStream stream, List<String> headers,
                                           String parentPlaceholderKey, String parentId) throws IOException {
        List<String> entityIds = new ArrayList<>();
        List<String[]> csvChunk = new ArrayList<>();
        csvChunk.add(headers.toArray(new String[0]));

        JsonFactory factory = MAPPER.getFactory();
        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = MAPPER.readTree(parser);

            // Extract data array using configured response key
            JsonNode dataArray = extractDataArray(rootNode, entity);

            int lineCount = processDataArray(dataArray, csvChunk, headers, entityIds, entity,
                parentPlaceholderKey, parentId);

            // Flush remaining data
            if (!csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
            }

            downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name(), lineCount);

        }

        if (entity.equals(FreshServiceConstants.FreshServiceEntityNames.AGENTS)) {
            CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.remove(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_ROLES);
            if (csvDataBuffer != null) {
                csvDataBuffer.flushRemaining(downloadHelper);
            }
            csvDataBuffer = csvDataBufferConcurrentHashMap.remove(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_GROUPS);
            if (csvDataBuffer != null) {
                csvDataBuffer.flushRemaining(downloadHelper);
            }
            csvDataBuffer = csvDataBufferConcurrentHashMap.remove(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_DEPARTMENTS);
            if (csvDataBuffer != null) {
                csvDataBuffer.flushRemaining(downloadHelper);
            }
            csvDataBuffer = csvDataBufferConcurrentHashMap.remove(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_WORKSPACES);
            if (csvDataBuffer != null) {
                csvDataBuffer.flushRemaining(downloadHelper);
            }
            csvDataBuffer = csvDataBufferConcurrentHashMap.remove(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_WORKLOAD_CONFIGS);
            if (csvDataBuffer != null) {
                csvDataBuffer.flushRemaining(downloadHelper);
            }
        }

        return entityIds;
    }

/*
* Note : This methods are for delta sync filtering based on timestamp fields which is not useful for now
* *//**
     * Checks if a record should be included based on timestamp filtering.
     * Returns true if no lastSyncDate is set (full sync) or if the record was created/updated after lastSyncDate.
     * If no timestamp headers exist in the entity, returns true (include all records).
     *//*
    private boolean shouldIncludeRecord(JsonNode record, List<String> headers) {
        if (lastSyncDate == null) {
            return true; // No filtering, include all records
        }

        // Check if entity has any timestamp fields
        boolean hasTimestampFields = false;
        for (String timestampField : FreshServiceConstants.TIMESTAMP_FIELDS) {
            if (headers.contains(timestampField)) {
                hasTimestampFields = true;
                break;
            }
        }

        // If entity has no timestamp fields, include all records
        if (!hasTimestampFields) {
            return true;
        }

        // Check UPDATED_AT and CREATED_AT fields
        for (String timestampField : FreshServiceConstants.TIMESTAMP_FIELDS) {
            if (headers.contains(timestampField)) {
                JsonNode timestampNode = record.get(timestampField.toLowerCase());
                if (timestampNode == null) {
                    timestampNode = record.get(timestampField);
                }

                if (timestampNode != null && !timestampNode.isNull()) {
                    String timestampStr = timestampNode.asText();
                    Instant recordTimestamp = parseTimestamp(timestampStr);

                    if (recordTimestamp != null && recordTimestamp.isAfter(lastSyncDate)) {
                        return true; // Record was created/updated after lastSyncDate
                    }
                }
            }
        }

        // If we have lastSyncDate and timestamp fields exist but couldn't find valid timestamps, exclude the record
        return false;
    }

    *//**
     * Parses timestamp string using multiple formats.
     *//*
    private Instant parseTimestamp(String timestampStr) {
        if (timestampStr == null || timestampStr.trim().isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : FreshServiceConstants.DATE_FORMATTERS) {
            try {
                return LocalDateTime.parse(timestampStr, formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }

        System.err.println("Warning: Could not parse timestamp: " + timestampStr);
        return null;
    }*/

    /**
     * Processes all records in the data array with timestamp filtering support
     */
    private int processDataArray(JsonNode dataArray, List<String[]> csvChunk, List<String> headers,
                                 List<String> entityIds, String entity,
                                 String parentPlaceholderKey, String parentId) throws IOException {
        int lineCount = 0;
//        int filteredCount = 0;

        for (JsonNode record : dataArray) {
//                Note: Delta sync based on timestamp fields which we are not using so commenting this part
//            // Apply timestamp filtering if lastSyncDate is set
//            if (!shouldIncludeRecord(record, headers)) {
//                filteredCount++;
//                continue; // Skip this record
//            }

            List<String> rowValues = ConnectorHelper.mapValues(record, headers);

            // Inject parent ID if parent context exists
            if (parentPlaceholderKey != null && parentId != null) {
                int idx = headers.indexOf(parentPlaceholderKey.toUpperCase());
                if (idx >= 0 && idx < rowValues.size()) {
                    rowValues.set(idx, parentId);
                }
            }

            String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, FreshServiceConstants.ID_HEADERS);

            if (idValue != null) {
                entityIds.add(idValue);
            }

            if (entity.equals(FreshServiceConstants.FreshServiceEntityNames.AGENTS)) {
                processAgentRecord(record, idValue);
            }


            csvChunk.add(rowValues.toArray(new String[0]));
            lineCount++;

            // Flush chunk when size limit reached
            if (lineCount % config.getCsvRowLimit() == 0) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                csvChunk.clear();
            }

        }

//        if (filteredCount > 0 && lastSyncDate != null) {
//            System.out.println("Filtered out " + filteredCount + " records (before " + lastSyncDate + ") for entity: " + entity);
//        }

        return lineCount;
    }

    private void processAgentRecord(JsonNode record, String agentId) throws IOException {
        if (record.has("roles")) {
            downloadHelper.logStartHistory(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_ROLES,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name());
            CsvDataBuffer agentRolesBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_ROLES,
                    k -> new CsvDataBuffer(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_ROLES,
                            config.getCsvRowLimit(),
                            FreshServiceConstants.FreshServiceEntityNames.AGENTS_ROLES,
                            config.getConnectorType()
                    )
            );
            JsonNode rolesNode = record.get("roles");
            if (rolesNode.isArray()) {
                for (JsonNode jsonNode : rolesNode) {
                    List<String> roleAgentHeaders = Arrays.asList(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_ROLES);
                    List<String> roleAgentRowValues = ConnectorHelper.mapValues(jsonNode, roleAgentHeaders);
                    // Inject agent ID
                    int idx = roleAgentHeaders.indexOf("AGENT_ID");
                    if (idx >= 0 && idx < roleAgentRowValues.size()) {
                        roleAgentRowValues.set(idx, agentId);
                    }
                    roleAgentRowValues = ConnectorHelper.setDlhHeaderValues(roleAgentHeaders, roleAgentRowValues);
                    agentRolesBuffer.addRowValues(roleAgentRowValues.toArray(new String[0]), downloadHelper);
                }
            }
        }

        if (record.has("member_of")) {
            downloadHelper.logStartHistory(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_GROUPS,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name());
            CsvDataBuffer agentGroupsBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_GROUPS,
                    k -> new CsvDataBuffer(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_GROUPS,
                            config.getCsvRowLimit(),
                            FreshServiceConstants.FreshServiceEntityNames.AGENTS_GROUPS,
                            config.getConnectorType()
                    )
            );
            JsonNode groupsNode = record.get("member_of");
            if (groupsNode.isArray()) {
                for (JsonNode jsonNode : groupsNode) {
                    List<String> groupAgentHeaders = Arrays.asList(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_GROUPS);
                    List<String> groupAgentRowValues = new ArrayList<>(
                            Collections.nCopies(groupAgentHeaders.size(), ""));
                    groupAgentRowValues.set(groupAgentHeaders.indexOf("AGENT_ID"), agentId);
                    groupAgentRowValues.set(groupAgentHeaders.indexOf("GROUP_ID"), jsonNode.asText());
                    groupAgentRowValues = ConnectorHelper.setDlhHeaderValues(groupAgentHeaders, groupAgentRowValues);
                    agentGroupsBuffer.addRowValues(groupAgentRowValues.toArray(new String[0]), downloadHelper);
                }
            }
        }

        if (record.has("department_ids")) {
            downloadHelper.logStartHistory(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_DEPARTMENTS,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name());
            CsvDataBuffer agentDepartmentsBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_DEPARTMENTS,
                    k -> new CsvDataBuffer(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_DEPARTMENTS,
                            config.getCsvRowLimit(),
                            FreshServiceConstants.FreshServiceEntityNames.AGENTS_DEPARTMENTS,
                            config.getConnectorType()
                    )
            );
            JsonNode departmentsNode = record.get("department_ids");
            if (departmentsNode.isArray()) {
                for (JsonNode jsonNode : departmentsNode) {
                    List<String> departmentAgentHeaders = Arrays.asList(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_DEPARTMENTS);
                    List<String> departmentAgentRowValues = new ArrayList<>(
                            Collections.nCopies(departmentAgentHeaders.size(), ""));
                    departmentAgentRowValues.set(departmentAgentHeaders.indexOf("AGENT_ID"), agentId);
                    departmentAgentRowValues.set(departmentAgentHeaders.indexOf("DEPARTMENT_ID"), jsonNode.asText());
                    departmentAgentRowValues = ConnectorHelper.setDlhHeaderValues(departmentAgentHeaders, departmentAgentRowValues);
                    agentDepartmentsBuffer.addRowValues(departmentAgentRowValues.toArray(new String[0]), downloadHelper);
                }
            }
        }

        if (record.has("workspace_ids")) {
            downloadHelper.logStartHistory(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_WORKSPACES,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name());
            CsvDataBuffer agentWorkspacesBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_WORKSPACES,
                    k -> new CsvDataBuffer(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_WORKSPACES,
                            config.getCsvRowLimit(),
                            FreshServiceConstants.FreshServiceEntityNames.AGENTS_WORKSPACES,
                            config.getConnectorType()
                    )
            );
            JsonNode workspacesNode = record.get("workspace_ids");
            if (workspacesNode.isArray()) {
                for (JsonNode jsonNode : workspacesNode) {
                    List<String> workspaceAgentHeaders = Arrays.asList(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_WORKSPACES);
                    List<String> workspaceAgentRowValues = new ArrayList<>(
                            Collections.nCopies(workspaceAgentHeaders.size(), ""));
                    workspaceAgentRowValues.set(workspaceAgentHeaders.indexOf("AGENT_ID"), agentId);
                    workspaceAgentRowValues.set(workspaceAgentHeaders.indexOf("WORKSPACE_ID"), jsonNode.asText());
                    workspaceAgentRowValues = ConnectorHelper.setDlhHeaderValues(workspaceAgentHeaders, workspaceAgentRowValues);
                    agentWorkspacesBuffer.addRowValues(workspaceAgentRowValues.toArray(new String[0]), downloadHelper);
                }
            }
        }

        if (record.has("workload_configs")) {
            downloadHelper.logStartHistory(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_WORKLOAD_CONFIGS,
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.FRESHSERVICE_ENTITY.name());
            CsvDataBuffer agentWorkloadConfigsBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(
                    FreshServiceConstants.FreshServiceEntityNames.AGENTS_WORKLOAD_CONFIGS,
                    k -> new CsvDataBuffer(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_WORKLOAD_CONFIGS,
                            config.getCsvRowLimit(),
                            FreshServiceConstants.FreshServiceEntityNames.AGENTS_WORKLOAD_CONFIGS,
                            config.getConnectorType()
                    )
            );
            JsonNode workloadConfigsNode = record.get("workload_configs");
            if (workloadConfigsNode.isArray()) {
                for (JsonNode jsonNode : workloadConfigsNode) {
                    List<String> agentWorkloadConfigsHeaders = Arrays.asList(
                            FreshServiceConstants.FreshServiceHeaders.AGENTS_WORKLOAD_CONFIGS);
                    List<String> roleAgentRowValues = ConnectorHelper.mapValues(jsonNode, agentWorkloadConfigsHeaders);
                    // Inject agent ID
                    int idx = agentWorkloadConfigsHeaders.indexOf("AGENT_ID");
                    if (idx >= 0 && idx < roleAgentRowValues.size()) {
                        roleAgentRowValues.set(idx, agentId);
                    }
                    roleAgentRowValues = ConnectorHelper.setDlhHeaderValues(agentWorkloadConfigsHeaders, roleAgentRowValues);
                    agentWorkloadConfigsBuffer.addRowValues(roleAgentRowValues.toArray(new String[0]), downloadHelper);
                }
            }
        }

    }

    /**
     * Extracts data array from FreshServices response.
     * Schema: { "entity_name": [...] }
     */
    private JsonNode extractDataArray(JsonNode rootNode, String entity) {
        // Get the response key for this entity
        String responseKey = FreshServiceConstants.getEntityResponseKey(entity);

        // If specific key is configured, use it
        if (responseKey != null && rootNode.has(responseKey)) {
            return rootNode.get(responseKey);
        }

        // Try common response keys
        String[] possibleKeys = FreshServiceConstants.getPossibleResponseKeys(entity);
        for (String key : possibleKeys) {
            if (rootNode.has(key)) {
                JsonNode node = rootNode.get(key);
                if (node.isArray()) {
                    return node;
                }
            }
        }

        // Fallback: return root if it's an array
        if (rootNode.isArray()) {
            return rootNode;
        }

        // Return empty array if no data found
        return MAPPER.createArrayNode();
    }

    /**
     * Writes empty CSV file with headers only
     */
    private List<String> writeEmptyFile(String normalizedEntity, List<String> headers) throws IOException {
        List<String[]> csvChunk = new ArrayList<>();
        csvChunk.add(headers.toArray(new String[0]));
        downloadHelper.writeChunkToCsv(normalizedEntity, csvChunk, 0, config.getConnectorType());
        return new ArrayList<>();
    }

    /**
     * Flushes accumulated data for an entity to CSV.
     * Should be called after all parents have been processed.
     */
    @Override
    protected void flushEntityData(String entity) throws IOException {
        CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.remove(entity);

        if (csvDataBuffer != null) {
            csvDataBuffer.flushRemaining(downloadHelper);
        }

    }
}

