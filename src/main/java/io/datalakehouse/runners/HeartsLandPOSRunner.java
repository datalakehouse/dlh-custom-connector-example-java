package io.datalakehouse.runners;

import io.datalakehouse.common.HeartsLandPosConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.config.QueryParameterResolver;
import io.datalakehouse.connector.HeartsLandPOSConnector;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.HeartsLandPOSConnectionType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HeartsLandPOSRunner extends BaseRunner {

    @Override
    public String name() {
        return HeartsLandPosConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) throws Exception {

        String accessToken = Req.required(request.getAccessToken(), "Heartland POS Access Token is required");
        String environment = Req.required(request.getEnvironment(), "Heartland POS Environment is required");

        String baseUrl = HeartsLandPosConstants.getHeartsLandPOSBaseUrl(environment);

        DLHIngestConfig config = new DLHIngestConfig(
                HeartsLandPosConstants.CONNECTOR_NAME, request.getCsvRowLimit(), request.getThreadPoolSize(),
                HeartsLandPosConstants.excludedEntities, baseUrl, accessToken);

        HeartsLandPOSConnectionType connection = new HeartsLandPOSConnectionType(baseUrl, accessToken);
        HeartsLandPOSConnector connector = new HeartsLandPOSConnector(connection, config, request.getOutputPath());

        // Set custom headers
        Map<String, String> customHeaders = Map.of(
                HeartsLandPosConstants.X_GP_VERSION_HEADER, HeartsLandPosConstants.X_GP_VERSION_HEADER_VALUE,
                "Accept", "application/json"
        );

        // Build pagination configuration
        PaginationInfo pagination = connection.buildPagination(HeartsLandPosConstants.MAX_PAGE_SIZE);

        Map<String, PaginationInfo> paginationByEntity = HeartsLandPosConstants.PAGINATION_ENTITIES.stream()
                .collect(Collectors.toMap(entityName -> entityName, entityName -> pagination));

        QueryParameterResolver queryParameterResolver = new QueryParameterResolver(Map.of());

        Map<String, String[]> headersToUse;
        Instant fromDate;
        if (Boolean.TRUE.equals(request.getIsHistorical())) {

            if (Req.has(request.getLastSyncDate())) {
                fromDate = Instant.parse(request.getLastSyncDate());
            } else {
                fromDate = Instant.now().minus(HeartsLandPosConstants.DEFAULT_HISTORICAL_DAYS, ChronoUnit.DAYS);
            }

            queryParameterResolver.setEntityQueryParams(HeartsLandPosConstants.CUSTOM_QUERY_PARAM_BY_ENTITY);
            queryParameterResolver.setParamValues(Map.of(HeartsLandPosConstants.HISTORICAL_SYNC_QUERY_PARAM,
                    fromDate.atZone(ZoneOffset.UTC).toLocalDate().toString(),
                    "from_stage_time_created", fromDate.atZone(ZoneOffset.UTC).toLocalDate().toString()));

            headersToUse = HeartsLandPosConstants.HEADERS_BY_ENTITY;
        } else {
            // For delta sync, use only delta-enabled entities
            Instant historicalDate =
                    Instant.now().minus(HeartsLandPosConstants.DEFAULT_HISTORICAL_DAYS, ChronoUnit.DAYS);
            headersToUse = HeartsLandPosConstants.HEADERS_BY_ENTITY;
            fromDate = Instant.parse(request.getLastSyncDate());
            queryParameterResolver.setEntityQueryParams(HeartsLandPosConstants.DELTA_QUERY_PARAMS_BY_ENTITY);
            queryParameterResolver.setParamValues(Map.of(HeartsLandPosConstants.HISTORICAL_SYNC_QUERY_PARAM,
                    historicalDate.atZone(ZoneOffset.UTC).toLocalDate().toString(),
                    "from_stage_time_created", historicalDate.atZone(ZoneOffset.UTC).toLocalDate().toString(),
                    "from_time_last_updated", fromDate.atZone(ZoneOffset.UTC).toLocalDate().toString()));
        }

        // Run the connector
        connector.run(headersToUse, queryParameterResolver, customHeaders, HeartsLandPosConstants.ENTITY_API_PATH_MAP,
                HeartsLandPosConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);
    }
}
