package com.lightspeedretail.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightspeedretail.common.GustoConstants;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GustoService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public record TokenInfoResponse(
            String scope,
            Resource resource,
            @JsonProperty("resource_owner") ResourceOwner resourceOwner
    ) {
        public record Resource(String type, String uuid) {}
        public record ResourceOwner(String type, String uuid) {}
    }

    public static String getCompanyId(String accessToken, String environment) throws IOException, InterruptedException {

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(GustoConstants.getGustoBaseUrl(environment) + GustoConstants.TOKEN_INFO_URI))
                    .header(GustoConstants.API_VERSION_HEADER, GustoConstants.API_VERSION)
                    .header("accept", "application/json")
                    .header("authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("Failed to fetch token info. Status code: " + response.statusCode());
            }

            TokenInfoResponse tokenInfo = objectMapper.readValue(response.body(), TokenInfoResponse.class);

            if (tokenInfo.resource() == null || tokenInfo.resource().uuid() == null) {
                throw new IOException("Company UUID not found in token info response");
            }

            return tokenInfo.resource().uuid();
        }
    }

}
