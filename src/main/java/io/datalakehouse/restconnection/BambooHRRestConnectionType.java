package io.datalakehouse.restconnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.BambooHRConstants;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class BambooHRRestConnectionType extends RestConnectionType {

    Logger logger = Logger.getLogger(BambooHRRestConnectionType.class.getName());

    private String refreshToken;
    private static final String GRANT_TYPE = "refresh_token";

    public BambooHRRestConnectionType(String baseUrl, String clientId, String clientSecret, String refreshToken, String accessToken) {
        super(baseUrl, accessToken, clientId, clientSecret, null, null);
        this.refreshToken = refreshToken;
    }

    /**
     * Build pagination configuration for BambooHR.
     * BambooHR uses cursor-based pagination with page[after] and page[limit].
     */
    public PaginationInfo buildPagination(Integer pageSize) {
        int size = (pageSize != null && pageSize > 0) ? pageSize : BambooHRConstants.MAX_PAGE_SIZE;
        // BambooHR uses cursor-based pagination
        return new PaginationInfo(
                BambooHRConstants.PAGE_AFTER,
                BambooHRConstants.PAGE_LIMIT,
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
        String cursor = null;
        int totalFetched = 0;
        int pageNumber = 1;

        boolean hasNextPage = true;
        boolean isFirstRequest = true;

        while (hasNextPage) {
            Map<String, String> pagedQuery = new HashMap<>();
            if (queryParams != null) {
                pagedQuery.putAll(queryParams);
            }

            // BambooHR uses cursor-based pagination with page[after] and page[limit]
            // Add cursor only after first response that indicates pagination support
            if (cursor != null) {
                pagedQuery.put(paginationInfo.pageParam(), cursor);
                pagedQuery.put(paginationInfo.sizeParam(), String.valueOf(paginationInfo.pageSize()));
            } else if (!isFirstRequest) {
                // After first request, if no cursor but pagination was detected, add limit
                pagedQuery.put(paginationInfo.sizeParam(), String.valueOf(paginationInfo.pageSize()));
            }

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

            // Mark that we've completed the first request
            isFirstRequest = false;

            // Check if response has meta object (indicates pagination support)
            boolean hasPagination = root.has(BambooHRConstants.META);

            if (hasPagination) {
                JsonNode metaNode = root.get(BambooHRConstants.META);
                JsonNode pageNode = metaNode.has(BambooHRConstants.PAGE) ? metaNode.get(BambooHRConstants.PAGE) : null;

                if (pageNode != null && pageNode.has(BambooHRConstants.NEXT_CURSOR)) {
                    JsonNode nextCursorNode = pageNode.get(BambooHRConstants.NEXT_CURSOR);
                    hasNextPage = !nextCursorNode.isNull();
                    cursor = hasNextPage ? nextCursorNode.asText() : null;
                } else {
                    hasNextPage = false;
                }
            } else {
                // No meta object means no pagination support for this entity
                hasNextPage = false;
            }

            int lastPageSize = checkLastPageSize(root);
            totalFetched += lastPageSize;

            // Create pagination state
            PaginationInfo.PaginationState state = new PaginationInfo.PaginationState(
                    pageNumber,
                    totalFetched,
                    lastPageSize,
                    null, // totalCount can be extracted from meta.total if needed
                    null  // maxPage not provided by BambooHR
            );

            boolean shouldContinue;
            try (InputStream pageInputStream = new ByteArrayInputStream(pageBytes)) {
                shouldContinue = pageProcessor.processPage(pageInputStream, state);
            }

            // Log pagination info if logging is enabled
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                logger.info(String.format("Processed page %d for entity %s: fetched %d items, total: %d, hasNextPage: %b",
                        pageNumber, entity, lastPageSize, totalFetched, hasNextPage));
            }

            // Check if we should continue
            if (!shouldContinue || lastPageSize == 0) {
                if (logger.isLoggable(java.util.logging.Level.INFO)) {
                    logger.info(String.format("Stopping pagination for entity %s. shouldContinue: %b, hasNextPage: %b, lastPageSize: %d",
                            entity, shouldContinue, hasNextPage, lastPageSize));
                }
                break;
            }

            pageNumber++;
        }
    }

    private int checkLastPageSize(JsonNode node) {
        if (node.isArray()) {
            return node.size();
        }
        // Check for 'data' node which contains the actual data array
        if (node.isObject() && node.has(BambooHRConstants.DATA)) {
            JsonNode dataNode = node.get(BambooHRConstants.DATA);
            if (dataNode.isArray()) {
                return dataNode.size();
            }
        }

        return 0;
    }

    @Override
    protected void refreshOAuth2Token() throws IOException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IOException("Refresh token is not available. Cannot refresh BambooHR OAuth2 token.");
        }

        HttpPost post = new HttpPost(baseUrl + "/token.php");

        // BambooHR expects application/x-www-form-urlencoded
        List<org.apache.http.NameValuePair> params = new java.util.ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
        params.add(new BasicNameValuePair("refresh_token", refreshToken));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));

        post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        try (CloseableHttpResponse response = getHttpClient().execute(post)) {
            int status = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);

                if (!json.has("access_token")) {
                    throw new IOException("Response does not contain access_token: " + body);
                }

                this.currentAccessToken = json.getString("access_token");

                if (json.has("refresh_token")) {
                    this.refreshToken = json.getString("refresh_token");
                }

                int expiresIn = json.optInt("expires_in", 3600);
                this.tokenExpiryTimeMillis = System.currentTimeMillis() + (expiresIn - 60) * 1000L;

                logger.info("Successfully refreshed BambooHR OAuth2 token. Expires in: " + expiresIn + " seconds");
            } else {
                throw new IOException("Failed to refresh BambooHR OAuth2 token. Status: " + status + ", Response: " + body);
            }
        }
    }

}
