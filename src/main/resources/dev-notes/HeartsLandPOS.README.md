There is no Authentication method I found with the credentials that provided in Ticket
like pkapi, skapi,  mid etc

There are two api urls based on the Environment
Sandbox : https://apis.sandbox.globalpay.com
Production : https://apis.globalpay.com

Created new Account for the developer in global Payments where there is Authentication information. Link : https://developer.globalpayments.com/user/b2c-login

Then after I have Created New App inside developers portal for the REST API  (Unified Payments)

After Creation of the new Application  Sandbox Environment is also created

For Authentication : Info Link:https://developer.globalpayments.com/api/definitions/access-tokens
```
curl --location 'https://apis.sandbox.globalpay.com/ucp/accesstoken' \
--header 'Content-type: application/json' \
--header 'X-GP-Version: 2021-03-22' \
--data '{
       "app_id" : "1EKMyldrMGNkh43LVHk3n4DYWzM69ufV",
       "nonce" : "2029-03-14T13:24:10.834Z",
       "secret" : "f432ac88a9e4d332dfc13500bd23b09e75eacdd63a25f8acf89915910cf520aaf8806a4cf8dcf80a1e73e24941611109d8ca9921887d004ab9806068842ebdf5",
       "grant_type" : "client_credentials"
}'
```

Where nonce is Unique Timestamp (Haven't Tested with unique String but i believe Its a random Unique String)
app_id : we can get from the application which we created in the developers portal
secret :
```json
// secret = SHA512(nonce + app_key)
```
I have generated from the simple java class
```
public class Sha512 {  
  
    public static void main(String[] args) throws Exception {  
        String nonce = "2029-03-14T13:24:10.834Z";  
        String appKey = "1CAG7Zb2FW5G23g0";  
  
        String input = nonce + appKey;  
  
        MessageDigest md = MessageDigest.getInstance("SHA-512");  
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));  
  
        String secret = bytesToHex(hashBytes);  
        System.out.println("secret = " + secret);  
    }  
  
    private static String bytesToHex(byte[] bytes) {  
        StringBuilder sb = new StringBuilder(bytes.length * 2);  
        for (byte b : bytes) {  
            sb.append(String.format("%02x", b));  
        }  
        return sb.toString();  
    }  
}
```

Common Header :
They are using `X-GP-Version` Header based Versioning
we have used `2021-03-22` Version

All Entities are Having Pagination

# HEARTSLAND_POS Connector Summary
## 📊 Overview

**Total Entities:** 11
**Delta Sync Entities:** 1
**Full Sync Entities:**

## 🔐 Authentication

**Token URL:** https://api.globalpayments.com/oauth2/token

## 📦 Response Structure

**Global Pattern:** All list responses are wrapped in Array at root level (e.g., "accounts": [...])

## 📄 Pagination

**Global Strategy:**  pagination with page and limit query parameters

**Exceptions:**
- **AUTHENTICATION:** No pagination as single resource creation

## 📋 Entities

### All Entities
LINKS, AUTHENTICATION, ORDERS, ACCOUNTS, TRANSACTION, PAYERS, ACTIONS, MERCHANTS, PAYMENT_METHODS, TRANSFERS, DISPUTES
### ✅ Delta Sync Supported
PAYMENT_METHODS

### 🟢 Record Status Columns

**Column Name: `status`**
LINKS, AUTHENTICATION, ORDERS, ACCOUNTS, TRANSACTION, MERCHANTS, PAYMENT_METHODS, TRANSFERS

## ✨ Key Features
- Supports OAuth2 client credentials for secure API access
- Cursor-based pagination for scalable data retrieval
- Webhook support for real-time updates on transactions and disputes
- Batch API endpoints for bulk operations on orders and transactions
- Comprehensive dispute management with stages and reason codes
- Rich payment method details including 3DS authentication data
- Parent-child entity relationships allowing hierarchical data queries