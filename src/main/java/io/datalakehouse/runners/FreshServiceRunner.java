package io.datalakehouse.runners;

import io.datalakehouse.common.FreshServiceConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connector.FreshServicesConnector;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.FreshServiceRestConnectionType;
import java.time.Instant;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * @author Nisarg Raval
 */
@Component
public class FreshServiceRunner extends BaseRunner {

    @Override
    public String name() {
        return FreshServiceConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) {
        String baseUrl = Req.required(request.getBaseUrl(), "FreshService baseUrl is required");
        String accessToken = Req.required(request.getAccessToken(), "FreshService accessToken is required");

        DLHIngestConfig config = new DLHIngestConfig(FreshServiceConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                request.getThreadPoolSize(), FreshServiceConstants.excludedEntities, baseUrl, accessToken);

        FreshServiceRestConnectionType conn = new FreshServiceRestConnectionType(baseUrl, accessToken,
                request.getClientId(), request.getClientSecret(), request.getRefreshToken(), request.getRedirectUri(),
                request.getAuthorizationCode(), request.getAuthUrl());

        boolean isDelta = Req.has(request.getLastSyncDate());

        if (!isDelta) {
            FreshServicesConnector connector = new FreshServicesConnector(conn, config, request.getOutputPath());
            connector.run(FreshServiceConstants.HEADERS_BY_ENTITY, Map.of(), Map.of(),
                    FreshServiceConstants.getEntityApiPathMap(), FreshServiceConstants.ENTITY_DEPENDENCY_MAP);
            return;
        }

        Instant lastSync = Instant.parse(request.getLastSyncDate());
        Map<String, String> deltaQueryParams = Map.of("updated_since", lastSync.toString());

        FreshServicesConnector connector = new FreshServicesConnector(conn, config, request.getOutputPath(), lastSync);

        // Process delta entities with updated_since parameter
        connector.run(FreshServiceConstants.DELTA_HEADERS_BY_ENTITY, deltaQueryParams, Map.of(),
                FreshServiceConstants.getEntityApiPathMap(), Map.of());

        // Process non-delta entities without updated_since parameter
        connector.run(FreshServiceConstants.NON_DELTA_HEADERS_BY_ENTITY, Map.of(), Map.of(),
                FreshServiceConstants.getEntityApiPathMap(), FreshServiceConstants.ENTITY_DEPENDENCY_MAP);
    }
}
