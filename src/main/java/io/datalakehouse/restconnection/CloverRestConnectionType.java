package io.datalakehouse.restconnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.CloverConstants;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CloverRestConnectionType extends RestConnectionType {

    Logger logger = LoggerFactory.getLogger(CloverRestConnectionType.class);

    private String refreshToken;

    /**
     * Constructor for Clover with OAuth2 support (refresh token flow).
     */
    public CloverRestConnectionType(String baseUrl, String accessToken,
                                   String clientId, String clientSecret, String refreshToken) {
        super(baseUrl, accessToken, clientId, clientSecret, null, null);
        this.refreshToken = refreshToken;
    }

    /**
     * Build pagination configuration for Clover.
     * Clover uses offset-based pagination with offset/limit.
     */
    public PaginationInfo buildPagination(Integer pageSize) {
        int size = (pageSize != null && pageSize > 0) ? Math.min(pageSize, CloverConstants.MAX_PAGE_SIZE) : CloverConstants.DEFAULT_PAGE_SIZE;
        // Clover uses offset-based pagination
        return new PaginationInfo(
                CloverConstants.OFFSET,
                CloverConstants.LIMIT,
                0,
                size,
                state -> true,
                null
        );
    }

    @Override
    protected void refreshOAuth2Token() throws IOException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IOException("Refresh token is not available. Cannot refresh Clover OAuth2 token.");
        }

        HttpPost post = new HttpPost(baseUrl + "/oauth/v2/refresh");

        // Create JSON request body as per Clover's API specification
        JSONObject requestBody = new JSONObject();
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("refresh_token", refreshToken);

        post.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");

        try (CloseableHttpResponse response = getHttpClient().execute(post)) {
            int status = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);

                if (!json.has("access_token")) {
                    throw new IOException("Response does not contain access_token: " + body);
                }

                this.currentAccessToken = json.getString("access_token");

                // Update refresh token if a new one is provided
                if (json.has("refresh_token")) {
                    this.refreshToken = json.getString("refresh_token");
                }

                // Set token expiry (default to 1 hour if not provided, refresh 1 minute early)
                int expiresIn = json.optInt("expires_in", 3600);
                this.tokenExpiryTimeMillis = System.currentTimeMillis() + (expiresIn - 60) * 1000L;

                logger.debug("Successfully refreshed Clover OAuth2 token. Expires in: {} seconds", expiresIn);
            } else {
                throw new IOException("Failed to refresh Clover OAuth2 token. Status: " + status + ", Response: " + body);
            }
        }
    }

    @Override
    public void fetchDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, PaginationInfo.PageProcessor pageProcessor)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        int offset = 0;
        int totalFetched = 0;
        int pageNumber = 1;

        boolean hasMoreData = true;
        while (hasMoreData) {
            Map<String, String> pagedQuery = new HashMap<>();
            if (queryParams != null) {
                pagedQuery.putAll(queryParams);
            }

            // Clover uses offset and limit for pagination
            pagedQuery.put(paginationInfo.pageParam(), String.valueOf(offset));
            pagedQuery.put(paginationInfo.sizeParam(), String.valueOf(paginationInfo.pageSize()));

            byte[] pageBytes;
            try (InputStream pageStream = fetchData(entity, apiPath, pagedQuery, customHeaders, entityIds)) {
                if (pageStream == null) {
                    logger.info("No more data returned for entity: {}", entity);
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
                    null,
                    null
            );

            // Process this page immediately
            boolean shouldContinue;
            try (InputStream pageInputStream = new ByteArrayInputStream(pageBytes)) {
                shouldContinue = pageProcessor.processPage(pageInputStream, state);
            }

            logger.debug("Processed page {} for entity {}: fetched {} items, total: {}",
                    pageNumber, entity, lastPageSize, totalFetched);

            // Determine if there's more data
            hasMoreData = lastPageSize >= paginationInfo.pageSize();

            if (!shouldContinue || lastPageSize == 0) {

                logger.debug("Stopping pagination for entity {}. shouldContinue: {}, lastPageSize: {}",
                        entity, shouldContinue, lastPageSize);
                break;
            }

            offset += paginationInfo.pageSize();
            pageNumber++;
        }
    }

    private int checkLastPageSize(JsonNode node) {
        if (!node.isArray() && !node.has(CloverConstants.ELEMENTS) && !node.has("result")) {
            return node.isObject() ? 1 : 0;
        }

        if (node.isArray()) {
            return node.size();
        }

        if (node.isObject() && node.has(CloverConstants.ELEMENTS)) {
            JsonNode elementsNode = node.get(CloverConstants.ELEMENTS);
            if (elementsNode.isArray()) {
                return elementsNode.size();
            }
        }

        return 0;
    }
}
