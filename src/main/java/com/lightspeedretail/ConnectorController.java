package com.lightspeedretail;

import com.lightspeedretail.common.GustoConstants;
import com.lightspeedretail.common.LightSpeedRetailXConstants;
import com.lightspeedretail.common.OnePageCRMConstants;
import com.lightspeedretail.connector.GustoConnector;
import com.lightspeedretail.connector.LightSpeedRetailXConnector;
import com.lightspeedretail.connector.OnePageCRMConnector;
import com.lightspeedretail.dto.RunConnectorRequest;
import com.lightspeedretail.restconnection.GustoRestConnectionType;
import com.lightspeedretail.restconnection.OnePageCRMRestConnectionType;
import com.lightspeedretail.service.GustoService;
import io.datalakehouse.config.Config;
import io.datalakehouse.connectors.core.Connector;
import io.datalakehouse.connectors.core.PaginationInfo;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectorController {

    @GetMapping("/callback")
    public String printRequestParams(@RequestParam Map<String, String> allParams) {
        if (allParams.isEmpty()) {
            System.out.println("No request parameters found!");
            return "No request parameters found!";
        }

        StringBuilder response = new StringBuilder("Received parameters:\n");
        allParams.forEach((key, value) -> response.append(key).append(" = ").append(value).append("\n"));
        System.out.println("Response: " + response);
        return response.toString();
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the Custom Connectors Home Page!";
    }

    @PostMapping("/runConnector")
    public String runConnector(@RequestBody RunConnectorRequest request) {
        if (null != request.getConnectorName() && request.getConnectorName()
                .equals(LightSpeedRetailXConstants.CONNECTOR_NAME)) {
            Map<String, String> queryParams = Map.of();
            Config config = new Config(LightSpeedRetailXConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                    request.getThreadPoolSize(), LightSpeedRetailXConstants.excludedEntities,
                    LightSpeedRetailXConstants.LightSpeedXRetailAPI.API_URL,
                    LightSpeedRetailXConstants.LightSpeedXRetailAPI.API_KEY);

            Connector lightspeedXRetailConnector = new LightSpeedRetailXConnector(request.getConnectorType(), config,
                    request.getOutputPath());
            lightspeedXRetailConnector.run(LightSpeedRetailXConstants.HEADERS_BY_ENTITY, queryParams, Map.of(),
                    LightSpeedRetailXConstants.getEntityApiPathMap(), LightSpeedRetailXConstants.ENTITY_DEPENDENCY_MAP);

        } else if (null != request.getConnectorName() &&
                request.getConnectorName().equals(GustoConstants.CONNECTOR_NAME)) {
            try {
                // Fetch company ID from token info endpoint
                String companyId = GustoService.getCompanyId(request.getAccessToken(), request.getEnvironment());

                Map<String, String> queryParams = Map.of();
                Config config = new Config(
                        GustoConstants.CONNECTOR_NAME, request.getCsvRowLimit(), request.getThreadPoolSize(),
                        GustoConstants.excludedEntities, GustoConstants.getGustoBaseUrl(request.getEnvironment()),
                        request.getAccessToken());

                // Create GustoRestConnectionType with OAuth2 support if refresh token is provided
                Connector gustoConnector;
                if (request.getRefreshToken() != null && !request.getRefreshToken().isEmpty()) {
                    GustoRestConnectionType gustoConnection = new GustoRestConnectionType(
                            GustoConstants.getGustoBaseUrl(request.getEnvironment()),
                            request.getAccessToken(),
                            request.getClientId(),
                            request.getClientSecret(),
                            request.getRefreshToken(),
                            request.getRedirectUri(),
                            request.getAuthorizationCode()
                    );
                    gustoConnector = new GustoConnector(gustoConnection, config, request.getOutputPath());
                } else {
                    System.out.println("Using Gusto with static access token");
                    GustoRestConnectionType gustoConnection = new GustoRestConnectionType(
                            GustoConstants.getGustoBaseUrl(request.getEnvironment()),
                            request.getAccessToken()
                    );
                    gustoConnector = new GustoConnector(gustoConnection, config, request.getOutputPath());
                }

                if (Objects.nonNull(request.getLastSyncDate())) {
                    LocalDate startDate = LocalDate.parse(request.getLastSyncDate());
                    queryParams = Map.of("start_date", startDate.toString());
                    gustoConnector.run(GustoConstants.DELTA_HEADERS_BY_ENTITY, queryParams,
                            Map.of(GustoConstants.API_VERSION_HEADER, GustoConstants.API_VERSION),
                            GustoConstants.getEntityApiPathMap(companyId), GustoConstants.ENTITY_DEPENDENCY_MAP);
                    gustoConnector.run(GustoConstants.NON_DELTA_HEADERS_BY_ENTITY, Map.of(),
                            Map.of(GustoConstants.API_VERSION_HEADER, GustoConstants.API_VERSION),
                            GustoConstants.getEntityApiPathMap(companyId), GustoConstants.ENTITY_DEPENDENCY_MAP);
                } else {
                    gustoConnector.run(
                            GustoConstants.HEADERS_BY_ENTITY, queryParams,
                            Map.of(GustoConstants.API_VERSION_HEADER, GustoConstants.API_VERSION),
                            GustoConstants.getEntityApiPathMap(companyId), GustoConstants.ENTITY_DEPENDENCY_MAP);
                }
            } catch (DateTimeException dte) {
                return "Error: Invalid date format for lastSyncDate. Expected format: YYYY-MM-DD";
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        } else if (null != request.getConnectorName() &&
                request.getConnectorName().equals(OnePageCRMConstants.CONNECTOR_NAME)) {

            try {
                // Create connection with appropriate authentication
                OnePageCRMRestConnectionType connection;
                connection =
                        new OnePageCRMRestConnectionType(
                                OnePageCRMConstants.BASE_API_URL,
                                request.getUsername(), request.getPassword());


                Config config = new Config(OnePageCRMConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                        request.getThreadPoolSize(), OnePageCRMConstants.excludedEntities,
                        OnePageCRMConstants.BASE_API_URL, request.getAccessToken());

                Connector connector = new OnePageCRMConnector(connection, config, request.getOutputPath());

                // Build per-entity pagination map (all entities share the same pagination for now)
                PaginationInfo pagination = connection.buildPagination(100);
                Map<String, PaginationInfo> paginationByEntity = new HashMap<>();

                Map<String, String> queryParams;
                if (Objects.isNull(request.getLastSyncDate())) {
                    queryParams = Map.of();
                    OnePageCRMConstants.HEADERS_BY_ENTITY
                            .keySet()
                            .forEach(entity -> paginationByEntity.put(entity, pagination));
                    connector.run(
                            OnePageCRMConstants.HEADERS_BY_ENTITY,
                            queryParams,
                            Map.of(),
                            OnePageCRMConstants.getEntityApiPathMap(),
                            Map.of(),
                            paginationByEntity
                                 );
                } else {
                    LocalDateTime startDate = LocalDateTime.parse(request.getLastSyncDate());
                    queryParams = Map.of("since", startDate.toString());
                    OnePageCRMConstants.DELTA_HEADERS_BY_ENTITY
                            .keySet()
                            .forEach(entity -> paginationByEntity.put(entity, pagination));
                    OnePageCRMConstants.NON_DELTA_HEADERS_BY_ENTITY
                            .keySet()
                            .forEach(entity -> paginationByEntity.put(entity, pagination));
                    connector.run(
                            OnePageCRMConstants.DELTA_HEADERS_BY_ENTITY,
                            queryParams, Map.of(), OnePageCRMConstants.getEntityApiPathMap(),
                            Map.of(), paginationByEntity);
                    connector.run(
                            OnePageCRMConstants.NON_DELTA_HEADERS_BY_ENTITY,
                            Map.of(), Map.of(), OnePageCRMConstants.getEntityApiPathMap(),
                            Map.of(), paginationByEntity);
                }

                return "OnePageCRM Connector completed successfully!";

            } catch (Exception e) {
                e.printStackTrace();
                return "Error running OnePageCRM Connector: " + e.getMessage();
            }
        } else {
            System.out.println("Connector not supported");
        }
        return "Connector " + request.getConnectorName() + " Started!";
    }
}
