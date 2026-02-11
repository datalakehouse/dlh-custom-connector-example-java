- Open Table Authentication Link : https://docs.opentable.com/?_gl=1*fcgo8*_gcl_au*MTQ2MjgyMjA5LjE3NjU4Mjk4Mjk.#ce3a8f78-70b7-480f-8902-51219c6d0ae5  
  - OAuth 2

#### Environment and OAuth Base URL

- **Pre-production:**[`https://oauth-pp.opentable.com`](https://oauth-pp.opentable.com/)
- **Production:**[`https://oauth.opentable.com`](https://oauth.opentable.com/)

#### Access Token URI

- **URI:**`{{base-url}}/api/v2/oauth/token?grant_type=client_credentials`

### Submitting Client Credentials

Client credentials are submitted in the Authorization header as defined in the OAuth specification. Follow these steps:

- Concatenate client ID and client secret using a “:” and base64 encode the result.

- Set the header`Authorization: Basic`


**Note:**For OAuth token POST requests, include the Content-Length header even when the request body is empty. Set Content-Length to 0. No update is required for GET requests for OAuth tokens.


```
curl --location -g '{{oauth-url}}/api/v2/oauth/token?grant_type=client_credentials' \
--header 'Authorization: Basic' \
--header 'Content-Type: application/json'
```

Response:
```
{
  "access_token": "v2-10cec40e-7dce-4049-8a95-5cc90df6bd00",
  "scope": "DEFAULT",
  "token_type": "Bearer",
  "expires_in": 1989434
}
```

There is another authentication  related info
https://docs.opentable.com/?_gl=1*fcgo8*_gcl_au*MTQ2MjgyMjA5LjE3NjU4Mjk4Mjk.#8959c9cb-560d-4058-8686-4a1c47390740

There we will need Rid which we need to pass
in some apis path param and in some apis in query params


Currently we are only able to Verify the data for only the Guests and Reservations

Other entities are having the following issues at now

CRM_TAGS  - Required Partner Id in the URL PATH
DIRECTORY_RESTAURANTS , BOOKING_AVAILABILITY, BOOKING_AVAILABILITY_METADATA, BOOKING_ACTIVE_EXPERIENCES
```
{"errors":[{"message":"Permission denied"}],"requestid":"94c75dc6-d69a-4757-abd8-ecf8f7489e96"}
```

REVIEWS_DATA, REVIEWS_SUMMARY, POS_RESTAURANTS, SYNC_ORDERS --- 403 in response body 


