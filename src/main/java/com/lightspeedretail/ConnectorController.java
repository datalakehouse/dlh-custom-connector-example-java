package com.lightspeedretail;

import com.lightspeedretail.common.GustoConstants;
import com.lightspeedretail.common.LightSpeedRetailXConstants;
import com.lightspeedretail.connector.GustoConnector;
import com.lightspeedretail.connector.LightSpeedRetailXConnector;
import com.lightspeedretail.dto.RunConnectorRequest;
import com.lightspeedretail.service.GustoService;
import io.datalakehouse.config.Config;
import io.datalakehouse.connectors.core.Connector;
import java.time.DateTimeException;
import java.time.LocalDate;
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
                String companyId = GustoService.getCompanyId(request.getAccessToken());

                Map<String, String> queryParams = Map.of();
                Config config = new Config(
                        GustoConstants.CONNECTOR_NAME, request.getCsvRowLimit(), request.getThreadPoolSize(),
                        GustoConstants.excludedEntities, GustoConstants.GustoAPI.API_URL, request.getAccessToken());

                Connector gustoConnector =
                        new GustoConnector(request.getConnectorType(), config, request.getOutputPath());

                Map<String, String> customHeaders = Map.of("X-Gusto-API-Version", "2025-06-15");

                if (Objects.nonNull(request.getLastSyncDate())) {
                    LocalDate startDate = LocalDate.parse(request.getLastSyncDate());
                    queryParams = Map.of("start_date", startDate.toString());
                    gustoConnector.run(
                            Map.of(
                                    GustoConstants.GustoEntityNames.PAY_PERIODS,
                                    GustoConstants.GustoHeaders.PAY_PERIODS), queryParams, customHeaders,
                            GustoConstants.getEntityApiPathMap(companyId), GustoConstants.ENTITY_DEPENDENCY_MAP);
                } else {
                    gustoConnector.run(
                            GustoConstants.HEADERS_BY_ENTITY, queryParams, customHeaders,
                            GustoConstants.getEntityApiPathMap(companyId), GustoConstants.ENTITY_DEPENDENCY_MAP);
                }
            } catch (DateTimeException dte) {
                return "Error: Invalid date format for lastSyncDate. Expected format: YYYY-MM-DD";
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        } else {
            System.out.println("Connector not supported");
        }
        return "Connector " + request.getConnectorName() + " Started!";
    }
}

