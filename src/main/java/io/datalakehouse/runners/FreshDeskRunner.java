package io.datalakehouse.runners;

import io.datalakehouse.common.FreshDeskConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connector.FreshDeskConnector;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.ingest.StandardSyncFlow;
import io.datalakehouse.restconnection.FreshDeskRestConnectionType;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * @author Nisarg Raval
 */
@Component
public class FreshDeskRunner extends BaseRunner {
    @Override
    public String name() {
        return FreshDeskConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) {
        String baseUrl = Req.required(request.getBaseUrl(), "FreshDesk baseUrl is required");
        String username = Req.required(request.getUsername(), "FreshDesk requires username");
        String password = Req.required(request.getPassword(), "FreshDesk requires password");

        FreshDeskRestConnectionType connection = new FreshDeskRestConnectionType(baseUrl, username, password);

        DLHIngestConfig config = new DLHIngestConfig(FreshDeskConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                request.getThreadPoolSize(), FreshDeskConstants.excludedEntities, baseUrl,
                request.getAccessToken() != null ? request.getAccessToken() : "");

        DLHIngest connector = new FreshDeskConnector(connection, config, request.getOutputPath());

        StandardSyncFlow.run(connector, request.getLastSyncDate(),
                lastSync -> Map.of("updated_since", LocalDateTime.parse(lastSync).toString()),
                FreshDeskConstants.HEADERS_BY_ENTITY, FreshDeskConstants.DELTA_HEADERS_BY_ENTITY,
                FreshDeskConstants.NON_DELTA_HEADERS_BY_ENTITY, FreshDeskConstants.getEntityApiPathMap(),
                FreshDeskConstants.ENTITY_DEPENDENCY_MAP, Map.of());
    }
}
