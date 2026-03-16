package io.datalakehouse.restconnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.GorgiasConstants;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gorgias REST connection type with OAuth2 refresh-token support and cursor-based pagination.
 * <p>
 * Gorgias uses two response shapes:
 * <ul>
 *   <li>Direct JSON array  – e.g. {@code /api/phone/voice-call-recordings}</li>
 *   <li>Envelope object    – {@code { "data": [...], "meta": { "next_cursor": "..." } }}</li>
 * </ul>
 * Pagination stops when {@code meta.next_cursor} is absent/null or the page returns no records.
 */
public class GorgiasRestConnectionType extends RestConnectionType {

    private static final Logger logger = LoggerFactory.getLogger(GorgiasRestConnectionType.class);

    private static final String CURSOR_PARAM = "cursor";
    private static final String LIMIT_PARAM = "limit";
    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int MAX_PAGE_SIZE = 200;
    private static final String DATA_FIELD = "data";
    private static final String META_FIELD = "meta";
    private static final String NEXT_CURSOR_FIELD = "next_cursor";

    private String refreshToken;

    /**
     * Constructor – OAuth2 access-token + refresh-token flow.
     *
     * @param baseUrl      e.g. {@code https://your-domain.gorgias.com}
     * @param accessToken  current Bearer access token
     * @param clientId     OAuth2 client / app id
     * @param clientSecret OAuth2 client secret
     * @param refreshToken OAuth2 refresh token
     */
    public GorgiasRestConnectionType(String baseUrl, String accessToken,
                                     String clientId, String clientSecret,
                                     String refreshToken) {
        super(baseUrl, accessToken, clientId, clientSecret, null, null);
        this.refreshToken = refreshToken;
    }

    /**
     * Build a {@link PaginationInfo} for Gorgias cursor-based pagination.
     */
    public PaginationInfo buildPagination(Integer pageSize) {
        int size = (pageSize != null && pageSize > 0)
                ? Math.min(pageSize, MAX_PAGE_SIZE)
                : DEFAULT_PAGE_SIZE;
        return new PaginationInfo(
                CURSOR_PARAM,
                LIMIT_PARAM,
                0,
                size,
                state -> true,
                null
        );
    }

    @Override
    public void fetchDataPaginated(
            String entity,
            String apiPath,
            Map<String, String> queryParams,
            Map<String, String> customHeaders,
            List<String> entityIds,
            PaginationInfo paginationInfo,
            PaginationInfo.PageProcessor pageProcessor) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String nextCursor = null;
        int pageNumber = 1;
        int totalFetched = 0;

        while (true) {
            Map<String, String> pagedQuery = new HashMap<>();
            if (queryParams != null) {
                pagedQuery.putAll(queryParams);
            }
            // Always include the limit
            pagedQuery.put(LIMIT_PARAM, String.valueOf(paginationInfo.pageSize()));
            // Add cursor if we have one from a previous page
            if (nextCursor != null && !nextCursor.isBlank()) {
                pagedQuery.put(CURSOR_PARAM, nextCursor);
            }

            byte[] pageBytes;
            try (InputStream pageStream = fetchData(entity, apiPath, pagedQuery, customHeaders, entityIds)) {
                if (pageStream == null) {
                    logger.info("No data returned for entity: {}", entity);
                    break;
                }
                pageBytes = pageStream.readAllBytes();
            }

            JsonNode root = mapper.readTree(pageBytes);
            int currentPageSize = countPageSize(root);
            totalFetched += currentPageSize;

            PaginationInfo.PaginationState state = new PaginationInfo.PaginationState(
                    pageNumber,
                    totalFetched,
                    currentPageSize,
                    null,
                    null
            );

            boolean shouldContinue;
            try (InputStream pageInputStream = new ByteArrayInputStream(pageBytes)) {
                shouldContinue = pageProcessor.processPage(pageInputStream, state);
            }

            nextCursor = extractNextCursor(root);

            logger.debug("Processed page {} for entity {}: fetched {} items, total: {}, nextCursor: {}",
                    pageNumber, entity, currentPageSize, totalFetched, nextCursor);

            if (!shouldContinue || currentPageSize == 0 || nextCursor == null || nextCursor.isBlank()) {
                logger.debug("Stopping pagination for entity {}. shouldContinue: {}, pageSize: {}, nextCursor: {}",
                        entity, shouldContinue, currentPageSize, nextCursor);
                break;
            }

            pageNumber++;
        }
    }

    /**
     * Refreshes the Gorgias OAuth2 access token using the refresh_token grant.
     * The request uses {@code application/x-www-form-urlencoded} body and a
     * Basic-auth header built from clientId:clientSecret (base64).
     */
    @Override
    protected void refreshOAuth2Token() throws IOException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IOException("Refresh token is not available. Cannot refresh Gorgias OAuth2 token.");
        }

        HttpPost post = new HttpPost(baseUrl + "/oauth/token");

        // Build form-encoded body
        String formBody = "grant_type=refresh_token"
                + "&refresh_token=" + encodeFormParam(refreshToken);

        post.setEntity(new StringEntity(formBody, StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Accept", "application/json");

        // Basic auth header: Base64(clientId:clientSecret)
        String credentials = clientId + ":" + clientSecret;
        String basicAuth = java.util.Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        post.setHeader("Authorization", "Basic " + basicAuth);

        try (CloseableHttpResponse response = getHttpClient().execute(post)) {
            int status = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);

                if (!json.has("access_token")) {
                    throw new IOException("Gorgias token response does not contain access_token: " + body);
                }

                this.currentAccessToken = json.getString("access_token");

                if (json.has("refresh_token")) {
                    this.refreshToken = json.getString("refresh_token");
                }

                int expiresIn = json.optInt("expires_in", 3600);
                this.tokenExpiryTimeMillis = System.currentTimeMillis() + (expiresIn - 60) * 1000L;

                logger.info("Successfully refreshed Gorgias OAuth2 token. Expires in: {} seconds", expiresIn);
            } else {
                throw new IOException(
                        "Failed to refresh Gorgias OAuth2 token. Status: " + status + ", Response: " + body);
            }
        }
    }

    /**
     * Count the number of records in a Gorgias API response page.
     */
    private int countPageSize(JsonNode root) {
        if (root == null || root.isNull()) {
            return 0;
        }
        // Envelope shape: { "data": [...] }
        if (root.isObject() && root.has(DATA_FIELD) && root.get(DATA_FIELD).isArray()) {
            return root.get(DATA_FIELD).size();
        }
        // Raw array
        if (root.isArray()) {
            return root.size();
        }
        // Single object
        if (root.isObject()) {
            return 1;
        }
        return 0;
    }

    /**
     * Extract the meta.next_cursor value from an envelope response.
     * Returns null for raw-array responses (no cursor available).
     */
    private String extractNextCursor(JsonNode root) {
        if (root == null || root.isNull() || root.isArray()) {
            return null;
        }
        JsonNode meta = root.get(META_FIELD);
        if (meta == null || meta.isNull()) {
            return null;
        }
        JsonNode cursorNode = meta.get(NEXT_CURSOR_FIELD);
        if (cursorNode == null || cursorNode.isNull()) {
            return null;
        }
        String cursor = cursorNode.asText(null);
        return (cursor == null || cursor.isBlank() || "null".equalsIgnoreCase(cursor)) ? null : cursor;
    }

    private String encodeFormParam(String value) {
        if (value == null || value.isEmpty()) return "";
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.warn("Error encoding form parameter, using raw value", e);
            return value;
        }
    }
}

