package io.datalakehouse.runners;

import io.datalakehouse.common.OnePageCRMConstants;
import io.datalakehouse.config.DLHIngestConfig;
import io.datalakehouse.connector.OnePageCRMConnector;
import io.datalakehouse.connectors.core.DLHIngest;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.dto.IngestConnectorRunRequest;
import io.datalakehouse.helpers.Req;
import io.datalakehouse.ingest.BaseRunner;
import io.datalakehouse.restconnection.OnePageCRMRestConnectionType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * @author Nisarg Raval
 */
@Component
public class OnePageCRMRunner extends BaseRunner {

    @Override
    public String name() {
        return OnePageCRMConstants.CONNECTOR_NAME;
    }

    @Override
    protected void execute(IngestConnectorRunRequest request) {
        String username = Req.required(request.getUsername(), "OnePageCRM requires username");
        String password = Req.required(request.getPassword(), "OnePageCRM requires password");

        OnePageCRMRestConnectionType connection = new OnePageCRMRestConnectionType(OnePageCRMConstants.BASE_API_URL,
                username, password);

        DLHIngestConfig config = new DLHIngestConfig(OnePageCRMConstants.CONNECTOR_NAME, request.getCsvRowLimit(),
                request.getThreadPoolSize(), OnePageCRMConstants.excludedEntities, OnePageCRMConstants.BASE_API_URL,
                request.getAccessToken());

        DLHIngest connector = new OnePageCRMConnector(connection, config, request.getOutputPath());

        // Pagination config (same for all entities)
        PaginationInfo pagination = connection.buildPagination(100);
        Map<String, PaginationInfo> paginationByEntity = new HashMap<>();

        boolean isDelta = Req.has(request.getLastSyncDate());

        if (!isDelta) {
            OnePageCRMConstants.HEADERS_BY_ENTITY.keySet()
                    .forEach(entity -> paginationByEntity.put(entity, pagination));

            connector.run(OnePageCRMConstants.HEADERS_BY_ENTITY, Map.of(), Map.of(),
                    OnePageCRMConstants.getEntityApiPathMap(), Map.of(), paginationByEntity);
            return;
        }

        // Delta + Non-delta
        LocalDateTime since = LocalDateTime.parse(request.getLastSyncDate());
        Map<String, String> deltaQueryParams = Map.of("since", since.toString());

        OnePageCRMConstants.DELTA_HEADERS_BY_ENTITY.keySet()
                .forEach(entity -> paginationByEntity.put(entity, pagination));

        OnePageCRMConstants.NON_DELTA_HEADERS_BY_ENTITY.keySet()
                .forEach(entity -> paginationByEntity.put(entity, pagination));

        connector.run(OnePageCRMConstants.DELTA_HEADERS_BY_ENTITY, deltaQueryParams, Map.of(),
                OnePageCRMConstants.getEntityApiPathMap(), Map.of(), paginationByEntity);

        connector.run(OnePageCRMConstants.NON_DELTA_HEADERS_BY_ENTITY, Map.of(), Map.of(),
                OnePageCRMConstants.getEntityApiPathMap(), Map.of(), paginationByEntity);
    }
}
