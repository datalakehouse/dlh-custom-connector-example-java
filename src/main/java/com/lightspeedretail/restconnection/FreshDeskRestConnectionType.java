package com.lightspeedretail.restconnection;

import io.datalakehouse.connectors.core.AuthConfig;
import io.datalakehouse.connectors.impl.RestConnectionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FreshDesk specific REST connection with Basic Authentication support.
 */
public class FreshDeskRestConnectionType extends RestConnectionType {

    public FreshDeskRestConnectionType(String baseUrl, String username, String password) {
        super(baseUrl, null, AuthConfig.Presets.basic(username, password));
    }

    /**
     * Override fetchData to filter out query parameters for dependent entities.
     * When entityIds is not empty, it means this is a dependent entity (e.g., TICKETS_CONVERSATIONS)
     * and we should NOT pass query parameters like "updated_since".
     */
    @Override
    public InputStream fetchData(
            String entity, String apiPath, Map<String, String> queryParams,
            Map<String, String> customHeaders, List<String> entityIds) throws IOException {

        if (!entityIds.isEmpty()) {
            queryParams = new HashMap<>();
        }

        return super.fetchData(entity, apiPath, queryParams, customHeaders, entityIds);
    }
}

