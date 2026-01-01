package com.lightspeedretail.dto;

import io.datalakehouse.connectors.core.ConnectionTypeOptions;

public class RunConnectorRequest {

    private String connectorName;
    private ConnectionTypeOptions connectorType;
    private int csvRowLimit;
    private int threadPoolSize;
    private String outputPath;
    private String outputType;
    private String environment;
    private String accessToken;
    private String lastSyncDate;

    private String baseUrl;
    private String authUrl;

    // OAuth2 fields for refresh token
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String redirectUri;
    private String authorizationCode;

    // Basic Auth fields
    private String username;
    private String password;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLastSyncDate() {
        return lastSyncDate;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setLastSyncDate(String lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authurl) {
        this.authUrl = authurl;
    }
}
