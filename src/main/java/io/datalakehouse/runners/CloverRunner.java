package io.datalakehouse.runners;

import io.datalakehouse.common.CloverConstants;
import io.datalakehouse.common.ServiceNowConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.config.QueryParameterResolver;
import io.datalakehouse.connector.CloverConnector;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.CloverRestConnectionType;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CloverRunner extends BaseRunner {

    @Override
    public String name() {
        return CloverConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) throws Exception {

        String accessToken = Req.required(request.getAccessToken(), "Clover Access Token is required");
        String clientId = Req.required(request.getClientId(), "Clover Client Id is required");
        String clientSecret = Req.required(request.getClientSecret(), "Clover Client Secret is required");
        String refreshToken = Req.required(request.getRefreshToken(), "Clover Refresh Token is required");
        String merchantId = Req.required(request.getCustomValue(), "Clover Merchant Id is required");
        String environment = Req.required(request.getEnvironment(), "Clover Environment is required");

        // Get environment-based baseUrl
        String baseUrl = CloverConstants.getCloverBaseUrl(environment);

        DLHIngestConfig config = new DLHIngestConfig(
                CloverConstants.CONNECTOR_NAME,
                request.getCsvRowLimit(),
                request.getThreadPoolSize(),
                CloverConstants.excludedEntities,
                baseUrl,
                accessToken
        );

        CloverRestConnectionType connection = new CloverRestConnectionType(baseUrl, accessToken,
                clientId, clientSecret, refreshToken);

        // Create connector
        CloverConnector connector = new CloverConnector(connection, config, request.getOutputPath());

        // Build pagination info
        PaginationInfo paginationInfo = connection.buildPagination(CloverConstants.DEFAULT_PAGE_SIZE);

        // Create pagination by entity map ONLY for parent entities
        Map<String, PaginationInfo> paginationByEntity = CloverConstants.HEADERS_BY_ENTITY.keySet().stream()
                .filter(entity -> !isChildEntity(entity))
                .collect(Collectors.toMap(entity -> entity, entity -> paginationInfo));

        Map<String, String> headers = Map.of();

        if (Boolean.TRUE.equals(request.getIsHistorical())) {
            QueryParameterResolver queryParameterResolver = new QueryParameterResolver(Map.of());
            queryParameterResolver.setEntityQueryParams(CloverConstants.CUSTOM_QUERY_PARAM_BY_ENTITY);
            Instant fromDate;
            if (Req.has(request.getLastSyncDate())) {
                fromDate = Instant.parse(request.getLastSyncDate());
                Map<String, String> paramValues = Map.of(
                        "filter=createdTime>", fromDate.toString(),
                        "filter=in_time>", fromDate.toString(),
                        "filter=clientCreatedTime>", fromDate.toString());
                queryParameterResolver.setParamValues(paramValues);
            } else {
                fromDate = Instant.now().minus(ServiceNowConstants.DEFAULT_HISTORICAL_DAYS, ChronoUnit.DAYS);
                Map<String, String> paramValues = Map.of(
                        "filter=createdTime>", fromDate.toString(),
                        "filter=in_time>", fromDate.toString(),
                        "filter=clientCreatedTime>", fromDate.toString());
                queryParameterResolver.setParamValues(paramValues);
            }
            connector.run(CloverConstants.HEADERS_BY_ENTITY, queryParameterResolver, headers,
                    CloverConstants.getEntityApiPathMap(merchantId), CloverConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);
        } else {
            Instant startDate = Instant.parse(request.getLastSyncDate());
            QueryParameterResolver queryParameterResolver = new QueryParameterResolver(Map.of(CloverConstants.DELTA_HEADER, startDate.toString()));
            connector.run(CloverConstants.DELTA_HEADERS_BY_ENTITY, queryParameterResolver, headers,
                    CloverConstants.getEntityApiPathMap(merchantId), CloverConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);
            connector.run(CloverConstants.NON_DELTA_HEADERS_BY_ENTITY, new QueryParameterResolver(Map.of()), headers,
                    CloverConstants.getEntityApiPathMap(merchantId), CloverConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);
        }
    }

    private boolean isChildEntity(String entity) {
        return CloverConstants.ENTITY_DEPENDENCY_MAP.values().stream()
                .anyMatch(childList -> childList.contains(entity));
    }
}
