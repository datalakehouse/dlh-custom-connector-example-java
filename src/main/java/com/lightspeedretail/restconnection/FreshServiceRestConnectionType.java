package com.lightspeedretail.restconnection;

import io.datalakehouse.connectors.impl.RestConnectionType;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class FreshServiceRestConnectionType extends RestConnectionType {

    Logger logger = Logger.getLogger(FreshServiceRestConnectionType.class.getName());

    private static final String GRANT_TYPE = "refresh_token";
    private String refreshToken;
    private final String authUrl;
    private final String redirectUri;
    private final String authorizationCode;

    /**
     * Constructor for FreshService with OAuth2 support (refresh token flow).
     */
    public FreshServiceRestConnectionType(String baseUrl, String accessToken,
            String clientId, String clientSecret, String refreshToken,
            String redirectUri, String authorizationCode, String authUrl) {
        super(baseUrl, accessToken, clientId, clientSecret, null, null);
        this.authUrl = authUrl;
        this.refreshToken = refreshToken;
        this.redirectUri = redirectUri;
        this.authorizationCode = authorizationCode;
    }

    /**
     * FreshService-specific OAuth2 token refresh implementation.
     * Uses refresh_token grant type with form-encoded data and Basic Authentication.
     */
    @Override
    protected void refreshOAuth2Token() throws IOException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IOException("Refresh token is not available. Cannot refresh FreshService OAuth2 token.");
        }

        HttpPost post = new HttpPost(authUrl);

        // Build form-encoded request body as per FreshService API specification
        StringBuilder formData = new StringBuilder();
        formData.append("grant_type=").append(GRANT_TYPE);
        formData.append("&refresh_token=").append(URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));

        if (redirectUri != null && !redirectUri.isEmpty()) {
            formData.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, StandardCharsets.UTF_8));
        }

        if (authorizationCode != null && !authorizationCode.isEmpty()) {
            formData.append("&code=").append(URLEncoder.encode(authorizationCode, StandardCharsets.UTF_8));
        }

        post.setEntity(new StringEntity(formData.toString(), StandardCharsets.UTF_8));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("Accept", "application/json");

        // Use Basic Authentication with base64 encoded clientId:clientSecret
        if (clientId != null && clientSecret != null) {
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            post.setHeader("Authorization", "Basic " + encodedCredentials);
        }

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
                if (json.has(GRANT_TYPE)) {
                    this.refreshToken = json.getString(GRANT_TYPE);
                }

                int expiresIn = json.optInt("expires_in", 1740);
                this.tokenExpiryTimeMillis = System.currentTimeMillis() + (expiresIn - 60) * 1000L;

                logger.info("Successfully refreshed FreshService OAuth2 token. Expires in: " + expiresIn + " seconds");
            } else {
                throw new IOException("Failed to refresh FreshService OAuth2 token. Status: " + status + ", Response: " + body);
            }
        }
    }

}
