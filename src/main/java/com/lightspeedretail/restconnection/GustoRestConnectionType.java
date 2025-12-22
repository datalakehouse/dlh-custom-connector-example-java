package com.lightspeedretail.restconnection;

import io.datalakehouse.connectors.impl.RestConnectionType;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GustoRestConnectionType extends RestConnectionType {

    private static final String GRANT_TYPE = "refresh_token";
    private String refreshToken;
    private final String redirectUri;
    private final String authorizationCode;

    /**
     * Constructor for Gusto with OAuth2 support (refresh token flow).
     */
    public GustoRestConnectionType(String baseUrl, String accessToken,
                                   String clientId, String clientSecret, String refreshToken,
                                   String redirectUri, String authorizationCode) {
        super(baseUrl, accessToken, clientId, clientSecret, null, null);
        this.refreshToken = refreshToken;
        this.redirectUri = redirectUri;
        this.authorizationCode = authorizationCode;
    }

    /**
     * Constructor for Gusto with static Bearer token (no refresh).
     */
    public GustoRestConnectionType(String baseUrl, String accessToken) {
        super(baseUrl, accessToken);
        this.refreshToken = null;
        this.redirectUri = null;
        this.authorizationCode = null;
    }

    /**
     * Gusto-specific OAuth2 token refresh implementation.
     * Uses refresh_token grant type as per Gusto's OAuth2 specification.
     */
    @Override
    protected void refreshOAuth2Token() throws IOException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IOException("Refresh token is not available. Cannot refresh Gusto OAuth2 token.");
        }

        HttpPost post = new HttpPost(baseUrl + "/oauth/token");

        // Create JSON request body as per Gusto's API specification
        JSONObject requestBody = new JSONObject();
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("grant_type", GRANT_TYPE);
        requestBody.put("refresh_token", refreshToken);

        if (redirectUri != null && !redirectUri.isEmpty()) {
            requestBody.put("redirect_uri", redirectUri);
        }

        if (authorizationCode != null && !authorizationCode.isEmpty()) {
            requestBody.put("code", authorizationCode);
        }

        post.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/json");
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
                if (json.has("gta")) {
                    this.refreshToken = json.getString("refresh_token");
                }

                // Set token expiry (default to 1 hour if not provided, refresh 1 minute early)
                int expiresIn = json.optInt("expires_in", 3600);
                this.tokenExpiryTimeMillis = System.currentTimeMillis() + (expiresIn - 60) * 1000L;

                System.out.println("Successfully refreshed Gusto OAuth2 token. Expires in: " + expiresIn + " seconds");
            } else {
                throw new IOException("Failed to refresh Gusto OAuth2 token. Status: " + status + ", Response: " + body);
            }
        }
    }

    /**
     * Get the current refresh token.
     * @return current refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Update the refresh token manually if needed.
     * @param refreshToken new refresh token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
