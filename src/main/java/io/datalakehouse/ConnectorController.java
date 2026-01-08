package io.datalakehouse;

import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.ingest.IngestConnectorRunner;
import io.datalakehouse.ingest.RunResult;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectorController {
    private final Map<String, IngestConnectorRunner> runners;

    public ConnectorController(Set<IngestConnectorRunner> runnerSet) {
        this.runners = runnerSet.stream().collect(Collectors.toUnmodifiableMap(IngestConnectorRunner::name, r -> r));
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the Custom Connectors Home Page!";
    }

    @GetMapping("/callback")
    public String printRequestParams(@RequestParam Map<String, String> allParams) {
        if (allParams.isEmpty()) {
            return "No request parameters found!";
        }
        StringBuilder response = new StringBuilder("Received parameters:\n");
        allParams.forEach((k, v) -> response.append(k).append(" = ").append(v).append("\n"));
        return response.toString();
    }

    @PostMapping("/runConnector")
    public String runConnector(@RequestBody IngestConnectorRunRequest request) {
        if (request.getConnectorName() == null || request.getConnectorName().isBlank()) {
            return "Error: connectorName is required";
        }

        IngestConnectorRunner runner = runners.get(request.getConnectorName());
        if (runner == null) {
            return "Error: Connector not supported: " + request.getConnectorName();
        }

        RunResult result = runner.run(request);
        return result.message();
    }



    // Old Code
    /*@PostMapping("/runConnector")
    public String runConnector(@RequestBody RunConnectorRequest request) {
        if (null != request.getConnectorName() && request.getConnectorName()
                .equals(LightSpeedRetailXConstants.CONNECTOR_NAME)) {
            Map<String, String> queryParams = Map.of();
            Config config = new Config(LightSpeedRetailXConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                    request.getThreadPoolSize(), LightSpeedRetailXConstants.excludedEntities,
                    LightSpeedRetailXConstants.LightSpeedXRetailAPI.API_URL,
                    LightSpeedRetailXConstants.LightSpeedXRetailAPI.API_KEY);

            DLHIngest lightspeedXRetailConnector = new LightSpeedRetailXConnector(request.getConnectorType(), config,
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
                DLHIngest gustoConnector;
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

                DLHIngest connector = new OnePageCRMConnector(connection, config, request.getOutputPath());

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
        } else if (null != request.getConnectorName() &&
                request.getConnectorName().equals(FreshDeskConstants.CONNECTOR_NAME)) {

            try {
                // Build FreshDesk API base URL from domain
                String baseUrl = request.getBaseUrl();

                // Create connection with Basic Authentication
                FreshDeskRestConnectionType connection;
                if (request.getUsername() != null && !request.getUsername().isBlank() &&
                           request.getPassword() != null && !request.getPassword().isBlank()) {

                    connection = new FreshDeskRestConnectionType(baseUrl, request.getUsername(), request.getPassword());
                } else {
                    return "Error: FreshDesk requires username/password";
                }

                // Create configuration
                Config config = new Config(
                        FreshDeskConstants.CONNECTOR_NAME,
                        request.getCsvRowLimit(),
                        request.getThreadPoolSize(),
                        FreshDeskConstants.excludedEntities,
                        baseUrl,
                        request.getAccessToken() != null ? request.getAccessToken() : ""
                );

                // Create connector
                DLHIngest freshdeskConnector = new FreshDeskConnector(connection, config, request.getOutputPath());

                if (Objects.isNull(request.getLastSyncDate())) {
                    freshdeskConnector.run(FreshDeskConstants.HEADERS_BY_ENTITY, Map.of(), Map.of(), FreshDeskConstants.getEntityApiPathMap(), FreshDeskConstants.ENTITY_DEPENDENCY_MAP);
                } else {
                    LocalDateTime lastSyncDate = LocalDateTime.parse(request.getLastSyncDate());
                    Map<String, String> queryParams = Map.of("updated_since", lastSyncDate.toString());
                    freshdeskConnector.run(FreshDeskConstants.DELTA_HEADERS_BY_ENTITY, queryParams, Map.of(), FreshDeskConstants.getEntityApiPathMap(), FreshDeskConstants.ENTITY_DEPENDENCY_MAP);
                    freshdeskConnector.run(FreshDeskConstants.NON_DELTA_HEADERS_BY_ENTITY, Map.of(), Map.of(), FreshDeskConstants.getEntityApiPathMap(), FreshDeskConstants.ENTITY_DEPENDENCY_MAP);
                }

                return "FreshDesk Connector completed successfully!";

            } catch (DateTimeException dte) {
                return "Error: Invalid date format for lastSyncDate. Expected format: YYYY-MM-DDTHH:MM:SSZ";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error running FreshDesk Connector: " + e.getMessage();
            }
        } else if (null != request.getConnectorName() &&
                request.getConnectorName().equals(FreshServiceConstants.CONNECTOR_NAME)) {

            try {
                // Build FreshService API base URL from domain
                String baseUrl = request.getBaseUrl();

                Config config = new Config(
                        FreshServiceConstants.CONNECTOR_NAME, request.getCsvRowLimit(), request.getThreadPoolSize(),
                        FreshServiceConstants.excludedEntities, baseUrl, request.getAccessToken());

                FreshServiceRestConnectionType freshServicesConnection = new FreshServiceRestConnectionType(
                        baseUrl, request.getAccessToken(), request.getClientId(), request.getClientSecret(),
                        request.getRefreshToken(), request.getRedirectUri(), request.getAuthorizationCode(), request.getAuthUrl());

                if (Objects.isNull(request.getLastSyncDate())) {
                    // Full sync - process all entities with dependencies
                    FreshServicesConnector freshServicesConnector =
                            new FreshServicesConnector(freshServicesConnection, config, request.getOutputPath());

                    freshServicesConnector.run(FreshServiceConstants.HEADERS_BY_ENTITY, Map.of(), Map.of(),
                            FreshServiceConstants.getEntityApiPathMap(), FreshServiceConstants.ENTITY_DEPENDENCY_MAP);
                } else {
                    // Delta sync - process only changed records
                    Instant lastSyncDate = Instant.parse(request.getLastSyncDate());
                    Map<String, String> queryParams = Map.of("updated_since", lastSyncDate.toString());

                    // Create connector instance with lastSyncDate for timestamp filtering
                    FreshServicesConnector freshServicesConnector =
                            new FreshServicesConnector(freshServicesConnection, config, request.getOutputPath(), lastSyncDate);

                    // Process delta entities with updated_since parameter
                    freshServicesConnector.run(FreshServiceConstants.DELTA_HEADERS_BY_ENTITY, queryParams, Map.of(),
                            FreshServiceConstants.getEntityApiPathMap(), Map.of());

//                     Process non-delta entities without updated_since parameter
//                     Timestamp filtering will be applied in-memory based on CREATED_AT/UPDATED_AT fields
                    freshServicesConnector.run(FreshServiceConstants.NON_DELTA_HEADERS_BY_ENTITY, Map.of(), Map.of(),
                            FreshServiceConstants.getEntityApiPathMap(), FreshServiceConstants.ENTITY_DEPENDENCY_MAP);
                }

                return "FreshService Connector completed successfully!";

            } catch (DateTimeParseException dte) {
                return "Error: Invalid date format for lastSyncDate. Expected format: YYYY-MM-DDTHH:MM:SSZ";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error running FreshService Connector: " + e.getMessage();
            }

        } else {
            System.out.println("Connector not supported");
        }
        return "Connector " + request.getConnectorName() + " Started!";
    }*/
}
