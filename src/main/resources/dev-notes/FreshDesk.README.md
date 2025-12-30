Looked for the Developer portal if they are providing and found it As they are providing the developers Portal.

- Authentication :
    - Fresh-desk is supporting the Basic Authentication With User name and password generated via the portal

      Profile Settings  -> View API Key  -> Code (This is Username)
      Password is `X`


- API Documentation : https://developers.freshdesk.com/api/#introduction
- UI of API Documentation is not in sync with Side navigation Bar

- Tested Multiple apis with the credentials via Postman
- Freshdesk is not providing open Postman Collection to fork and test

- Created ALL JSON Files from the all responses and name is entity name
- Then run python script and ddl will be generated
- Compare with JSON and make required changes

- Delta Supported Entities (According to the Documentation)
    - Tickets
    - Contacts
    - Companies

Tried the Delta Sync As this connector supports `updated_since` with some other entities but its responding with error
Tried 
- Admin Groups 
  - Response :
  ```
  {
    "description": "Validation failed",
    "errors": [
        {
            "field": "updated_since",
            "message": "Unexpected/invalid field in request",
            "code": "invalid_field"
        }
    ]
    }
  ```
  
- Agents
- Email Configs

Able To Extract the Entities:
- Accounts
- Admin Groups
- Agents
- Business Hours
- Canned response Folders
- Canned Responses
- Companies
- Company Fields
- Contact Fields
- Contacts
- Discussion Category Forums
- Discussion Forum Topics
- Discussion Topic Comments
- Discussion Categories
- Email Configs
- Email Mail boxes
- Groups
- Groups Agents
- Products
- Roles
- Scenario Automation
- Skills
- SLA Policies
- Solutions Categories
- Solution Satisfactory Ratings (There is No Data Fetched as not found to add data)
- Surveys
- Tickets
- Ticket Fields
- Ticket  Forms
- Ticket Conversations
- Tickets Satisfaction Ratings
- Tickets Time Entries
- Time Entries

Not Able To Extract the Entity :

- Tickets Summary
    - the API is Responding with Not Found Tried with Tickets ID

- Custom Object Schemas
    - The API is responding with Different Response Structure so In future we need to add the Extraction Logic

- Custom Object Schema Records
    - This entity is dependent to the Custom Object Schemas  so when we will enable at that time we will enable this also and having the different schema response structure with entity name as json node and inside that we have the actual response

- Ticket Field Sections
    - API is responding `invalid_ticket_field` with Bad Request and i have Tried to pass Tickets id and Tickets fields ID
    - In Tickets Id we are receiving the 404 Not Found

- We are having two entity which are not mapped with their parent entities as this requires entity id aware processing in sdk
    - Group Agents
    - Canned Responses
    