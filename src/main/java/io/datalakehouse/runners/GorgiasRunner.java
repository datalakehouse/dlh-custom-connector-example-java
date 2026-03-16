package io.datalakehouse.runners;

import io.datalakehouse.common.GorgiasConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.config.QueryParameterResolver;
import io.datalakehouse.connector.GorgiasConnector;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.GorgiasRestConnectionType;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GorgiasRunner extends BaseRunner {

    @Override
    public String name() {
        return GorgiasConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) throws Exception {
        String baseUrl = Req.required(request.getBaseUrl(), "Gorgias Base URL is required (e.g. https://your-domain.gorgias.com)");
        String accessToken = Req.required(request.getAccessToken(), "Gorgias Access Token is required");
        String clientId = Req.required(request.getClientId(), "Gorgias Client Id is required");
        String clientSecret = Req.required(request.getClientSecret(), "Gorgias Client Secret is required");
        String refreshToken = Req.required(request.getRefreshToken(), "Gorgias Refresh Token is required");

        DLHIngestConfig config = new DLHIngestConfig(
                GorgiasConstants.CONNECTOR_NAME,
                request.getCsvRowLimit(),
                request.getThreadPoolSize(),
                GorgiasConstants.excludedEntities,
                baseUrl,
                accessToken
        );

        GorgiasRestConnectionType connection = new GorgiasRestConnectionType(
                baseUrl, accessToken, clientId, clientSecret, refreshToken);

        GorgiasConnector connector = new GorgiasConnector(connection, config, request.getOutputPath());
        PaginationInfo paginationInfo = connection.buildPagination(100);
        Map<String, PaginationInfo> paginationByEntity = GorgiasConstants.PAGINATION_HEADERS.stream()
                .collect(Collectors.toMap(header -> header, header -> paginationInfo));

        QueryParameterResolver queryParameterResolver = new QueryParameterResolver(Map.of());

        connector.run(
                GorgiasConstants.HEADERS_BY_ENTITY,
                queryParameterResolver,
                Map.of(),
                GorgiasConstants.ENTITY_API_PATH_MAP,
                GorgiasConstants.ENTITY_DEPENDENCY_MAP,
                paginationByEntity
        );
    }

}
