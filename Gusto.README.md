# Gusto Connector

TODO:
In GustoConstants.java file we need to update the base URL according to the environment.

We are having PAY_PERIODS entity which is only supported for delta sync.

**Sample Request Structure for delta sync:**
```
{
    "connectorName": "GUSTO_CONNECTOR",
    "connectorType": "REST",
    "csvRowLimit": 500,
    "threadPoolSize": 10,
    "outputPath": "/path/to/output/directory",
    "outputType": "CSV",
    "accessToken": "your_gusto_access_token",
    "lastSyncDate": "2025-11-17"
}
```

In Gusto Connector currently we are extracting the following entities:
- COMPANY
- COMPANY_ADMINS
- COMPANY_LOCATIONS
- PAY_SCHEDULES
- PAY_PERIODS
- PAYROLLS
- COMPANY_BENEFITS
- BENEFITS
- DEPARTMENTS
- TIMESHEETS
- EMPLOYEES
- EMPLOYEE_HOME_ADDRESSES
- EMPLOYEE_WORK_ADDRESSES
- EMPLOYEE_TERMINATIONS
- EMPLOYEE_REHIRE
- JOBS 
- JOBS_COMPENSATIONS
- EMPLOYEE_BENEFITS
- GARNISHMENTS
- CONTRACTORS
