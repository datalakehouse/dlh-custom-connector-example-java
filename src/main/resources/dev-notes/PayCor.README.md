
- Paycor is Providing Developers portal and here is Account Creation Guide URL :
  https://developers-sandbox.paycor.com/guides#developer-portal-account-creation

- Having Sandbox Environment
- 
- APIs are having the Rate Limits 1000 calls Per minute per application then will respond 429

- Authentication : OAuth Supported

- Api Documentation is having two versions of public api only some api are having the version 2 rest all apis to be used from version 1
  Version 1 Swagger : https://developers-sandbox.paycor.com/try?version=1
  Version 2 Swagger : https://developers-sandbox.paycor.com/try?version=2

- Not found any difference documentation or details between version 1 and version 2 but after comparing the responses of Get Employees responses found that some fields are removed and some are added
    - ex : v1  is having alternateEmployeeNumber, badgeNumber but in v2 these fields not there and added workCountryCode, workerCategory, lastName2
- So we have like whichever entity is having latest v2 response we have taken that and for rest of the apis or entities we have taken from v1

- v2 Entities
    - Employees
    - Onboarding Employees
    - punches
    - Timeoff accruals
    - departments
    - work locations

we need to check with the actual data, according to the api document IDs https://developers.paycor.com/explore?tag=Legal%2520Entity#section/IDs
- ClientId = LegalEntityId
- TenantId = CompanyId

- API is having the pagination and Paycor Pagination is based on continuationToken then are providing in the response, Its passed on URL parameters

I believe we need to first fetch the Legal Entities first by the token and then according to the legal entities id we need to fetch other data and there is endpoint for tenants also

Authentication : https://developers-sandbox.paycor.com/guides#authorization
Search for `Authorization Code Flow` where the authentication apis are mentioned

https://developers-sandbox.paycor.com/guides#security-connections-tab
Our APIs flow through an API gateway and each caller is required to have a subscription key.
APIm Subscription Key we can get from the Developers Portal Application ->  Security Connections Tab

The APIm Subscription Key should be included on every call to our APIs by following either one of these methods:

- Via Header as: Ocp-Apim-Subscription-Key: <Application APIm Subscription Key> //preffered
- Via URL query string as: ?subscription-key=<Application APIm Subscription Key>


There are two different baseurl based on the environment,
Sandbox : https://apis-sandbox.paycor.com
Production : https://apis.paycor.com

Authorization :
https://developers-sandbox.paycor.com/guides#authorization-code-standard-application

Developer Account Application Creation:
https://developers-sandbox.paycor.com/guides#managing-applications
Applications -> Add Application -> Account Type (Standard Application), Application Name
Update Redirect URI 
-> In Data Access we need to enable scopes for the application and then we can use the client id and secret to generate the token and then we can call the APIs
-> After setting up the scopes/data access in General Tab the Scope Version Identifier is Generated we will need that 
-> Also from Security Connections Tab we can get the APIm Subscription Key which is required for calling the APIs

Account Type Differences: https://developers-sandbox.paycor.com/guides#application-types

Scopes for Verified Entities:
```text
View Legal Entity Work Locations
View Employee Taxes
View Legal Entity Deductions
View I9 Verification
View Employee Deductions Information
View Legal Entity Departments Information
View Legal Entity Persons
View Legal Entity Services
View Legal Entity Pay Groups
View Legal Entity Manager Profiles
View Employee Custom Fields
View Legal Entity Earnings Information & Amounts
View Certification Information for Legal Entity
View Certification Organizations for Legal Entity
Get Reason Codes by Legal Entity ID
View Legal Entity Time Off Plans
View Activity Types by Legal Entity
View Legal Entity Employees
View Employee Earnings Information
View Legal Entity Time Off Types
View Employee Direct Deposit Information by Employee Id
View Legal Entity Basic Information
View Legal Entity Taxes
View Legal Entity Payroll Processing Data
View Legal Entity Onboarding Employees
View Employee Timeoff Accruals by Legal Entity Id
View Employee Time Card Punches By Legal Entity Id
View Employee HSA Account Information
View Legal Entity Employees Identifying Data
View Employee Payrates
View Paystub Information
View Employee Time Card Hours By Legal Entity Id
Get Assignment of an Employee to Time Off Plans
View Employee Pay Schedule
View Employee Policy Group Data by EmployeeId
```

