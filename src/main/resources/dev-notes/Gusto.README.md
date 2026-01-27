
- Forked Latest API Version Postman Collection from this link: https://www.postman.com/gusto-api/gusto-embedded/overview

- Created Demo Company in Gusto Developer Portal and then looked for the Authentication Mechanism and retrieved Client ID and Secret from developer portal

- Tried to authenticate by following the link :https://docs.gusto.com/app-integrations/docs/oauth2
    - https://api.gusto-demo.com/oauth/authorize?response_type=code&client_id=BaqEC39FOmKBDz56UOcaxKGBz7UWSTAu9fpLYWb_eGQ&redirect_uri=http://localhost:3000/dashboard/oauth-redirect&scope=employees:read payrolls:read&state=xyz123
    - After Authorization Received Code in URL param
    - Then by the Next Request Specification Generated Access Token by grant type authorization code  https://api.gusto-demo.com/oauth/token with following details
        - ```
		  {   
		  "client_id": "BaqEC39FOmKBDz56UOcaxKGBz7UWSTAu9fpLYWb_eGQ", 
		  "client_secret": "secret",   
		  "code": "QlonVT2x4MraSwH464XXmmtFpICJ8N6t_O5CENuC3ys",   
		  "grant_type": "authorization_code",   
		  "redirect_uri": "http://localhost:3000/dashboard/oauth-redirect" 
		  }
		  ```
    - Looked into the api structure and tried the API `/v1/token_info` as other apis will need companyId extracted from this response
    - Verified that sample data is created by exploring the platform
    - Tried Company Details endpoint in postman by providing company id and its worked and tried couple of other endpoint also

- Looked into the documentation and started creating the ddl file with the python script
    - By Creating one Json file for the entity and converting to ddl by script and coppying to the final script. and validating the structure

- Creating Mermaid file by using the Ddl to er script and provided api key  and extracted
    - Looked into the unmapped tables and added their foreign keys

- Looked into the sdk and example implementation to Understand the implementation

-  Worked on process by copying the example and modified according to gusto connector
    - Created Entity Headers for the basic entities in constant file
    - Tried to fetch few entites that is dependent to the token details endpoint `/v1/token_info`
    - But there is a dependency so and tried dependency map as to extract the details but doesn't worked the token and company details endpoints were executing in the real time
- There is one api requirement to pass `"X-Gusto-API-Version" =  "2025-06-15"` as custom header
    - For testing purpose Hardcoded and added into the API  then Implemented changes to Provide Custom Headers in SDK Request Configurations

- Created service to extract the company details id and pass it into the some basic entities
    - API Path converted to receive the parameter

- Logic worked but there was no data so modified mapping data logic according to the response structure

- Created Metadata yml file from the ddl file

- Added entity names and headers for the remaining entities
- One by One added Dependency Map for the entites and testing and comparing results .


- Get an employee's custom fields API is providing response like
```
{
  "custom_fields": [
    {
      "id": "ee515986-f3ca-49da-b576-2691b95262f9",
      "company_custom_field_id": "ea7e5d57-6abb-47d7-b654-347c142886c0",
      "name": "employee_level",
      "description": "Employee Level",
      "type": "text",
      "value": "2",
      "selection_options": null
    }
}
```

with the entity name and it was not mapping.
Added Logic for the nested json node to go inside the json and map

Faced issue of code is like getting [[]] two time array and then object and not mapping with the entity found by debugging the same scenario

Refactored logic of Mapping to go inside the child entity or nested entity

- Get Payrolls for company response
```
  {
    "uuid": "3601a7a2-0562-4e4c-9559-20886658daac",
    "payroll_uuid": "3601a7a2-0562-4e4c-9559-20886658daac",
    "company_uuid": "b43e6012-bf6c-4752-b67b-5c8000595e0e",
    "payroll_status_meta": {
      "cancellable": false,
      "expected_check_date": "2025-06-08",
      "initial_check_date": "2025-06-27",
      "expected_debit_time": "2025-06-12T23:00:00Z",
      "payroll_late": false,
      "initial_debit_cutoff_time": "2025-06-12T23:00:00Z"
    },
    "off_cycle": false,
    "auto_pilot": false,
    "processed": true,
    "processed_date": "2025-06-11",
    "calculated_at": "2025-06-11T19:40:51Z",
    "pay_period": {
      "start_date": "2025-05-20",
      "end_date": "2025-06-04",
      "pay_schedule_uuid": "ded21d08-02d6-41cb-b211-8d8ca02f1c6a"
    },
    "check_date": "2025-06-08",
    "external": false,
    "payroll_deadline": "2025-06-12T23:00:00Z",
    "company_taxes": [],
    "created_at": "2025-06-11T19:40:51Z",
    "partner_owned_disbursement": null
  }
```

so the pay_period_start_date not mapped with the entity header as nested entity.
Converted logic to Combine the Json nodes and try to find headers logic
Then enhanced to that like
- breakdown with each UNDERSCORE
- try Combination of clubbing the different different word sets by UNDERSCORE
- (As in one entity we are having PAYROLL {
"PAYROLL_UUID"}) for this scenario implemented logic

- During Testing Identified that The dependent entity was having URL Template and If like
  we have 10 employees and addresses are depend on employee ID then
  10 times the same employee id is passing

- ISSUE : Template URL : `v1/employees/{employee_id}/work_addresses`
    - At First time template is updated with the employee_id but then after the place holder is replaced with values next time its treating
      `v1/employees/1/work_addresses` as a template url so its not updated


- Delta Sync
    - There is no endpoint that provides lastupdated or kind of query param support
    - There was only two entities that is kind of providing support for the startdate or kind of
    - Looked for put api so that get an idea that its updating or not
    - Pay Periods API is having the query param of Start Date which can be used for delta pull as its not updated
    - Timesheets API is providing feature of pulling data based on before after query param but its for CreatedAt field so that wont work

- Implemented Delta Sync by adding one more variable `lastSyncDate` and added call to fetch the delta entries

- Refactored code

```
https://api.gusto-demo.com/oauth/token
{
"client_id": "BaqEC39FOmKBDz56UOcaxKGBz7UWSTAu9fpLYWb_eGQ",
"client_secret": "secret",
"grant_type": "refresh_token",
"refresh_token": "SJHG_SBxSpHmcnmY31l5FgudIEkTXOBeRwbUqslK67M",
"redirect_uri": "http://localhost:3000/dashboard/oauth-redirect",
"code" : "XYwfjY_rVWgpbkY24-kwn1U_KogXi6CVU4p3UUvCCxE"
}
```

For refresh token Looked into the sdk to implement
- Implemented changes to extend the refresh Oauth token logic
- Implemented the Service logic to refresh token


Enhanced Metadata file to correct the details and enhance with adding details according to the sample