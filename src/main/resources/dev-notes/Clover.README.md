### Create Sandbox Account

- Info Doc : https://docs.clover.com/dev/docs/global-developer-platform-get-started
- Link : https://www.clover.com/global-developer-home/public/create-account
- Filled Signup form with Password and Setup the account name and all
- After Redirected to login screen and setup mfa for the account
- Redirected to dashboard

### Create New App

- From Global Developer Platform My Apps -> Create New App
  -> Select App Name and Countries this app will be available -> Select default Locale
-  What type of app do you want to create?
    - Private Application
- Select a Platform
    - Web

### Requested Permission
- Inventory Read
- Merchant Read
- Orders Read
- Payments Read
- Enable Online Payments  (We need to Test)


#### There is Limitation in Sandbox account we can only access the following Entities and set scopes for those entities only
- Inventory
- Merchant
- Orders
- Payments
#### Following Others  will be available in the Production Environment
- Customers
- Employees


### Authentication
Link : https://docs.clover.com/dev/docs/oauth-flows-in-clover#
Base Urls is Varies based on Environment

For Sandbox Environment :
(We need to switch to the Test Account to get Authorization from that Test Account as we are not provided by the Credentials of Test Merchant Account)
Authorization Code :  (We will get Merchant Id also which we will need to pass)

```
https://sandbox.dev.clover.com/oauth/v2/authorize?client_id=DPGJEW5FPMSXJ&redirect_uri=https://localhost:3000/dashboard/oauth-redirect&response_type=code&state=xyz456
```

Token Generation :
Client Id : App ID
Client Secret : APP Secret
```
curl --location 'https://apisandbox.dev.clover.com/oauth/v2/token' \
--header 'Content-Type: application/json' \
--data '{
    "client_id": "DPGJEW5FPMSXJ",
    "client_secret": "47a3a348-9e09-a6ba-6f44-df8ff4e905f0",
    "code": "d0a69fa0ae9f42829f6e94bfbaded026"
}'
```

This will Get us Access and Refresh Token

### 

Discount table need to look

Pagination and other rest api related Details : https://docs.clover.com/dev/docs/clover-rest-api-index

# Clover Connector Summary

---

## 📊 Overview

**Total Entities:** 36

## 🔐 Authentication

**Type:** OAuth2 🎯
**Token URL:** `https://apisandbox.dev.clover.com/oauth/v2/token`
**Prod Token URL:** `https://api.clover.com/oauth/v2/token`
**Flow:** OAuth 2.0 Authorization Code Grant flow with bearer tokens
**Scopes:** `MERCHANT_READ, INVENTORY_READ, EMPLOYEE_READ, ORDER_READ, PAYMENT_READ, CUSTOMERS_READ`
**Alternative Methods:** API Key

## 📦 Response Structure

**Global Pattern:** JSON responses typically wrap data in a root object, e.g., {"elements": [...], "metadata": {...}}

**Exceptions:**

- **AUTHORIZATIONS:** Returns a single authorization object without wrapping in elements array
- **MERCHANT_PROPERTIES:** Returns a flat key-value object
- **MERCHANTS_DEFAULT_SERVICE_CHARGE:** Returns a single object, not an array

## 📄 Pagination

**Global Strategy:** Offset-based pagination with 'limit' and 'offset' query parameters; response includes 'total' count and 'elements' array

**Exceptions:**

- **EMPLOYEE_SHIFTS:** Supports filtering by date range but uses offset/limit pagination
- **AUTHORIZATIONS:** No pagination, single object or limited result set

## ⏱️ Rate Limits

**Global Limit:** 1000 requests per minute per client ID

