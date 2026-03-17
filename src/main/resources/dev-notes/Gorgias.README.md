
### Developers Account Creation 

Link : https://partners.gorgias.com/signup

By Signing Up this form we are able to create Developers Account for the Gorgias
After Signup We need to Activate the account by the mail we receive 

Then We are able to login to Developers Account.

### Creating Demo Account 

Link : https://www.gorgias.com/get-started-trial

By Filling out the form we will receive on-boarding link in Mail 
After filling out On-boarding form we will receive Account Activation Link and We are able to Create an Account 

We Can Get the Subdomain from the URL 

### Creating Application 

Link : https://partners.gorgias.com/login

Logged in with Account Credentials  -> My Apps  -> Create Application filling out the Basic Details

After Creating the application we need to edit that application to get the client id and client secret 

(Redirect URI doesn't allowing the localhost url so we have used postman redirect URL)


### Authentication and Authorizations

- There are Two types of Authentication 
	Link : https://developers.gorgias.com/docs/access-tokens-api-keys
	- oauth 2
	- API Keys (Settings -> Advanced -> REST API -> Create API Key)


- OAuth 2

```
https://{{sub-domain}}.gorgias.com/oauth/authorize?response_type=code&client_id=69ae95712086297dee235651&state=xyz123&scope=users%3Aread%20account%3Aread%20customers%3Aread%20tickets%3Aread%20custom_fields%3Aread%20jobs%3Aread%20macros%3Aread%20rules%3Aread%20satisfaction_survey%3Aread%20statistics%3Aread%20tags%3Aread%20offline&redirect_uri=https%3A%2F%2Foauth.pstmn.io%2Fv1%2Fcallback
```

```
POST /oauth/token HTTP/1.1

Content-Type: application/x-www-form-urlencoded

Authorization: Basic NjlhZTk1NzEyMDg2Mjk3ZGVlMjM1NjUxOnB1MG5wZ3c2ZGNsNjhpZHJjYWZuY3Yydzlib2d5azhlcTVycHR1a2o= (Base64(ClientId:ClientSecret))

User-Agent: PostmanRuntime/7.49.1
Accept: */*
Cache-Control: no-cache
Postman-Token: e00bc364-3371-40bb-bc28-0bcd526eef1d
Host: {subdomain}.gorgias.com
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Content-Length: 141

grant_type=authorization_code&code=Ly93UZlXV0T1fprQQCymahmBC9CRYdK8dGr1ch6hJHC3Ehr1&redirect_uri=https%3A%2F%2Foauth.pstmn.io%2Fv1%2Fcallback
```


In return we are getting access token and refresh Token 

```
{
	"token_type":"Bearer",
"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImhpS0NIZ00yeXRWUUsyd2ZoTGVuOEdxLXZ2aDYzNkloSmQwSzZVTVMwb0EifQ.eyJpc3MiOiJodHRwczovL25peWFudHJhcy1kZW1vLmdvcmdpYXMuY29tL2lkcCIsImF1ZCI6Imh0dHBzOi8vbml5YW50cmFzLWRlbW8uZ29yZ2lhcy5jb20vYXBpIiwic3ViIjoiMjAyODI3LTM0NzUzMjE3OSIsImlhdCI6MTc3MzA0OTc4NiwiZXhwIjoxNzczMTM2MTg2LCJhenAiOiI2OWFlOTU3MTIwODYyOTdkZWUyMzU2NTEiLCJqdGkiOiJ5bkVQMEFqNSIsInNjb3BlIjoidXNlcnM6cmVhZCBhY2NvdW50OnJlYWQgY3VzdG9tZXJzOnJlYWQgdGlja2V0czpyZWFkIGN1c3RvbV9maWVsZHM6cmVhZCBqb2JzOnJlYWQgbWFjcm9zOnJlYWQgcnVsZXM6cmVhZCBzYXRpc2ZhY3Rpb25fc3VydmV5OnJlYWQgc3RhdGlzdGljczpyZWFkIHRhZ3M6cmVhZCBvZmZsaW5lIiwiYXV0aF90aW1lIjoxNzczMDQyMzQ2fQ.IkiafA-pPzgG9WSHXxo64CEbNkoeAcGWYuVVmjti37EwhQA1XVCMqfQ79kAsv2S_U0GXOdeRzF3Zvr6quZ_H2bI_z8Wm-WhK0RtDibW0VGkyTgODKPO-KOxf1aIq-1oK_Y8DYRkSiwTf64YWT4dWAJNUiO6iiWotguxsv_MyMXQ_HAemgiGA2NaOx60dxBRg5r_Z5QhlzeVVBeKG2hD7iSkhw9AMhRFsyGEYr9Pxbm4R6iMQIcptv6Y6lIGwdCPApp26QFTSEhnOJ2TwwxW_cnWaoU0mUtauIvNvYiieRzw1CBLY9aa-9wyQmJsrTyDpTpQ66Fkhpv1c-s8jpbtNXw",
"expires_in":86400,
"refresh_token":"KpzJioTYI5rvYGQ9C9PB3q0hvOuoqaTLwh33JzVyi6qJrnIn",
"scope":"users:read account:read customers:read tickets:read custom_fields:read jobs:read macros:read rules:read satisfaction_survey:read statistics:read tags:read offline"
}
```


This connector Doesn't provide any filters to get the Historical Data or Delta Sync as of Now 

Scopes:
```
users:read account:read customers:read tickets:read custom_fields:read jobs:read macros:read rules:read satisfaction_survey:read tags:read offline integrations:read
```