Scopes for Not Verified Entities:
```text
View ATS Accounts By Legal Entity Id
View ATS Account jobs by ATS Account ID
View Certification Information for Employee
Get Employee Pay Stub Document Link
View Tenant Work Locations
View Tenant Job Titles
Get Missed Punch Requests by Legal Entity Id
View Employee Punch Profile Data by EmployeeId
View Legal Entity Work Sites
Get Payroll Business Entity Custom Field Values By Legal Entity
View Labor Categories by Legal Entity Id
View Labor Codes by Legal Entity Id
View Employee Benefit Classification By Employee Id
View Employee Hours
Get Unpaid Employee Payrates By Employee Id
View Legal Entity Scheduling Jobs
View scheduling shifts
View Legal Entity Forecasting Data
View Legal Entity Job Titles
View Labor Profiles
View Employee On Demand Pay Deduction By Legal Entity Id ----
View Paydata Information
View Pay Schedule Information
View Legal Entity Schedule Groups
View Employee Pay Items by Legal Entity ID
View Employee Punches by EmployeeId
View Legal Entity Unassigned Punches
View Employee Hours by EmployeeId
```

Scopes for No Data Entities:
```text
View Retirement Data
View Timeoff Requests by Legal Entity Ids
``` 

Authentication Endpoint:
Endpoints:
Sandbox Authentication Endpoint : https://hcm-demo.paycor.com/AppActivation/Authorize
Production Authentication Endpoint : https://hcm.paycor.com/AppActivation/Authorize

```text
curl --location 'https://hcm-demo.paycor.com/AppActivation/Authorize?client_id={{clientId}}&scope={{Scope Identifier}}%20offline_access&response_type=code&redirect_uri=https%3A%2F%2Flocalhost%3A3000%2Fdashboard%2Foauth-redirect&subscription-key={{Subscription Key}}&code_challenge=R1D5EuJbDTLtzda07fHhbLL76XJQY3lRK7C0BqIQ_o4&code_challenge_method=s256&code_verifier=XvI4T17p7jkUlpuQRaOh8lHL3Ap5fn2wuofOEPoPcYitQT815Si6x8th6NF0CiS3CAe70beI2NRWZyqC3xg7C-ToLw0FjOSi88dSWfUpDKMoNDSd0GkOm0sI-nm2-YoY'
```

After successful authentication, we will get the code in the redirect URL and then we can exchange that code for access token and refresh token by calling the below endpoint

Endpoints:
Sandbox Authentication Endpoint : https://apis-sandbox.paycor.com/sts/v1/common/token
Production Authentication Endpoint : https://apis.paycor.com/sts/v1/common/token

```text
curl --location 'https://apis-sandbox.paycor.com/sts/v1/common/token?subscription-key={{Subscription Key}}' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'client_id={{clientId}}' \
--data-urlencode 'client_secret={{clientSecret}}' \
--data-urlencode 'code=77f652bf15259c3353e682303238cb96a4f9983b4c92e983ddcd2ac3c232ff4e' \
--data-urlencode 'code_verifier=XvI4T17p7jkUlpuQRaOh8lHL3Ap5fn2wuofOEPoPcYitQT815Si6x8th6NF0CiS3CAe70beI2NRWZyqC3xg7C-ToLw0FjOSi88dSWfUpDKMoNDSd0GkOm0sI-nm2-YoY' \
--data-urlencode 'redirect_uri=https://localhost:3000/dashboard/oauth-redirect'
```

