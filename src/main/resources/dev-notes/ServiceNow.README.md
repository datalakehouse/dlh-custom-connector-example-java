
### Pre Authentication Setup (Client Side or Demo Account Side)

Created application in Service Studio

System OAuth  ->  Application Registry -> Create New Entry
-> Client Credentials
-> Account Scope (Need to Look for the Table API Only Scope (Haven't Tested) )

### Authentication

API: https://dev198111.service-now.com/oauth_token.do

Content-Type: application/x-www-form-urlencoded

```
grant_type: "client_credentials"
client_id: "29b4c1281217438284324322f3077187"
client_secret: "Nq^1kD{*m98kMPSa9ds)eUkdz^k]jy`F"
```

- We need to only extract table endpoints  where we can pass different table names and  get different data


When we compare our entities with FT then the followings are missing
- cmn_skill
- service_offering
- process_set
- state_binding



Summary of the connector (Generated)
# ServiceNow Connector Summary

*Generated: 2026-01-30 17:47:21*

## 📊 Overview

**Total Entities:** 35
**Delta Sync Entities:** 35
**Full Sync Entities:** 0
## 🔐 Authentication

**Type:** OAuth 2.0 🎯
**Token URL:** `https://<instance>.service-now.com/oauth_token.do`

**Flow:** OAuth 2.0 Authorization Code Grant and Client Credentials supported via ServiceNow OAuth Provider

**Scopes:** `useraccount, useraccount.email, useraccount.roles, api`

**Alternative Methods:** Basic Authentication, API Key via OAuth tokens

## 📦 Response Structure


**Global Pattern:** All responses are JSON objects containing 'result' field which is an array of records for GET list queries or a single record object for GET single record requests

## 📄 Pagination

**Global Strategy:** Offset-based pagination using 'sysparm_limit' and 'sysparm_offset' query parameters; supports 'sysparm_query' for filter conditions

## ⏱️ Rate Limits

**Global Limit:** ServiceNow API rate limits are tenant-specific and generally recommended 5000 requests per hour; actual limits controlled per instance

## 📋 Entities

### All Entities

CMN_DEPARTMENT, PROBLEM, SC_TASK, CONTRACT_SLA, SC_CATALOG, CHANGE_TASK, SC_REQUEST, SYS_USER_ROLE, SYS_DB_OBJECT, INCIDENT, CMN_SCHEDULE, CMN_LOCATION, CMDB_CI_APPL, CMDB_CI_SERVICE, SYS_APPROVAL_GROUP, ALM_ASSET, SC_CAT_ITEM_DELIVERY_TASK, PROBLEM_TASK, SC_CATEGORY, SYS_APPROVAL_APPROVER, SC_REQ_ITEM, TASK_SLA, SYS_AUDIT, SYS_USERS, SC_CAT_ITEM_DELIVERY_PLAN, TASK, SYS_ROLE, CHANGE_REQUEST, CMDB_CI_SERVER, CORE_COMPANY, SYS_USER_GROUP, CMN_COST_CENTER, CMDB_CI, SYS_USER_GROUP_MEMBER, BASE_CONFIGURATION_ITEM

### ✅ Delta Sync Supported

CMN_DEPARTMENT, PROBLEM, SC_TASK, CONTRACT_SLA, SC_CATALOG, CHANGE_TASK, SC_REQUEST, SYS_USER_ROLE, SYS_DB_OBJECT, INCIDENT, CMN_SCHEDULE, CMN_LOCATION, CMDB_CI_APPL, CMDB_CI_SERVICE, SYS_APPROVAL_GROUP, ALM_ASSET, SC_CAT_ITEM_DELIVERY_TASK, PROBLEM_TASK, SC_CATEGORY, SYS_APPROVAL_APPROVER, SC_REQ_ITEM, TASK_SLA, SYS_AUDIT, SYS_USERS, SC_CAT_ITEM_DELIVERY_PLAN, TASK, SYS_ROLE, CHANGE_REQUEST, CMDB_CI_SERVER, CORE_COMPANY, SYS_USER_GROUP, CMN_COST_CENTER, CMDB_CI, SYS_USER_GROUP_MEMBER, BASE_CONFIGURATION_ITEM

### 🟢 Record Status Columns

**Column Name: `active`**

CMN_DEPARTMENT, TASK_SLA, SYS_USERS, SC_CAT_ITEM_DELIVERY_PLAN, TASK, CHANGE_REQUEST, SYS_USER_GROUP, SC_TASK, PROBLEM, CONTRACT_SLA, SC_CATALOG, CHANGE_TASK, SC_REQUEST, INCIDENT, SYS_APPROVAL_GROUP, PROBLEM_TASK, SC_CATEGORY


## ✨ Key Features

- Supports OData v2 protocol for some endpoints
- Extensive Table API supporting CRUD on any ServiceNow table
- Support for Query Parameters with encoded query syntax in 'sysparm_query'
- Batch API available via Import Sets
- Webhook support via IntegrationHub
- Supports delta sync via 'sysparm_query' with encoded queries filtering on 'sys_updated_on' or 'sys_created_on' fields
- Comprehensive role-based access control integrated via OAuth scopes and user roles