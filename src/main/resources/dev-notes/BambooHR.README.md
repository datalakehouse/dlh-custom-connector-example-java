# BambooHR Connector

Bamboo HR Has Developer Portal 
Developer Portal Link : https://developers.bamboohr.com/
API Documentation : https://documentation.bamboohr.com/docs/getting-started

I have Set currently enabled all apis with read scopes in the developer portal, 
Scopes enabled :
```angular2html
application job_opening benefit company:administration company:info company_file employee employee:dependent employee:file goal tasks offline_access public.integration training user scheduling:employee_schedule scheduling:schedules scheduling:shifts time_off time_tracking time_tracking:breaks field
```

Account Creation
- Created New Account with the following URL : https://www.bamboohr.com/signup/
- It will automatically generate Demo/sample data with the new account

Bamboo HR is supporting OAuth2.0 Authentication with authorization_code method 
Authentication URL 
```
https://<domain>.bamboohr.com/authorize.php?response_type=code&client_id=developer_portal-41e35d93d6c1e34f5db0fbdcafcb40fbcb58a1b5&state=xyz123&scope=application%20job_opening%20benefit%20company%3Aadministration%20company%3Ainfo%20company_file%20data_cleaner%20error_management%20employee%20employee%3Aassets%20employee%3Acompensation%20employee%3Acontact%20employee%3Acustom_fields%20employee%3Acustom_fields_encrypted%20employee%3Ademographic%20employee%3Adependent%20employee%3Adependent%3Assn%20employee%3Aeducation%20employee%3Aemergency_contacts%20employee%3Afile%20employee%3Aidentification%20employee%3Ajob%20employee%3Amanagement%20employee%3Aname%20employee%3Apayroll%20employee%3Aphoto%20employee%3Aproviders%20employee%3Aproviders%3Apayroll%20employee%3Avaccination%20employee_directory%20goal%20sensitive_employee%3Aaddress%20sensitive_employee%3Acreditcards%20sensitive_employee%3Aprotected_info%20tasks%20access_level%20benchmarking%3Acompensation%20field%20gridlets%20offline_access%20public.integration%20training%20user%20user%3Amanagement%20payroll%20payroll%3Alegal_entities%20payroll%3Aretirements%20report%20scheduling%3Aemployee_schedule%20scheduling%3Aschedules%20scheduling%3Ashifts%20time_off%20time_tracking%20time_tracking%3Abreaks%0A&redirect_uri=https%3A%2F%2Flocalhost%3A3000%2Fdashboard%2Foauth-redirect&request=authorize
```

After Authentication we need to do Authorization to get Access and Refresh Token 
```
curl --location 'https://<domain>.bamboohr.com/token.php?request=token' \
--header 'Content-Type: application/json' \
--data '{
   "client_secret": "<secret>",
   "client_id": "<client_id>",
   "code": "3d3eeb10cccc4c125185c2fa6e58593a9b855b0c",
   "grant_type": "authorization_code",
   "redirect_uri": "<redirect or callback url"
}'
```


# BambooHR Connector Summary

## 📊 Overview

**Total Entities:** 26
**Delta Sync Entities:** 0
**Full Sync Entities:** 26

## 🔐 Authentication

**Type:** OAuth 2.0
**Token URL:**  `https://<domain>.bamboohr.com/token.php?request=token`
**Flow:**  OAuth 2.0 Authorization Code Grant supported via BambooHR Developer Portal
**Alternative Methods:** Basic Authentication, API Key via OAuth tokens

## 📦 Response Structure

**Global Pattern:** Responses typically return JSON objects with root level keys representing the entity or data array, e.g., {"employees": [...]} or {"timeOffRecords": [...]}. Single object endpoints return the object directly without wrapping.

## 📄 Pagination

**Global Strategy:** Pagination is generally handled via 'page' and 'perPage' query parameters; some endpoints do not support pagination and return full datasets.

## ⏱️ Rate Limits

**Global Limit:** Rate limits are not explicitly documented publicly; enforcement is per API key with typical SaaS limits (e.g., hundreds of requests per minute).

## 📋 Entities

### All Entities

APPLICANT_STATUSES, BENEFIT_COVERAGES, BENEFIT_DEDUCTION_TYPES, COMPANY_BENEFITS, COMPANY_FILE_CATEGORIES, COMPANY_FILES, COMPANY_INFORMATION, COMPANY_LOCATIONS, STATES, COUNTRIES, EMPLOYEE_BENEFITS, EMPLOYEE_DEPENDENTS, EMPLOYEE_FILE_CATEGORIES, EMPLOYEE_TRAINING_TYPES, EMPLOYEE_TRAININGS, EMPLOYEES, GOALS, JOB_APPLICATIONS, JOB_SUMMARY, MEMBER_BENEFITS, MILESTONES, TIME_OFF_POLICIES, TIME_OFF_RECORDS, TIME_OFF_REQUESTS, TIME_SHEET_ENTRIES, USERS
### ❌ Full Sync Only

APPLICANT_STATUSES, BENEFIT_COVERAGES, BENEFIT_DEDUCTION_TYPES, COMPANY_BENEFITS, COMPANY_FILE_CATEGORIES, COMPANY_FILES, COMPANY_INFORMATION, COMPANY_LOCATIONS, STATES, COUNTRIES, EMPLOYEE_BENEFITS, EMPLOYEE_DEPENDENTS, EMPLOYEE_FILE_CATEGORIES, EMPLOYEE_TRAINING_TYPES, EMPLOYEE_TRAININGS, EMPLOYEES, GOALS, JOB_APPLICATIONS, JOB_SUMMARY, MEMBER_BENEFITS, MILESTONES, TIME_OFF_POLICIES, TIME_OFF_RECORDS, TIME_OFF_REQUESTS, TIME_SHEET_ENTRIES, USERS

### 🟢 Record Status Columns

**Column Name: `status`**

APPLICANT_STATUSES, EMPLOYEES, JOB_APPLICATIONS, JOB_SUMMARY, TIME_OFF_REQUESTS, USERS

**Column Name: `enabled`**
APPLICANT_STATUSES

**Column Name: `manageable`**
APPLICANT_STATUSES

## ✨ Key Features

- API supports field-level metadata retrieval for customization
- No explicit delta sync (timestamp-based filtering) parameters documented
- Authentication via Bearer token;  OAuth2 supported Via Developers Portal
- Documentation is fragmented; some entities undocumented or only accessible via private API
- Batch operations and webhooks are not documented publicly



=> We are not able to extract following Entities : EMPLOYEE_BENEFITS, GOALS, TIME_SHEET_ENTRIES
 Reasons are attached with the Test Report File 

=> Historical Sync Entities : Member Benefits , Time off requests, Time off Records 