After Successful Authentication, we will get the access token and refresh token in the response and then we can use that access token to call the APIs
```text
{
    "access_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImIyYTY3ZDhjNDFiM2YyMGZmMzI0M2M3MmFiZmZkZjg5IiwidHlwIjoiSldUIn0.eyJpc3MiOiJodHRwczovL2FwaS1kZW1vLnBheWNvci5jb20vc3RzL3YyL2NvbW1vbiIsImV4cCI6MTc3MzI5MzM4OCwiaWF0IjoxNzczMjkxNTg4LCJuYmYiOjE3NzMyODc5ODgsImNsaWVudF9pZCI6ImMxOGJmZjZjOTA5N2FkZTdmNzI2Iiwic2tleSI6IkFGODJ6QVpMYWVLeHc0RzFOYUpLbFVEV3pPdXdSVFM0NS1aTEZpMi1BY1UiLCJzaWQiOiIzZGEzMjYzNC1hZjBhLTRiM2YtOTkzYy1mNzFmNGRiMThiNTYiLCJhdXRoX3RpbWUiOjE3NzMyNzQyMjIsInZlciI6MSwic3ViIjoiNzM5ZDdmYWItNTE5YS00Njk0LWFiMWEtMGYwZDM0MjQ4MmIwIiwiaWRwIjozLCJwYXljb3JfdXNlcmlkZW50aXR5IjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwicGF5Y29yX3RpbWVvdXQiOjI0MCwicGF5Y29yX2J2IjoiLWRINWhjN0p0a0stMVNhOGJmNDlPdyIsInBheWNvcl9wcml2aWxlZ2VzIjoiSDRzSUFBQUFBQUFBQ2xXU081SWNNQWhFN3pJeGdRQWhnYk10MThaMjd2SkpwdWJ1ZnIySlp3TythaUZvOUh4OGZQeDgvUGlUMDVuN3J4SCtJdnh5ZnVQNFN2TTE1cjRSYkdEaklQanBDUEVPODFvV2ZwQ0xqQVc0OUxZc2JDL2JxMjA3bGpxYi9ONWorMktucmNpWHAxWGdiN2NxYkYrck9YWnk3RkQvM0xITGU3ZkNlaUY1ckdkczFyS3B0QUhyNjZwSmRlaWJVRDI1WDZsbUNtK05NWmZlRjQyclc1UkdDaUFSeW1tYVNFRzJjaG9zTm5mamZLblMzRUJ5Y1RkRlM2b0t6S0V1cDlrTkhVZDhRSVNYR0tva1Y2cFNHMXh0Y0dmaEhlYjFxdzZ1R0wzSndWV0JPeFJvUVZvUDlhSlVRNkMzY0MzcU80R001aGh0YUtEVUorbDVJTmJudE1XQ2cxZ2xkVmpJZ3Vsd3FnVHpTN21XSmUrRzFuWFpPVC9oK2NMSVBsZzdIMkFITk9wandPbDd5STMza0YyL2g2encrOTN2YUZINVArYkZULzdhOC9YNkJ3ZjR3WmVPQWdBQSIsInBheWNvcl96aXAiOiJwYXljb3JfcHJpdmlsZWdlcyIsIm5hbWUiOiJETEggRGV2IEludGVncmF0aW9uIiwiZ2l2ZW5fbmFtZSI6IkRMSCBEZXYiLCJmYW1pbHlfbmFtZSI6IkludGVncmF0aW9uIiwiZW1haWwiOiJJVC1JbnRlZ3JhdGlvbnNTdXBwb3J0RExAcGF5Y29yLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJpcGFkZHIiOiIxMTUuMjQ2LjE4LjIxMCIsImp0aSI6IjhkZDZhNmZhOGE2NmRjM2Q3YzRmNTAwYzRhMTUyMzc0Iiwic2NvcGUiOlsiZTVhYmE5ZWUzYjFjZjExMTlhNDk2MDQ1YmQ0OTJiZjQiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIiwibWZhIl0sInBheWNvcl9jbGllbnRzIjp7IkMiOlt7IkNsaWVudElkIjozOTgzMzQsIkNvbXBhbnlJZCI6NDIxNTA0LCJFbXBsb3llZVVpZCI6bnVsbCwiQ29udGFjdFVpZCI6bnVsbCwiQ3BhQ29udGFjdFVpZCI6bnVsbH1dfSwiYXVkIjoiYzE4YmZmNmM5MDk3YWRlN2Y3MjYifQ.Kk_SEwfkLJrePvxb8NA_iheyEWRaIXxfbKP84EJVYvXdesN576e5n3SQa_6gvxcfFARhUhHtwcUsk14m7BpAXIuau5wUSELK0-jJMmOuwUx_YHcqbq7QlD3dj90PYKrKMz7Gy3iyL_a6rbq8I7YIiP_GNI3d-lHSnfai-jMzTtx-j8cdMYzjvMTYR6tenrwm7jb6-ajUcQYYDVWe7-jpC0qNKs2cYs3CXKF4pIW6GlI9z9UCpdJu_EbBhIoZ-0R-ER2ib0jLSXdELuYHrfUwWC6KCio-NtAmU67Y2bH1h_SDYAwr2Q39e--g2qrK1qb8qHVoaNF9DG-NSHzHi6XC-g",
    "expires_in": 1800,
    "token_type": "Bearer",
    "refresh_token": "a2a8986540b1f2afe2e1604c90b47177dacb37b5d355bf1f2a99d21557cd1665"
}
```

We can only get the Historical Entities Data for 30 days due to Limitations of the API.





