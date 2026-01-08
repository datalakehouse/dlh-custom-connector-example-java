package io.datalakehouse.runners;

import io.datalakehouse.common.LightSpeedRetailXConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connector.LightSpeedRetailXConnector;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.ingest.BaseRunner;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * @author Nisarg Raval
 */
@Component
public class LightSpeedRetailXRunner extends BaseRunner {

    @Override
    public String name() {
        return LightSpeedRetailXConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) {
        Map<String, String> queryParams = Map.of();

        DLHIngestConfig config = new DLHIngestConfig(LightSpeedRetailXConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                request.getThreadPoolSize(), LightSpeedRetailXConstants.excludedEntities,
                LightSpeedRetailXConstants.LightSpeedXRetailAPI.API_URL,
                LightSpeedRetailXConstants.LightSpeedXRetailAPI.API_KEY);

        DLHIngest connector = new LightSpeedRetailXConnector(request.getConnectorType(), config,
                request.getOutputPath());

        connector.run(LightSpeedRetailXConstants.HEADERS_BY_ENTITY, queryParams, Map.of(),
                LightSpeedRetailXConstants.getEntityApiPathMap(), LightSpeedRetailXConstants.ENTITY_DEPENDENCY_MAP);
    }
}