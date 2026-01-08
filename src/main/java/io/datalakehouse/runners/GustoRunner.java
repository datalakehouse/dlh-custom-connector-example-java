package io.datalakehouse.runners;

import io.datalakehouse.common.GustoConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connector.GustoConnector;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.GustoRestConnectionType;
import io.datalakehouse.service.GustoService;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * @author Nisarg Raval
 */
@Component
public class GustoRunner extends BaseRunner {
    @Override
    public String name() {
        return GustoConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) throws Exception {
        Req.required(request.getAccessToken(), "Gusto accessToken is required");

        // Getting companyId which is required for api paths
        String companyId = GustoService.getCompanyId(request.getAccessToken(), request.getEnvironment());

        // config
        String baseUrl = GustoConstants.getGustoBaseUrl(request.getEnvironment());
        DLHIngestConfig config = new DLHIngestConfig(GustoConstants.CONNECTOR_NAME, request.getCsvRowLimit(), request.getThreadPoolSize(),
                GustoConstants.excludedEntities, baseUrl, request.getAccessToken());

        GustoRestConnectionType conn = buildConnection(request, baseUrl);
        DLHIngest connector = new GustoConnector(conn, config, request.getOutputPath());

        // Run logic (gusto has special delta split)
        Map<String, String> headers = Map.of(GustoConstants.API_VERSION_HEADER, GustoConstants.API_VERSION);

        if (Req.has(request.getLastSyncDate())) {
            LocalDate startDate = LocalDate.parse(request.getLastSyncDate());

            connector.run(GustoConstants.DELTA_HEADERS_BY_ENTITY, Map.of("start_date", startDate.toString()), headers,
                    GustoConstants.getEntityApiPathMap(companyId), GustoConstants.ENTITY_DEPENDENCY_MAP);

            connector.run(GustoConstants.NON_DELTA_HEADERS_BY_ENTITY, Map.of(), headers,
                    GustoConstants.getEntityApiPathMap(companyId), GustoConstants.ENTITY_DEPENDENCY_MAP);

        } else {
            connector.run(GustoConstants.HEADERS_BY_ENTITY, Map.of(), headers,
                    GustoConstants.getEntityApiPathMap(companyId), GustoConstants.ENTITY_DEPENDENCY_MAP);
        }
    }

    private static GustoRestConnectionType buildConnection(IngestConnectorRunRequest r, String baseUrl) {
        if (Req.has(r.getRefreshToken())) {
            return new GustoRestConnectionType(baseUrl, r.getAccessToken(), r.getClientId(), r.getClientSecret(),
                    r.getRefreshToken(), r.getRedirectUri(), r.getAuthorizationCode());
        }
        return new GustoRestConnectionType(baseUrl, r.getAccessToken());
    }
}
