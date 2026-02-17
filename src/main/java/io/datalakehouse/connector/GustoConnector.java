package io.datalakehouse.connector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CoreCustomConstants;
import io.datalakehouse.common.GustoConstants;
import io.datalakehouse.utils.ConnectorHelper;
import io.datalakehouse.utils.CsvDataBuffer;
import io.datalakehouse.utils.MD5Helper;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connectors.core.ConnectionType;
import io.datalakehouse.connectors.core.DLHIngest;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GustoConnector extends DLHIngest {

    private final Map<String, List<String[]>> mappingTableData = new HashMap<>();
    private final Map<String, Integer> mappingTableLineCounts = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    // Thread-safe accumulator for CSV data across multiple parent IDs
    private final ConcurrentHashMap<String, CsvDataBuffer> csvDataBufferConcurrentHashMap = new ConcurrentHashMap<>();

    public GustoConnector(ConnectionType connectionType, DLHIngestConfig config, String outputPath) {
        super(connectionType, config, outputPath);
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers) throws IOException {
        try {
            return processData(entity, stream, headers, null, null);
        } catch (IOException e) {
            downloadHelper.logWarning(entity, e.getCause().getLocalizedMessage(),
                    CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());
            throw e;
        }
    }

    @Override
    protected List<String> processData(String entity, InputStream stream, List<String> headers,
                                       String parentPlaceholderKey, String parentId) throws IOException {

        // Special handling for EARNING_TYPES entity
        if (GustoConstants.GustoEntityNames.EARNING_TYPES.equals(entity)) {
            return processEarningTypesData(entity, stream, headers);
        }

        // Special handling for PAY_SCHEDULES_ASSIGNMENTS entity
        if (GustoConstants.GustoEntityNames.PAY_SCHEDULES_ASSIGNMENTS.equals(entity)) {
            return processPayScheduleAssignmentsData(stream);
        }

        Instant startTime = Instant.now();
        downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());

        int lineCount = 0;
        List<String[]> csvChunk = new ArrayList<>();
        List<String> entityIds = new ArrayList<>();

        // Response doesn't return any data
        if (stream == null) {
            return entityIds;
        }

        JsonFactory factory = mapper.getFactory();

        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = mapper.readTree(parser);

            // EMPLOYEE_CUSTOM_FIELDS comes as single-object array
            if (GustoConstants.GustoEntityNames.EMPLOYEE_CUSTOM_FIELDS.equals(entity) &&
                    rootNode.isArray() && rootNode.size() == 1 && rootNode.get(0).isObject()) {
                rootNode = rootNode.get(0); // Extract the single object from the array
            }

            if (GustoConstants.GustoEntityNames.CONTRACTOR_PAYMENTS.equals(entity)) {
                rootNode = resolveDataArray(entity, rootNode);
                rootNode = rootNode.get(0);
                System.out.println("CONTRACTOR_PAYMENTS rootNode after first resolution: " + rootNode.toString());
                System.out.println(rootNode.has(GustoConstants.CONTRACTOR_PAYMENTS_DATA_KEY));
                if (rootNode.has(GustoConstants.CONTRACTOR_PAYMENTS_DATA_KEY) &&
                        rootNode.get(GustoConstants.CONTRACTOR_PAYMENTS_DATA_KEY).isArray()) {
                    rootNode = rootNode.get(GustoConstants.CONTRACTOR_PAYMENTS_DATA_KEY);
                }
            }

            JsonNode dataNode = resolveDataArray(entity, rootNode);

            // Check if this is part of multi-parent processing
            boolean isMultiParentProcessing = (parentPlaceholderKey != null && parentId != null);
            CsvDataBuffer csvDataBuffer = null;
            if (isMultiParentProcessing) {
                // Initialize accumulator for this entity if not present
                csvDataBuffer = csvDataBufferConcurrentHashMap.computeIfAbsent(
                        entity,
                        k -> new CsvDataBuffer(headers.toArray(new String[0]), config.getCsvRowLimit(), entity,
                                config.getConnectorType())
                );
            } else {
                // Add header row for single-parent processing
                csvChunk.add(headers.toArray(new String[0]));
                if (dataNode.isEmpty()) {
                    downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
                    downloadHelper.addBridgeStats(Map.of(entity, 0), startTime,
                            CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());
                    downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name(), 0);
                    return entityIds;
                }
            }

            for (JsonNode recordNode : dataNode) {
                Iterable<JsonNode> nodes = recordNode.isArray() ? recordNode : List.of(recordNode);
                for (JsonNode node : nodes) {

                    List<String> rowValues = ConnectorHelper.mapValues(node, headers);
                    String idValue = ConnectorHelper.getValueForHeader(headers, rowValues, GustoConstants.ID_HEADERS);
                    if (idValue != null) entityIds.add(idValue);

                    if (isMultiParentProcessing) {
                        csvDataBuffer.addRowValues(rowValues.toArray(new String[0]), downloadHelper);
                    } else {
                        csvChunk.add(rowValues.toArray(new String[0]));
                        lineCount++;
                        if (lineCount % config.getCsvRowLimit() == 0) {
                            downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                            csvChunk.clear(); // DO NOT re-add header
                        }
                    }
                }
            }

            if (!csvChunk.isEmpty() && !isMultiParentProcessing) {
                downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
            }

            // Only call addBridgeStats for non-multi-parent processing (single entity processing)
            // For paginated/multi-parent processing, stats are handled in flushEntityData
            if (!isMultiParentProcessing) {
                downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime, CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());
            }
            downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name(), lineCount);
            processMappingTables(entity, rootNode);
        }
        return entityIds;
    }

    /**
     * Flushes accumulated data for an entity to CSV.
     * Should be called after all parents have been processed.
     */
    @Override
    protected void flushEntityData(String entity) throws IOException {
        CsvDataBuffer csvDataBuffer = csvDataBufferConcurrentHashMap.remove(entity);
        if (csvDataBuffer != null) {
            downloadHelper.addBridgeStats(Map.of(entity, csvDataBuffer.getTotalRecordsCount()),
                    csvDataBuffer.getEntityProcessingStartTime(), CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());
            csvDataBuffer.flushRemaining(downloadHelper);
        }
        flushAllMappingTables();
    }

    /**
     * Processing for EARNING_TYPES entity which has "default" and "custom" arrays
     */
    private List<String> processEarningTypesData(String entity, InputStream stream, List<String> headers)
            throws IOException {

        downloadHelper.logStartHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());
        Instant startTime = Instant.now();

        int lineCount = 0;
        List<String[]> csvChunk = new ArrayList<>();
        List<String> entityIds = new ArrayList<>();

        // Add header row
        csvChunk.add(headers.toArray(new String[0]));

        if (stream == null) {
            downloadHelper.writeChunkToCsv(entity, csvChunk, 0, config.getConnectorType());
            return entityIds;
        }

        JsonFactory factory = mapper.getFactory();

        try (JsonParser parser = factory.createParser(stream)) {
            JsonNode rootNode = mapper.readTree(parser);

            String[] earningTypes = {"default", "custom"};
            for (String earningType : earningTypes) {
                if (rootNode.has(earningType) && rootNode.get(earningType).isArray()) {
                    JsonNode array = rootNode.get(earningType);
                    for (JsonNode record : array) {
                        List<String> rowValues = new ArrayList<>();
                        List<String> headersCopy = new ArrayList<>(headers);
                        headersCopy.removeFirst();
                        rowValues.add(earningType);
                        List<String> mappedValues = ConnectorHelper.mapValues(record, headersCopy, rowValues);
                        csvChunk.add(mappedValues.toArray(new String[0]));
                        lineCount++;
                        if (lineCount % config.getCsvRowLimit() == 0) {
                            downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
                            csvChunk.clear(); // DO NOT re-add header
                        }
                    }
                }
            }
        }

        // Flush any remaining rows
        if (!csvChunk.isEmpty()) {
            downloadHelper.writeChunkToCsv(entity, csvChunk, lineCount, config.getConnectorType());
        }

        downloadHelper.addBridgeStats(Map.of(entity, lineCount), startTime, CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name());
        downloadHelper.logEndHistory(entity, CoreCustomConstants.HISTORY_ENTITY_TYPE.GUSTO_ENTITY.name(),
                lineCount);
        return entityIds;
    }

    /**
     * Processing for PAY_SCHEDULES_ASSIGNMENTS entity.
     * Extracts parent data once (not persisted) and uses it to create child mapping tables.
     * Memory-efficient: no object creation, direct primitive value usage.
     */
    private List<String> processPayScheduleAssignmentsData(InputStream stream) throws IOException {

        if (stream == null) {
            return new ArrayList<>();
        }

        JsonNode rootNode = mapper.readTree(mapper.getFactory().createParser(stream));

        // Extract parent values once as primitives (not persisted to CSV)
        final String parentType = rootNode.has("type") ? rootNode.get("type").asText() : "";
        final String parentDefaultUuid = rootNode.has("default_pay_schedule_uuid")
                ? rootNode.get("default_pay_schedule_uuid").asText() : "";
        final String parentHourlyUuid = rootNode.has("hourly_pay_schedule_uuid")
                && !rootNode.get("hourly_pay_schedule_uuid").isNull()
                ? rootNode.get("hourly_pay_schedule_uuid").asText() : "";
        final String parentSalariedUuid = rootNode.has("salaried_pay_schedule_uuid")
                && !rootNode.get("salaried_pay_schedule_uuid").isNull()
                ? rootNode.get("salaried_pay_schedule_uuid").asText() : "";
        final String timestamp = Instant.now().toString();

        // Process employees mapping table
        processChildTable(rootNode, "employees", "employee_uuid", "EMPLOYEE_UUID",
                GustoConstants.GustoEntityNames.EMPLOYEE_PAY_SCHEDULE_ASSIGNMENTS,
                GustoConstants.GustoHeaders.EMPLOYEE_PAY_SCHEDULES_ASSIGNMENTS,
                parentType, parentDefaultUuid, parentHourlyUuid, parentSalariedUuid, timestamp);

        // Process departments mapping table
        processChildTable(rootNode, "departments", "department_uuid", "DEPARTMENT_UUID",
                GustoConstants.GustoEntityNames.DEPARTMENT_PAY_SCHEDULE_ASSIGNMENTS,
                GustoConstants.GustoHeaders.DEPARTMENT_PAY_SCHEDULES_ASSIGNMENTS,
                parentType, parentDefaultUuid, parentHourlyUuid, parentSalariedUuid, timestamp);

        return new ArrayList<>();
    }

    /**
     * Process a single mapping table without creating intermediate objects.
     * All parent values passed as primitives for memory efficiency.
     */
    private void processChildTable(JsonNode rootNode, String jsonArrayName, String jsonUuidField,
                                   String csvUuidField, String entityName, String[] headers,
                                   String parentType, String parentDefaultUuid, String parentHourlyUuid,
                                   String parentSalariedUuid, String timestamp) throws IOException {

        JsonNode arrayNode = rootNode.get(jsonArrayName);
        List<String[]> csvChunk = new ArrayList<>();
        csvChunk.add(headers);
        int lineCount = 0;

        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode itemNode : arrayNode) {
                String entityUuid = itemNode.has(jsonUuidField) ? itemNode.get(jsonUuidField).asText() : "";
                String payScheduleUuid = itemNode.has("pay_schedule_uuid")
                        ? itemNode.get("pay_schedule_uuid").asText() : "";

                if (entityUuid.isEmpty() || payScheduleUuid.isEmpty()) continue;

                String[] row = new String[headers.length];
                List<String> md5Values = new ArrayList<>();

                for (int i = 0; i < headers.length; i++) {
                    String header = headers[i];
                    String value = switch (header) {
                        case "PAY_SCHEDULES_ASSIGNMENTS_TYPE" -> parentType;
                        case "DEFAULT_PAY_SCHEDULE_UUID" -> parentDefaultUuid;
                        case "HOURLY_PAY_SCHEDULE_UUID" -> parentHourlyUuid;
                        case "SALARIED_PAY_SCHEDULE_UUID" -> parentSalariedUuid;
                        case "PAY_SCHEDULE_UUID" -> payScheduleUuid;
                        case "__DLH_IS_DELETED" -> "false";
                        case "__DLH_IS_ACTIVE" -> "true";
                        case "__ROW_MD5" -> "";
                        default -> CoreCustomConstants.DLH_TS_COLUMNS.contains(header) ? timestamp
                                : header.equals(csvUuidField) ? entityUuid : "";
                    };

                    row[i] = value;
                    if (!header.startsWith("__")) md5Values.add(value);
                }

                // Set MD5
                for (int i = 0; i < headers.length; i++) {
                    if ("__ROW_MD5".equals(headers[i])) {
                        row[i] = MD5Helper.getMD5FromArguments(md5Values.toArray(new String[0]));
                        break;
                    }
                }

                csvChunk.add(row);
                lineCount++;

                if (lineCount % config.getCsvRowLimit() == 0) {
                    downloadHelper.writeChunkToCsv(entityName, csvChunk, lineCount, config.getConnectorType());
                    csvChunk.clear();
                    csvChunk.add(headers);
                }
            }
        }

        // Flush remaining rows or write empty file
        if (csvChunk.size() > 1 || lineCount == 0) {
            downloadHelper.writeChunkToCsv(entityName, csvChunk, lineCount, config.getConnectorType());
        }
    }

    private void processMappingTables(String entity, JsonNode rootNode) throws IOException {

        List<String> mappingEntities = GustoConstants.MAPPING_TABLES_MAP.get(entity);
        if (mappingEntities == null || mappingEntities.isEmpty()) {
            return;
        }

        JsonNode dataNode = resolveDataArray(entity, rootNode);

        for (JsonNode recordNode : dataNode) {
            if (recordNode.isArray()) {
                for (JsonNode inner : recordNode) {
                    extractMappingData(inner, mappingEntities);
                }
            } else {
                extractMappingData(recordNode, mappingEntities);
            }
        }

        flushAllMappingTables();
    }

    private void extractMappingData(JsonNode recordNode, List<String> mappingEntities) throws IOException {

        String parentId = extractIdFromNode(recordNode);
        if (parentId == null) {
            return;
        }

        for (String mappingEntity : mappingEntities) {

            String[] headers = GustoConstants.CHILD_TABLE_HEADERS_BY_ENTITY.get(mappingEntity);
            if (headers == null) {
                continue;
            }

            initializeMappingTable(mappingEntity, headers);
            String fieldName = determineMappingFieldName(mappingEntity);

            if (!recordNode.has(fieldName)
                    || !recordNode.get(fieldName).isArray()) {
                continue;
            }

            for (JsonNode childNode : recordNode.get(fieldName)) {
                String childId = extractIdFromNode(childNode);
                if (childId != null) {
                    addMappingRow(
                            mappingEntity, headers, parentId, childId
                    );
                }
            }
        }
    }

    private void addMappingRow(String mappingEntity, String[] headers, String parentId, String childId)
            throws IOException {

        Instant now = Instant.now();
        String[] row = new String[headers.length];

        int parentIdx = -1;
        int childIdx = -1;

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].endsWith("_UUID") && !headers[i].startsWith("__")) {
                if (parentIdx == -1) parentIdx = i;
                else childIdx = i;
            }
        }

        // Build row and collect values for MD5
        List<String> md5Values = new ArrayList<>();

        for (int i = 0; i < headers.length; i++) {
            String value;
            if (i == parentIdx) value = parentId;
            else if (i == childIdx) value = childId;
            else if ("__DLH_IS_DELETED".equals(headers[i])) value = "false";
            else if ("__DLH_IS_ACTIVE".equals(headers[i])) value = "true";
            else if ("__ROW_MD5".equals(headers[i])) value = "";  // Placeholder, will be set below
            else if (CoreCustomConstants.DLH_TS_COLUMNS.contains(headers[i]))
                value = now.toString();
            else value = "";

            row[i] = value;

            // Collect non-dunder fields for MD5 calculation
            if (!headers[i].startsWith("__")) {
                md5Values.add(value);
            }
        }

        // Calculate and set MD5
        for (int i = 0; i < headers.length; i++) {
            if ("__ROW_MD5".equals(headers[i])) {
                row[i] = MD5Helper.getMD5FromArguments(md5Values.toArray(new String[0]));
                break;
            }
        }

        List<String[]> csvChunk = mappingTableData.get(mappingEntity);
        csvChunk.add(row);

        int count = mappingTableLineCounts.get(mappingEntity) + 1;
        mappingTableLineCounts.put(mappingEntity, count);

        if (count % config.getCsvRowLimit() == 0) {
            downloadHelper.writeChunkToCsv(
                    mappingEntity, csvChunk, count, config.getConnectorType()
            );
            csvChunk.clear(); // header not re-added
        }
    }

    private void initializeMappingTable(String entity, String[] headers) {
        mappingTableData.computeIfAbsent(entity, k -> {
            List<String[]> list = new ArrayList<>();
            list.add(headers);
            mappingTableLineCounts.put(entity, 0);
            return list;
        });
    }

    private String extractIdFromNode(JsonNode node) {
        for (String idHeader : GustoConstants.ID_HEADERS) {
            if (node.has(idHeader)) {
                return node.get(idHeader).asText();
            }
            String lowerCase = idHeader.toLowerCase();
            if (node.has(lowerCase)) {
                return node.get(lowerCase).asText();
            }
        }
        return null;
    }

    private String determineMappingFieldName(String mappingEntity) {
        String[] parts = mappingEntity.split("_");
        return parts.length >= 2
                ? parts[parts.length - 1].toLowerCase()
                : mappingEntity.toLowerCase();
    }

    private void flushAllMappingTables() throws IOException {

        for (Map.Entry<String, List<String[]>> e : mappingTableData.entrySet()) {

            String entity = e.getKey();
            List<String[]> csvChunk = e.getValue();
            int count = mappingTableLineCounts.getOrDefault(entity, 0);

            if (!csvChunk.isEmpty()) {
                downloadHelper.writeChunkToCsv(
                        entity, csvChunk, count, config.getConnectorType()
                );
            }
        }

        mappingTableData.clear();
        mappingTableLineCounts.clear();
    }


    private JsonNode resolveDataArray(String entity, JsonNode rootNode) {

        if (rootNode.isArray()) {
            return rootNode;
        }

        String[] keys = {"data", "items", "results", "custom_fields", StringUtils.lowerCase(entity)};
        for (String key : keys) {
            if (rootNode.has(key) && rootNode.get(key).isArray()) {
                return rootNode.get(key);
            }
        }

        return new ObjectMapper().createArrayNode().add(rootNode);
    }
}
