package io.datalakehouse.restconnection;

import io.datalakehouse.connectors.core.AuthConfig;
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

    public FreshServiceRestConnectionType(String baseUrl, String username, String password) {
        super(baseUrl, null, AuthConfig.Presets.basic(username, password));
    }

}
