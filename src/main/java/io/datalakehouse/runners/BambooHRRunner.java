package io.datalakehouse.runners;

import io.datalakehouse.common.BambooHRConstants;
import io.datalakehouse.common.ServiceNowConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.config.QueryParameterResolver;
import io.datalakehouse.connector.BambooHRConnector;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.BambooHRRestConnectionType;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BambooHRRunner extends BaseRunner {

    @Override
    public String name() {
        return BambooHRConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) throws Exception {

        String clientId = Req.required(request.getClientId(), "BambooHR Client Id is required");
        String clientSecret = Req.required(request.getClientSecret(), "BambooHR Client Secret is required");
        String baseUrl = Req.required(request.getBaseUrl(), "BambooHR Base Url is required");
        String refreshToken = Req.required(request.getRefreshToken(), "BambooHR Refresh Token is required");
        String accessToken = Req.required(request.getAccessToken(), "BambooHR Access Token is required");

        DLHIngestConfig config = new DLHIngestConfig(BambooHRConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                request.getThreadPoolSize(), BambooHRConstants.excludedEntities, baseUrl, accessToken);

        BambooHRRestConnectionType connection =
                new BambooHRRestConnectionType(baseUrl, clientId, clientSecret, refreshToken, accessToken);

        BambooHRConnector connector = new BambooHRConnector(connection, config, request.getOutputPath());

        // Use empty headers if not provided
        Map<String, String> headers = Map.of("AcceptHeaderParameter", "application/json");

        PaginationInfo pagination = connection.buildPagination(BambooHRConstants.MAX_PAGE_SIZE);
        Map<String, PaginationInfo> paginationByEntity = BambooHRConstants.PAGINATION_HEADERS.stream()
                .collect(Collectors.toMap(header -> header, header -> pagination));

        QueryParameterResolver queryParameterResolver = new QueryParameterResolver(Map.of());
        queryParameterResolver.setEntityQueryParams(BambooHRConstants.CUSTOM_QUERY_PARAM_BY_ENTITY);
        Instant toDate = Instant.now().plus(15, ChronoUnit.DAYS);

        if (Boolean.TRUE.equals(request.getIsHistorical())) {

            Instant fromDate;
            if (Req.has(request.getLastSyncDate())) {
                fromDate = Instant.parse(request.getLastSyncDate());
                Map<String, String> paramValues = Map.of(
                        "calendarYear", String.valueOf(fromDate.atZone(ZoneOffset.UTC).getYear()),
                        "start", fromDate.atZone(ZoneOffset.UTC).toLocalDate().toString(),
                        "end", toDate.atZone(ZoneOffset.UTC).toLocalDate().toString());
                queryParameterResolver.setParamValues(paramValues);
            } else {
                fromDate = Instant.now().minus(ServiceNowConstants.DEFAULT_HISTORICAL_DAYS, ChronoUnit.DAYS);
                Map<String, String> paramValues = Map.of(
                        "calendarYear", String.valueOf(fromDate.atZone(ZoneOffset.UTC).getYear()),
                        "start", fromDate.atZone(ZoneOffset.UTC).toLocalDate().toString(),
                        "end", toDate.atZone(ZoneOffset.UTC).toLocalDate().toString());
                queryParameterResolver.setParamValues(paramValues);
            }
            connector.run(BambooHRConstants.HEADERS_BY_ENTITY, queryParameterResolver, headers,
                    BambooHRConstants.ENTITY_API_PATH_MAP, BambooHRConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);

        } else {
            Instant startDate = Instant.parse(request.getLastSyncDate());
            Map<String, String> paramValues = Map.of(
                    "calendarYear", String.valueOf(startDate.atZone(ZoneOffset.UTC).getYear()),
                    "start", startDate.atZone(ZoneOffset.UTC).toLocalDate().toString(),
                    "end", toDate.atZone(ZoneOffset.UTC).toLocalDate().toString());
            queryParameterResolver.setParamValues(paramValues);
        }
        connector.run(BambooHRConstants.HEADERS_BY_ENTITY, queryParameterResolver, headers,
                BambooHRConstants.ENTITY_API_PATH_MAP, BambooHRConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);
    }
}
