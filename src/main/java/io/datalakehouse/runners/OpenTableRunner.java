package io.datalakehouse.runners;

import io.datalakehouse.common.OpenTableConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.config.QueryParameterResolver;
import io.datalakehouse.connector.OpenTableConnector;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.OpenTableRestConnectionType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class OpenTableRunner extends BaseRunner {

    private static final String SCHEDULED_TIME_FROM = "scheduled_time_from";
    private static final String START_DATE_TIME = "start_date_time";
    private static final String START_DATE = "StartDate";
    private static final String RID = "rid";

    @Override
    public String name() {
        return OpenTableConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) {

        String accessToken = Req.required(request.getAccessToken(), "OpenTable accessToken is required");
        String restaurantId = Req.required(request.getCustomValue(), "OpenTable customValue (rid) is required");

        DLHIngestConfig config = new DLHIngestConfig(OpenTableConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                request.getThreadPoolSize(), OpenTableConstants.excludedEntities, OpenTableConstants.BASE_URL, accessToken);

        OpenTableRestConnectionType connection = new OpenTableRestConnectionType(
                OpenTableConstants.BASE_URL, accessToken);

        OpenTableConnector connector = new OpenTableConnector(connection, config, request.getOutputPath());
        // Use empty headers if not provided
        Map<String, String> headers = Map.of();

        // Configure pagination for entities that support it
        PaginationInfo pagination = connection.buildPagination(1000);
        Map<String, PaginationInfo> paginationByEntity = new HashMap<>();

        OpenTableConstants.HEADERS_BY_ENTITY.keySet().forEach(entity ->
                paginationByEntity.put(entity, pagination));

        if (Boolean.TRUE.equals(request.getIsHistorical())) {
            Date fromDate;

            if (Req.has(request.getLastSyncDate())) {
                fromDate = Date.from(Instant.parse(request.getLastSyncDate()));
            } else {
                fromDate = Date.from(Instant.now().minus(OpenTableConstants.DEFAULT_HISTORICAL_DAYS, ChronoUnit.DAYS));
            }

            QueryParameterResolver queryParameterResolver = new QueryParameterResolver(Map.of());
            queryParameterResolver.setEntityQueryParams(OpenTableConstants.CUSTOM_QUERY_PARAM_BY_ENTITY);
            queryParameterResolver.setParamValues(Map.of(
                    SCHEDULED_TIME_FROM, fromDate.toInstant().toString(),
                    START_DATE_TIME, fromDate.toInstant().toString(),
                    START_DATE, fromDate.toInstant().toString(),
                    RID, restaurantId
            ));

            connector.run(OpenTableConstants.HEADERS_BY_ENTITY, queryParameterResolver, headers,
                    OpenTableConstants.getEntityApiPathMap(restaurantId),
                    OpenTableConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);

        } else {
            Date startDate = Date.from(Instant.parse(request.getLastSyncDate()));

            QueryParameterResolver queryParameterResolver = new QueryParameterResolver(
                    Map.of(OpenTableConstants.DELTA_QUERY_PARAM, startDate.toInstant().toString()));
            queryParameterResolver.setEntityQueryParams(OpenTableConstants.CUSTOM_QUERY_PARAM_BY_ENTITY);
            queryParameterResolver.setParamValues(Map.of(RID, restaurantId));

            connector.run(OpenTableConstants.DELTA_HEADERS_BY_ENTITY, queryParameterResolver,
                    headers, OpenTableConstants.getEntityApiPathMap(restaurantId),
                    OpenTableConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);

            connector.run(OpenTableConstants.NON_DELTA_HEADERS_BY_ENTITY, queryParameterResolver, headers,
                    OpenTableConstants.getEntityApiPathMap(restaurantId), OpenTableConstants.ENTITY_DEPENDENCY_MAP,
                    paginationByEntity);
        }
    }
}
