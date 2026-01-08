package io.datalakehouse.ingest;

import io.datalakehouse.dto.IngestConnectorRunRequest;

/**
 * @author Nisarg Raval
 */
public interface IngestConnectorRunner {
    String name();

    RunResult run(IngestConnectorRunRequest req);
}