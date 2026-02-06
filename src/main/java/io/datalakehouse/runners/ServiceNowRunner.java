package io.datalakehouse.runners;

import io.datalakehouse.common.ServiceNowConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.config.QueryParameterResolver;
import io.datalakehouse.connector.ServiceNowConnector;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.ServiceNowRestConnectionType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceNowRunner extends BaseRunner {

    private static final String SYS_PARAM_QUERY = "sysparm_query";

    @Override
    public String name() {
        return ServiceNowConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) throws Exception {

        String accessToken = Req.required(request.getAccessToken(), "ServiceNow accessToken is required");
        String clientId = Req.required(request.getClientId(), "ServiceNow Client Id is required");
        String clientSecret = Req.required(request.getClientSecret(), "ServiceNow Client Secret is required");
        String baseUrl = Req.required(request.getBaseUrl(), "ServiceNow Base Url is required");


        DLHIngestConfig config = new DLHIngestConfig(ServiceNowConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                request.getThreadPoolSize(), ServiceNowConstants.excludedEntities, baseUrl, accessToken);

        ServiceNowRestConnectionType connection = new ServiceNowRestConnectionType(
                baseUrl, accessToken, clientId, clientSecret);

        ServiceNowConnector connector = new ServiceNowConnector(connection, config, request.getOutputPath());

        // Use empty headers if not provided
        Map<String, String> headers = Map.of();

        // Configure pagination for all entities
        PaginationInfo pagination = connection.buildPagination(ServiceNowConstants.MAX_PAGE_SIZE);
        Map<String, PaginationInfo> paginationByEntity = new HashMap<>();

        ServiceNowConstants.HEADERS_BY_ENTITY.keySet().forEach(entity ->
                paginationByEntity.put(entity, pagination));

        if (Boolean.TRUE.equals(request.getIsHistorical())) {
            // Historical sync - fetch all data or from lastSyncDate if provided
            QueryParameterResolver queryParameterResolver;

            if (Req.has(request.getLastSyncDate())) {
                Date fromDate = Date.from(Instant.parse(request.getLastSyncDate()));
                // Use sysparm_query to filter by sys_created_on
                queryParameterResolver = new QueryParameterResolver(
                        Map.of(SYS_PARAM_QUERY, ServiceNowConstants.SYS_CREATED_ON + ">=" +
                                fromDate.toInstant().toString(),"sysparm_exclude_reference_link", "true"));
            } else {
                Instant fromDate = Instant.now().minus(ServiceNowConstants.DEFAULT_HISTORICAL_DAYS, ChronoUnit.DAYS);
                // Use sysparm_query to filter by sys_created_on
                queryParameterResolver = new QueryParameterResolver(
                        Map.of(SYS_PARAM_QUERY, ServiceNowConstants.SYS_CREATED_ON + ">=" + fromDate.toString()
                        ,"sysparm_exclude_reference_link", "true"));
            }

            connector.run(ServiceNowConstants.HEADERS_BY_ENTITY, queryParameterResolver, headers,
                    ServiceNowConstants.ENTITY_API_PATH_MAP, ServiceNowConstants.ENTITY_DEPENDENCY_MAP,
                    paginationByEntity);

        } else {
            // Delta sync - fetch only updated records since lastSyncDate
            Date startDate = Date.from(Instant.parse(request.getLastSyncDate()));

            // Use sysparm_query to filter by sys_updated_on for delta sync
            QueryParameterResolver queryParameterResolver = new QueryParameterResolver(
                    Map.of(SYS_PARAM_QUERY, ServiceNowConstants.SYS_UPDATED_ON + ">" +
                            startDate.toInstant().toString(),"sysparm_exclude_reference_link", "true"));

            connector.run(ServiceNowConstants.DELTA_HEADERS_BY_ENTITY, queryParameterResolver, headers,
                    ServiceNowConstants.ENTITY_API_PATH_MAP, ServiceNowConstants.ENTITY_DEPENDENCY_MAP,
                    paginationByEntity);
        }
    }
}
