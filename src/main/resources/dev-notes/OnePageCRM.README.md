Step 1:
- Looked for the Developer portal if they are providing

Authentication :
HTTP Basic Authentication  (Currently the Credentials attached in Tickets)
(User name and API Token key Generated in Apps and Integration > API > Configuration)
Details can be get from there

- Tested Credentials and Checked for the API

API Documentation :
Link : https://developer.onepagecrm.com/api/


Step 2:

Look into the api and create DDL Documentation
- Created ALL Json Files from the all responses and name is entity name
- Then run python script and ddl will be generated
- Compare with json and make required changes

Step 3 :

Give DDL path to Mermaid Er converter
- looked into and checked and added missing and corrections


Step 4 :
- Created Constants file for the Connector

Step 5 :
- Modified Controller and added to accept The API for this connector
- Added Connector name in Core Custom Constants

Step 6 :
- Implemented the One Page CRM Connector File
- Extended the Rest Connection Type for the Pagination Authentication

Step 7 :
- Looked for the Responses Structure and implemented code to extract the responses and map with the Headers and Entities

Step 8 :
- Enable one by one Entity and looked for the data and Tried and Tested

In this connector we have added Generic Pagination that enables feature to Custom Validation for next page and also Metadata we can supply

And we have also enabled generic Auth Bean so we can define Authentication Method and will be used by the Connector 

Note : One Page CRM is supporting Delta Sync in entities but as per the testingwe are passing params but the data is not returned according to modified Timestamp

We are able to extract below Entities:

ACTIONS                
CALLS                  
COMPANIES              
COMPANY_FIELDS         
CONTACTS               
CUSTOM_FIELDS          
DEAL_FIELD             
DEALS                  
FILTERS                
LEAD_SOURCES           
MEETINGS               
NOTES                  
NOTIFICATIONS          
PIPELINES              
PREDEFINED_ACTIONS     
PREDEFINED_ITEMS       
PREDEFINED_ITEM_GROUPS 
RELATIONSHIP_TYPES     
STATUSES               
USERS                  