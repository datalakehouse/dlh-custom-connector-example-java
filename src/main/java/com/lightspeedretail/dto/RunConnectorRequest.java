package com.lightspeedretail.dto;

import io.datalakehouse.connectors.core.ConnectionTypeOptions;

public class RunConnectorRequest {
    private String connectorName;
    private ConnectionTypeOptions connectorType;
    private int csvRowLimit;
    private int threadPoolSize;
    private String outputPath;
    private String outputType;

    // Getters and Setters
    public ConnectionTypeOptions getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(ConnectionTypeOptions connectorType) {
        this.connectorType = connectorType;
    }

    public int getCsvRowLimit() {
        return csvRowLimit;
    }

    public void setCsvRowLimit(int csvRowLimit) {
        this.csvRowLimit = csvRowLimit;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }
}
