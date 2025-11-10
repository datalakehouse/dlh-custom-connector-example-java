package com.lightspeedretail;

import com.lightspeedretail.common.LightSpeedRetailXConstants;
import com.lightspeedretail.connector.LightSpeedRetailXConnector;
import com.lightspeedretail.dto.RunConnectorRequest;
import io.datalakehouse.config.Config;
import io.datalakehouse.connectors.core.Connector;
import java.util.Map;
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
            lightspeedXRetailConnector.run(LightSpeedRetailXConstants.HEADERS_BY_ENTITY, queryParams,
                    LightSpeedRetailXConstants.getEntityApiPathMap(), LightSpeedRetailXConstants.ENTITY_DEPENDENCY_MAP);
        } else {
            System.out.println("Connector not supported");
        }
        return "Connector " + request.getConnectorName() + " Started!";
    }
}

