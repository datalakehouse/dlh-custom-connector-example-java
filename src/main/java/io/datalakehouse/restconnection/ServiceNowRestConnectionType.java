package io.datalakehouse.restconnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.ServiceNowConstants;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ServiceNowRestConnectionType extends RestConnectionType {

    Logger logger = Logger.getLogger(ServiceNowRestConnectionType.class.getName());
    
    public ServiceNowRestConnectionType(String baseUrl, String accessToken, String clientId, String clientSecret) {
        super(baseUrl, accessToken, clientId, clientSecret, baseUrl + "/oauth_token.do", null);
    }

    /**
     * Build pagination configuration for ServiceNow.
     * ServiceNow uses offset-based pagination with sysparm_offset/sysparm_limit.
     */
    public PaginationInfo buildPagination(Integer pageSize) {
        int size = (pageSize != null && pageSize > 0) ? pageSize : ServiceNowConstants.MAX_PAGE_SIZE;
        // ServiceNow uses offset-based pagination
        return new PaginationInfo(
                ServiceNowConstants.OFFSET,
                ServiceNowConstants.LIMIT,
                0,
                size,
                state -> true,
                null
        );
    }

    @Override
    public void fetchDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, PaginationInfo.PageProcessor pageProcessor)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        int offset = 0; // Start from offset 0 for pagination
        int totalFetched = 0;
        int pageNumber = 1; // Track page number for state reporting

        boolean hasMoreData = true;
        while (hasMoreData) {
            Map<String, String> pagedQuery = new HashMap<>();
            if (queryParams != null) {
                pagedQuery.putAll(queryParams);
            }

            // ServiceNow uses sysparm_offset and sysparm_limit for pagination
            pagedQuery.put(paginationInfo.pageParam(), String.valueOf(offset));
            pagedQuery.put(paginationInfo.sizeParam(), String.valueOf(paginationInfo.pageSize()));

            byte[] pageBytes;
            try (InputStream pageStream = fetchData(entity, apiPath, pagedQuery, customHeaders, entityIds)) {
                if (pageStream == null) {
                    logger.info(() -> "No more data returned for entity: " + entity);
                    break;
                }
                pageBytes = pageStream.readAllBytes();
            }

            // Parse response
            JsonNode root = mapper.readTree(pageBytes);

            int lastPageSize = checkLastPageSize(root);
            totalFetched += lastPageSize;

            // Create pagination state
            PaginationInfo.PaginationState state = new PaginationInfo.PaginationState(
                    pageNumber,
                    totalFetched,
                    lastPageSize,
                    null, // totalCount not provided by ServiceNow
                    null  // maxPage not provided by ServiceNow
            );

            // Process this page immediately
            boolean shouldContinue;
            try (InputStream pageInputStream = new ByteArrayInputStream(pageBytes)) {
                shouldContinue = pageProcessor.processPage(pageInputStream, state);
            }

            // Log pagination info if logging is enabled
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                logger.info(String.format("Processed page %d for entity %s: fetched %d items, total: %d",
                        pageNumber, entity, lastPageSize, totalFetched));
            }

            // Check if we should continue - stop if page size is less than requested size
            hasMoreData = lastPageSize >= paginationInfo.pageSize();

            if (!shouldContinue || lastPageSize == 0) {
                if (logger.isLoggable(java.util.logging.Level.INFO)) {
                    logger.info(String.format("Stopping pagination for entity %s. shouldContinue: %b, lastPageSize: %d",
                            entity, shouldContinue, lastPageSize));
                }
                break;
            }

            // Move to next page by incrementing offset
            offset += paginationInfo.pageSize();
            pageNumber++;
        }
    }

    @Override
    protected void refreshOAuth2Token() throws IOException {
        if (clientId == null || clientSecret == null) {
            throw new IOException("ClientId and ClientSecret are required for ServiceNow OAuth2 token refresh");
        }

        // ServiceNow OAuth token endpoint
        String tokenEndpoint = baseUrl + "/oauth_token.do";

        HttpPost post = new HttpPost(tokenEndpoint);
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));

        post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Accept", "application/json");

        try (CloseableHttpResponse response = getHttpClient().execute(post)) {
            int status = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (status >= 200 && status < 300) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(body);

                this.currentAccessToken = json.get("access_token").asText();
                int expiresIn = json.has("expires_in") ? json.get("expires_in").asInt() : 179998;

                // Refresh token 1 minute before expiry
                this.tokenExpiryTimeMillis = System.currentTimeMillis() + (expiresIn - 60) * 1000L;

                logger.info(() -> String.format("Successfully refreshed ServiceNow OAuth2 token. Expires in %d seconds", expiresIn));
            } else {
                String errorMsg = String.format("Failed to refresh ServiceNow OAuth2 token: %d - %s", status, body);
                logger.severe(errorMsg);
                throw new IOException(errorMsg);
            }
        }
    }

    private int checkLastPageSize(JsonNode node) {
        if (node.isArray()) {
            return node.size();
        }
        if (node.isObject() && node.has("result")) {
            JsonNode resultNode = node.get("result");
            if (resultNode.isArray()) {
                return resultNode.size();
            }
        }

        return 0;
    }
}
