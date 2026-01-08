package io.datalakehouse.ingest;

import io.datalakehouse.dto.IngestConnectorRunRequest;

/**
 * @author Nisarg Raval
 */
public abstract class BaseRunner implements IngestConnectorRunner {
    @Override
    public final RunResult run(IngestConnectorRunRequest request) {
        try {
            execute(request);
            return RunResult.ok(name() + " Connector completed successfully!");
        } catch (IllegalArgumentException e) {
            return RunResult.fail("Error: " + e.getMessage());
        } catch (Exception e) {
            return RunResult.fail("Error running " + name() + " Connector: " + e.getMessage());
        }
    }

    protected abstract void execute(IngestConnectorRunRequest request) throws Exception;
}
