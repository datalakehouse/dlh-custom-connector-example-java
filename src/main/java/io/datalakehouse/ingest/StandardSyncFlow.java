package io.datalakehouse.ingest;

import io.datalakehouse.connectors.core.DLHIngest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nisarg Raval
 */
public final class StandardSyncFlow {

    private StandardSyncFlow() {
    }

    /**
     * Standard pattern:
     * - if lastSyncDate blank => run FULL
     * - else => run DELTA (with params) + run NON_DELTA (no params)
     */
    public static void run(DLHIngest connector, String lastSyncDate,
            Function<String, Map<String, String>> deltaQueryParamsFactory,

            Map<String, String[]> headersFull, Map<String, String[]> headersDelta,
            Map<String, String[]> headersNonDelta,

            Map<String, String> entityApiPaths, Map<String, List<String>> dependencyMap,

            Map<String, String> extraHeaders) {
        if (lastSyncDate == null || lastSyncDate.isBlank()) {
            connector.run(headersFull, Map.of(), extraHeaders, entityApiPaths, dependencyMap);
            return;
        }

        Map<String, String> deltaParams = deltaQueryParamsFactory.apply(lastSyncDate);

        connector.run(headersDelta, deltaParams, extraHeaders, entityApiPaths, dependencyMap);

        connector.run(headersNonDelta, Map.of(), extraHeaders, entityApiPaths, dependencyMap);
    }
}