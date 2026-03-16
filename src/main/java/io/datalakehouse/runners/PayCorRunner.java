package io.datalakehouse.runners;

import io.datalakehouse.common.PayCorConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.config.QueryParameterResolver;
import io.datalakehouse.connector.PayCorConnector;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.PayCorRestConnectionType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PayCorRunner extends BaseRunner {

    @Override
    public String name() {
        return PayCorConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) throws Exception {

        String accessToken = Req.required(request.getAccessToken(), "PayCor Access Token is required");
        String clientId = Req.required(request.getClientId(), "PayCor Client Id is required");
        String clientSecret = Req.required(request.getClientSecret(), "PayCor Client Secret is required");
        String refreshToken = Req.required(request.getRefreshToken(), "PayCor Refresh Token is required");
        String environment = Req.required(request.getEnvironment(), "PayCor Environment is required");
        String subscriptionKey = Req.required(request.getCustomValue(), "PayCor Subscription Key as customValue is required");

        String baseUrl = PayCorConstants.getBaseUrl(environment);

        DLHIngestConfig config = new DLHIngestConfig(PayCorConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                request.getThreadPoolSize(), PayCorConstants.excludedEntities, baseUrl, accessToken);

        PayCorRestConnectionType connection = new PayCorRestConnectionType(
                baseUrl, accessToken, refreshToken, clientId, clientSecret);

        // Set custom headers
        Map<String, String> customHeaders = Map.of(PayCorConstants.SUBSCRIPTION_KEY, subscriptionKey);

        PayCorConnector connector = new PayCorConnector(connection, config, request.getOutputPath());

        PaginationInfo paginationInfo = connection.buildPagination(PayCorConstants.PAGE_SIZE);
        Map<String, PaginationInfo> paginationByEntity = PayCorConstants.HEADERS_BY_ENTITY.keySet().stream()
                .collect(Collectors.toMap(entity -> entity, entity -> paginationInfo));

        QueryParameterResolver queryParameterResolver = new QueryParameterResolver(Map.of());
        queryParameterResolver.setEntityQueryParams(PayCorConstants.CUSTOM_QUERY_PARAM_BY_ENTITY);

        LocalDate startDate;

        if (Req.has(request.getLastSyncDate())) {
            startDate = LocalDate.ofInstant(Instant.parse(request.getLastSyncDate()), ZoneOffset.UTC);
        } else {
            startDate = Instant.now()
                    .minus(PayCorConstants.DEFAULT_HISTORICAL_DAYS, ChronoUnit.DAYS)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate();
        }

        LocalDate endDate = LocalDate.now();

        queryParameterResolver.setParamValues(Map.of(
                "startDate", startDate.toString(), "endDate", endDate.toString(),
                "periodStartDate", startDate.toString(), "periodEndDate", endDate.toString(),
                "fromCheckDate", startDate.toString(), "toCheckDate", endDate.toString(),
                "asOfDate", startDate.toString(), "untilDate", endDate.toString()));

        connector.run(
                PayCorConstants.HEADERS_BY_ENTITY, queryParameterResolver, customHeaders,
                PayCorConstants.ENTITY_API_PATH_MAP, PayCorConstants.ENTITY_DEPENDENCY_MAP, paginationByEntity);
    }
}
