API Documentation : https://api.freshservice.com/
Authentication : https://api.freshservice.com/#authentication

### Create Test Account

Link : https://www.freshworks.com/freshservice/lp/home/?tactic_id=5149054&utm_source=google-adwords&utm_medium=FS-Search-Brand-IND&utm_campaign=FS-Search-Brand-IND&utm_term=freshservice%20signup&device=c&matchtype=e&network=g&gclid=EAIaIQobChMIqNnP5OPdkgMVLm4PAh2InTQHEAAYASAAEgI-0vD_BwE&audience=kwd-666859515808&ad_id=716547021270&gad_source=1&gad_campaignid=16731826065

- After the Start Free trial we will be redirected to the Freshservice portal onboarding page
- After Onboarding we will be redirected to the Freshservice portal dashboard
- We will also receive link on the mail to activate the account and set the password for the account
- Then we need to create the API key for authentication and then we can start testing the APIs

- Looked for the Authentication model and found that freshworks is also providing the Oauth Authentication for the Fresh services and also For Fresh works
- Fresh service is also providing the API Key which is never expiring and we can get it from User Profile
  From User portal :

### Authentication : 

    - Fresh-desk is supporting the Basic Authentication With User name and password generated via the portal

      Profile Settings  -> View API Key  -> Code (This is Username)
      Password is `X`

if the API key is Disabled by the Admin Message showed then we need to go 
Admin -> Agents -> Admin Agent (User) -> Permissions -> Enable API key Flag

There is a difference in structure and fields between api returns the data and the api documentations.
Corrected Entities
- Roles


Problematic entities
- Cabs  --- No Scope Found in configuration and api is returning 403
- JOURNEY_REQUEST_ACTIVITIES --- No Scope Found in configuration
- JOURNEY_REQUESTS --- No Scope Found in configuration
- JOURNEYS_CONFIGS_DATA_FIELDS --- No Scope Found in configuration
- JOURNEYS_CONFIGS --- No Scope Found in configuration
- CHANGES_APPROVAL_GROUPS --- Need to Look for Scope
- CHANGES_APPROVALS --- Need to Look for Scope
- TASK NOTES  --- have two variables in url
- TASK TYPE FIELDS  --- have two variables in url
- SERVICE_CATALOG_SHARED_FIELDS ---- 403
- SOLUTION_ARTICLES   -  -------403
- SOLUTION_FOLDERS  - not able to map


- Onboarding Requests
 ```
 {

"code": "require_feature",

"message": "The Employee Onboarding feature(s) is/are not supported in your plan. Please upgrade your account to use it."

}
 ```
- offboarding Requests
```
{
    "code": "inaccessible_api",
    "message": "There are no active actors available."
}
```


- Need to verify After Data Comes
  CONTRACT_ATTACHMENTS (child of CONTRACTS)
  CSAT_RESPONSE
  PROBLEM_NOTES (child of PROBLEMS)
  PROBLEM_TASKS (child of PROBLEMS)
  PROBLEM_TIME_ENTRIES (child of PROBLEMS)
  RELEASES_NOTES (child of RELEASES)
  RELEASES_TASKS (child of RELEASES)
  RELEASES_TIME_ENTRIES 




