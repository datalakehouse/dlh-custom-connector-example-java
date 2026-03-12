package io.datalakehouse.restconnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCorRestConnectionType extends RestConnectionType {

    private static final Logger logger = LoggerFactory.getLogger(PayCorRestConnectionType.class);

    private static final String CONTINUATION_TOKEN_PARAM = "continuationToken";
    private static final String HAS_MORE_RESULTS = "hasMoreResults";
    private static final String CONTINUATION_TOKEN = "continuationToken";
    private static final String RECORDS = "records";
    private String refreshToken;


    /**
     * Constructor for PayCor with OAuth2 support (refresh token flow).
     */
    public PayCorRestConnectionType(String baseUrl, String accessToken, String refreshToken, String clientId, String clientSecret) {
        super(baseUrl, accessToken, clientId, clientSecret, null, null);
        this.refreshToken = refreshToken;
    }

    /**
     * Compatibility pagination config. PayCor pagination is cursor/token based and handled in fetchDataPaginated.
     */
    public PaginationInfo buildPagination(Integer pageSize) {
        return new PaginationInfo(
                CONTINUATION_TOKEN_PARAM,
                null,
                1,
                pageSize,
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
        String continuationToken = null;
        int pageNumber = 1;
        int totalFetched = 0;

        while (true) {
            Map<String, String> pagedQuery = new HashMap<>();
            if (queryParams != null) {
                pagedQuery.putAll(queryParams);
            }
            if (continuationToken != null && !continuationToken.isBlank()) {
                pagedQuery.put(CONTINUATION_TOKEN_PARAM, continuationToken);
            }

            byte[] pageBytes;
            try (InputStream pageStream = fetchData(entity, apiPath, pagedQuery, customHeaders, entityIds)) {
                if (pageStream == null) {
                    logger.info("No more data returned for entity: {}", entity);
                    break;
                }
                pageBytes = pageStream.readAllBytes();
            }

            JsonNode root = mapper.readTree(pageBytes);
            int currentPageSize = countRecordSize(root);
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

            boolean hasMoreResults = isHasMoreResults(root);
            String nextContinuationToken = extractContinuationToken(root);

            logger.info(
                    "Processed page {} for entity {}: fetched {} items, total: {}, hasMoreResults: {}",
                    pageNumber,
                    entity,
                    currentPageSize,
                    totalFetched,
                    hasMoreResults
            );

            if (!shouldContinue || currentPageSize == 0 || !hasMoreResults || nextContinuationToken == null || nextContinuationToken.isBlank()) {
                break;
            }

            continuationToken = nextContinuationToken;
            pageNumber++;
        }
    }



    private int countRecordSize(JsonNode root) {
        if (root.isArray() && root.size() == 1) {
            root = root.get(0);
        }

        if (root != null && root.has(RECORDS) && root.get(RECORDS).isArray()) {
            return root.get(RECORDS).size();
        }
        return 0;
    }

    private boolean isHasMoreResults(JsonNode root) {
        if (root.isArray() && root.size() == 1) {
            root = root.get(0);
        }

        if (root == null || !root.has(HAS_MORE_RESULTS)) {
            return false;
        }

        JsonNode hasMoreNode = root.get(HAS_MORE_RESULTS);
        if (hasMoreNode.isBoolean()) {
            return hasMoreNode.asBoolean();
        }

        return "true".equalsIgnoreCase(hasMoreNode.asText());
    }

    private String extractContinuationToken(JsonNode root) {
        if (root.isArray() && root.size() == 1) {
            root = root.get(0);
        }

        if (root == null || !root.has(CONTINUATION_TOKEN) || root.get(CONTINUATION_TOKEN).isNull()) {
            return null;
        }
        return root.get(CONTINUATION_TOKEN).asText(null);
    }

    /**
     * PayCor-specific OAuth2 token refresh implementation.
     * Uses refresh_token grant type via PayCor's STS (Secure Token Service) endpoint.
     * Request format uses application/x-www-form-urlencoded content type.
     */
    @Override
    protected void refreshOAuth2Token() throws IOException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IOException("Refresh token is not available. Cannot refresh PayCor OAuth2 token.");
        }

        // PayCor STS endpoint for token refresh
        String tokenUrl = baseUrl + "/sts/v1/common/token";
        HttpPost post = new HttpPost(tokenUrl);

        // Build form-encoded request body as per PayCor's API specification
        StringBuilder formBody = new StringBuilder();
        formBody.append("grant_type=refresh_token");
        formBody.append("&refresh_token=").append(encodeFormParameter(refreshToken));
        formBody.append("&client_id=").append(encodeFormParameter(clientId));
        formBody.append("&client_secret=").append(encodeFormParameter(clientSecret));

        post.setEntity(new StringEntity(formBody.toString(), StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Accept", "application/json");

        try (CloseableHttpResponse response = getHttpClient().execute(post)) {
            int status = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (status >= 200 && status < 300) {
                JSONObject json = new JSONObject(body);

                // Extract access token
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

                logger.info("Successfully refreshed PayCor OAuth2 token. Expires in: {} seconds", expiresIn);
            } else {
                throw new IOException("Failed to refresh PayCor OAuth2 token. Status: " + status + ", Response: " + body);
            }
        }
    }

    /**
     * URL-encodes form parameters for application/x-www-form-urlencoded content type.
     * @param value the parameter value to encode
     * @return the encoded parameter value
     */
    private String encodeFormParameter(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.warn("Error encoding form parameter, returning original value", e);
            return value;
        }
    }
}
