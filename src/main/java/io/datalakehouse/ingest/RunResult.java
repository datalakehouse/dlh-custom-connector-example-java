package io.datalakehouse.ingest;

/**
 * @author Nisarg Raval
 */
public record RunResult(boolean success, String message) {
    public static RunResult ok(String msg) {
        return new RunResult(true, msg);
    }

    public static RunResult fail(String msg) {
        return new RunResult(false, msg);
    }
}