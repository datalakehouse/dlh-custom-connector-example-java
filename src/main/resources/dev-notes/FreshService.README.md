- Looked for the Authentication model and found that freshworks is also providing the Oauth Authentication for the Fresh services and also For Fresh works
- Fresh service is also providing the API Key which is never expiring and we can get it from User Profile
  From developers portal :
- I have created one oauth Credentials with the following scopes

```
freshservice.agents.roles.view freshservice.tickets.fields.manage freshservice.tickets.view freshservice.tickets.time_entries.view freshservice.tickets.edit freshservice.tickets.tasks.view freshservice.tickets.conversations.view freshservice.problems.view freshservice.problems.fields.view freshservice.problems.notes.view freshservice.problems.time_entries.view freshservice.problems.tasks.view freshservice.changes.view freshservice.changes.edit freshservice.changes.notes.view freshservice.changes.time_entries.view freshservice.changes.tasks.view freshservice.releases.view freshservice.releases.notes.view freshservice.releases.time_entries.view freshservice.releases.tasks.view freshservice.workspaces.view freshservice.requesters.view freshservice.requesters.fields.view freshservice.agents.manage freshservice.agents.fields.view freshservice.agentgroups.manage freshservice.locations.view freshservice.products.view freshservice.vendors.view freshservice.assets.view freshservice.purchase_orders.view freshservice.assets.manage freshservice.contract_types.view freshservice.contracts.view freshservice.departments.view freshservice.departments.fields.view freshservice.business_hours.view freshservice.projects.view freshservice.projects.fields.view freshservice.projects.manage freshservice.solutions.view freshservice.service_catalog.edit freshservice.announcements.view freshservice.onboarding_requests.view freshservice.offboarding_requests.view freshservice.oncall.view freshservice.objects.manage freshservice.pir_template.manage freshservice.sla_policies.view freshservice.canned_responses.view
```


OAUTH:

For Authentication followed this document : https://developers.freshworks.com/docs/app-sdk/v3.0/service_asset/oauth-implementation-in-apps/ofoauth-in-external-apps/

Firstly we have this api so that i have got code from authorize the scope and organization

https://niyantrasllc.myfreshworks.com//org/oauth/v2/authorize?response_type=code&state=xyz123&client_id=fw_ext_925659012783064550&scope=freshservice.tickets.view&redirect_uri=https://localhost:3000/dashboard/oauth-redirect

Code : rPC0dHpsxrAp-TKpJVGi80PSdCJEB1Dv0E7GtERKhbehUNHY826rKnjjW0gt-JepxlKQrCioBlHSt-MbGBWybeFaOS44SKO8P1dE02O038qrIDbP_rCcn8oePNc4yhbs


Then with this URL Generated refresh token and access Token

https://niyantrasllc.myfreshworks.com/org/oauth/v2/token

with x-www-form-urlencoded params of the following
- code
- grant_type = authorization_code
- redirect_uri

Got Access Token and Tried the tickets api and its working

- we have extracted the responses with json files and created basic ddl with script and then validated
- Implemented Refresh token logic with the following curl
- Authentication token is Basic with Base64 encrypted with client id and client secret
```
curl --location 'https://niyantrasllc.myfreshworks.com/org/oauth/v2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic ZndfZXh0XzkyNTY1OTAxMjc4MzA2NDU1MDpxMTJGN01TblNmTEdqU2p2Z3NKWldVMzFXOWpjUWxTdA==' \
--header 'Cookie: __cf_bm=Sgpx71HXUSbTTbcOPshmWb_ql.ZjHYxQjtnoKpVECMA-1767097061-1.0.1.1-sZwDG9fwynf7fgN7NWnobvqsOMQcJYtwwI_6gwt6KVMVEmZ8vE_PK1HMOeesjk3cGQna7eYodZiwVO487slB3ZSrJ_4lJdvZyOOlnBk0s.s' \
--data-urlencode 'code=4UnnEyoofh6ASQ7hoDuK6b19LeBhIl8Qq7FQe1-_LJtEJCN-qg29jhRvk5tvXKLpESmSp-uI4nFK8X-iRdDbCtyuraibM7fcHhmn-sWTGlD3fxokvx5fFnx5VQGv0YKW' \
--data-urlencode 'grant_type=refresh_token' \
--data-urlencode 'redirect_uri=https://localhost:3000/dashboard/oauth-redirect' \
--data-urlencode 'refresh_token=QQCWy4TDZBalVcIIdQ3T_1X_ZtXPfUoC_jHMdZRLDVcmkjzzp6G779iIpCOsI4y0oL1KDbBBJwr48YYw69-QGbGOq9TqXQLJVJ6qH8KBk61OoKKhLgLnzmlZsIMjN8Qf'
```

- Identified the scopes with view Privileges

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




