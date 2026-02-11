package io.datalakehouse.common;

import java.util.List;
import java.util.Map;

public class HeartsLandPosConstants {

    public static final String CONNECTOR_NAME = "HEARTS_LAND_POS";
    public static final String[] ID_HEADERS = {"ID"};
    public static final String X_GP_VERSION_HEADER = "X-GP-Version";
    public static final String X_GP_VERSION_HEADER_VALUE = "2021-03-22";
    public static final long DEFAULT_HISTORICAL_DAYS = 90L;
    public static final String HISTORICAL_SYNC_QUERY_PARAM = "from_time_created";


    // Pagination constants
    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "page_size";
    public static final int MAX_PAGE_SIZE = 100;
    public static final String PAGING = "paging";
    public static final String CURRENT_PAGE_SIZE = "current_page_size";
    public static final String TOTAL_RECORD_COUNT = "total_record_count";

    public static String getHeartsLandPOSBaseUrl(String environment) {
        if (environment == null || environment.isBlank()) {
            return "https://apis.sandbox.globalpay.com"; // default
        }

        return switch (environment.trim().toUpperCase()) {
            case "PROD", "PRODUCTION" -> "https://apis.globalpay.com";
            case "DEMO", "SANDBOX" -> "https://apis.sandbox.globalpay.com";
            default -> throw new IllegalArgumentException("Unsupported HeartsLand environment: " + environment);
        };
    }


    public static final class HeartsLandPosEntityNames {

        public static final String LINKS = "LINKS";
        public static final String AUTHENTICATION = "AUTHENTICATION";
        public static final String ORDERS = "ORDERS";
        public static final String ACCOUNTS = "ACCOUNTS";
        public static final String TRANSACTION = "TRANSACTION";
        public static final String PAYERS = "PAYERS";
        public static final String ACTIONS = "ACTIONS";
        public static final String MERCHANTS = "MERCHANTS";
        public static final String PAYMENT_METHODS = "PAYMENT_METHODS";
        public static final String TRANSFERS = "TRANSFERS";
        public static final String DISPUTES = "DISPUTES";

        private HeartsLandPosEntityNames() {}
    }

    public static final class HeartsLandPosHeaders {

        public static final String[] LINKS = {"ID", "LINKS_URL", "STATUS", "LINKS_TYPE", "USAGE_MODE", "USAGE_LIMIT", "REFERENCE", "NAME", "DESCRIPTION", "SHIPPABLE", "SHIPPING_AMOUNT", "USAGE_COUNT", "VIEWED_COUNT", "EXPIRATION_DATE", "IMAGES", "NOTIFICATIONS_RETURN_URL", "NOTIFICATIONS_STATUS_URL", "NOTIFICATIONS_CANCEL_URL", "TRANSACTIONS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] AUTHENTICATION = {"ID", "TIME_CREATED", "STATUS", "CHANNEL", "AMOUNT", "CURRENCY", "COUNTRY", "SOURCE", "PAYMENT_METHOD_RESULT", "PAYMENT_METHOD_MESSAGE", "PAYMENT_METHOD_ENTRY_MODE", "PAYMENT_METHOD_FINGERPRINT", "PAYMENT_METHOD_FINGERPRINT_PRESENCE_INDICATOR", "PAYMENT_METHOD_NAME", "PAYMENT_METHOD_CARD_FUNDING", "PAYMENT_METHOD_CARD_BRAND", "PAYMENT_METHOD_CARD_AUTHCODE", "PAYMENT_METHOD_CARD_BRAND_REFERENCE", "PAYMENT_METHOD_CARD_MASKED_NUMBER_FIRST6LAST4", "PAYMENT_METHOD_CARD_CVV_INDICATOR", "PAYMENT_METHOD_CARD_CVV_RESULT", "PAYMENT_METHOD_CARD_AVS_ADDRESS_RESULT", "PAYMENT_METHOD_CARD_AVS_POSTAL_CODE_RESULT", "PAYMENT_METHOD_THREE_DS_ACS_TRANS_REF", "PAYMENT_METHOD_THREE_DS_DS_TRANS_REF", "PAYMENT_METHOD_THREE_DS_SERVER_TRANS_REF", "PAYMENT_METHOD_THREE_DS_LIABILITY_SHIFT", "PAYMENT_METHOD_THREE_DS_VALUE", "PAYMENT_METHOD_THREE_DS_ECI", "PAYMENT_METHOD_THREE_DS_STATUS", "PAYMENT_METHOD_THREE_DS_STATUS_REASON", "PAYMENT_METHOD_THREE_DS_MESSAGE_CATEGORY", "PAYMENT_METHOD_THREE_DS_MESSAGE_VERSION", "PAYMENT_METHOD_THREE_DS_CHALLENGE_STATUS", "ACTION_CREATE_ID", "SYSTEM_MID", "SYSTEM_TID", "SYSTEM_NAME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ORDERS = {"ID", "MERCHANT_ID", "MERCHANT_NAME", "STATUS", "ACCOUNT_ID", "ACCOUNT_NAME", "TIME_CREATED", "TIME_LAST_UPDATED", "REFERENCE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ACCOUNTS = {"ID", "ACCOUNTS_TYPE", "NAME", "STATUS", "PERMISSIONS", "COUNTRIES", "CHANNELS", "CURRENCIES", "ACTIONS", "PAYMENT_METHODS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TRANSACTION = {"ID", "TIME_CREATED", "STATUS", "TRANSACTION_TYPE", "CHANNEL", "AMOUNT", "CURRENCY", "USER_REFERENCE", "REFERENCE", "BATCH_ID", "COUNTRY", "PARENT_RESOURCE_ID", "CREATE_ACTION_ID", "PAYMENT_METHOD_NAME", "PAYMENT_METHOD_RESULT", "PAYMENT_METHOD_MESSAGE", "PAYMENT_METHOD_ENTRY_MODE", "PAYMENT_METHOD_CARD", "PAYMENT_METHOD_FINGERPRINT", "PAYMENT_METHOD_FINGERPRINT_PRESENCE_INDICATOR", "PAYMENT_METHOD_AUTHENTICATION_ID", "PAYMENT_METHOD_AUTHENTICATION_THREE_DS", "PAYMENT_METHOD_BNPL_PROVIDER", "INSTALLMENT_PROGRAM", "INSTALLMENT_MODE", "INSTALLMENT_COUNT", "INSTALLMENT_GRACE_PERIOD_COUNT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAYERS = {"ID", "FIRST_NAME", "LAST_NAME", "REFERENCE", "EMAIL", "PAYERS_LANGUAGE", "PAYMENT_METHODS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ACTIONS = {"ID", "ACTIONS_TYPE", "TIME_CREATED", "ACTIONS_RESOURCE", "RESOURCE_ID", "RESOURCE_STATUS", "VERSION", "HTTP_RESPONSE_CODE", "RESPONSE_CODE", "APP_ID", "APP_NAME", "MERCHANT_NAME", "ACCOUNT_NAME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MERCHANTS = {"ID", "NAME", "STATUS", "ORGANIZATION", "INDUSTRY", "INTEGRATOR", "HIERARCHY", "PRIMARY_MID", "REGION", "LINKS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAYMENT_METHODS = {"ID", "TIME_CREATED", "STATUS", "REFERENCE", "CARD_NUMBER_LAST4", "CARD_BRAND", "CARD_EXPIRY_MONTH", "CARD_EXPIRY_YEAR", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TRANSFERS = {"ID", "TIME_CREATED", "TIME_LAST_UPDATED", "TRANSFERS_TYPE", "STATUS", "AMOUNT", "USABLE_BALANCE_MODE", "MERCHANT_ID", "MERCHANT_NAME", "ACCOUNT__ID", "ACCOUNT_NAME", "RECIPIENT_MERCHANT_ID", "RECIPIENT_MERCHANT_NAME", "RECIPIENT_ACCOUNT_NAME", "RECIPIENT_ACCOUNT_ID", "REFERENCE", "DESCRIPTION", "PROVIDER_RESULT", "PROVIDER_MESSAGE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] DISPUTES = {"ID", "STATUS", "STAGE", "STAGE_TIME_CREATED", "AMOUNT", "CURRENCY", "REASON_CODE", "REASON_DESCRIPTION", "ORDER_REFERENCE", "TIME_TO_RESPOND_BY", "RESULT", "ACQUIRER_CODE", "SYSTEM_MID", "SYSTEM_TID", "SYSTEM_HIERARCHY", "SYSTEM_NAME", "SYSTEM_DBA", "LAST_ADJUSTMENT_AMOUNT", "LAST_ADJUSTMENT_CURRENCY", "LAST_ADJUSTMENT_FUNDING", "TRANSACTION_ORDER_REFERENCE", "PAYMENT_METHOD_CARD_NUMBER", "PAYMENT_METHOD_CARD_ARN", "PAYMENT_METHOD_CARD_BRAND", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};

        private HeartsLandPosHeaders() {}
    }

    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.ofEntries(
            Map.entry(HeartsLandPosEntityNames.MERCHANTS, List.of(HeartsLandPosEntityNames.ORDERS))
    );

    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(HeartsLandPosEntityNames.LINKS, HeartsLandPosHeaders.LINKS),
            Map.entry(HeartsLandPosEntityNames.AUTHENTICATION, HeartsLandPosHeaders.AUTHENTICATION),
            Map.entry(HeartsLandPosEntityNames.ORDERS, HeartsLandPosHeaders.ORDERS),
            Map.entry(HeartsLandPosEntityNames.ACCOUNTS, HeartsLandPosHeaders.ACCOUNTS),
            Map.entry(HeartsLandPosEntityNames.TRANSACTION, HeartsLandPosHeaders.TRANSACTION),
            Map.entry(HeartsLandPosEntityNames.PAYERS, HeartsLandPosHeaders.PAYERS),
            Map.entry(HeartsLandPosEntityNames.ACTIONS, HeartsLandPosHeaders.ACTIONS),
            Map.entry(HeartsLandPosEntityNames.MERCHANTS, HeartsLandPosHeaders.MERCHANTS),
            Map.entry(HeartsLandPosEntityNames.PAYMENT_METHODS, HeartsLandPosHeaders.PAYMENT_METHODS),
            Map.entry(HeartsLandPosEntityNames.TRANSFERS, HeartsLandPosHeaders.TRANSFERS),
            Map.entry(HeartsLandPosEntityNames.DISPUTES, HeartsLandPosHeaders.DISPUTES)
    );

    // Entities that support pagination
    public static final List<String> PAGINATION_ENTITIES = List.of(
            HeartsLandPosEntityNames.LINKS,
            HeartsLandPosEntityNames.AUTHENTICATION,
            HeartsLandPosEntityNames.ACCOUNTS,
            HeartsLandPosEntityNames.TRANSACTION,
            HeartsLandPosEntityNames.PAYERS,
            HeartsLandPosEntityNames.ACTIONS,
            HeartsLandPosEntityNames.PAYMENT_METHODS,
            HeartsLandPosEntityNames.TRANSFERS,
            HeartsLandPosEntityNames.DISPUTES
    );

    public static final Map<String, String> ENTITY_API_PATH_MAP = Map.ofEntries(
            Map.entry(HeartsLandPosEntityNames.LINKS, "ucp/links"),
            Map.entry(HeartsLandPosEntityNames.AUTHENTICATION, "ucp/authentications"),
            Map.entry(HeartsLandPosEntityNames.ORDERS, "ucp/merchants/{merchants_id}/orders"),
            Map.entry(HeartsLandPosEntityNames.ACCOUNTS, "ucp/accounts"),
            Map.entry(HeartsLandPosEntityNames.TRANSACTION, "ucp/transactions"),
            Map.entry(HeartsLandPosEntityNames.PAYERS, "ucp/payers"),
            Map.entry(HeartsLandPosEntityNames.ACTIONS, "ucp/actions"),
            Map.entry(HeartsLandPosEntityNames.MERCHANTS, "ucp/merchants"),
            Map.entry(HeartsLandPosEntityNames.PAYMENT_METHODS, "ucp/payment-methods"),
            Map.entry(HeartsLandPosEntityNames.TRANSFERS, "ucp/transfers"),
            Map.entry(HeartsLandPosEntityNames.DISPUTES, "ucp/disputes")
    );

    public static final List<String> excludedEntities = List.of();

    public static final Map<String, List<String>> CUSTOM_QUERY_PARAM_BY_ENTITY = Map.ofEntries(
            Map.entry(HeartsLandPosEntityNames.LINKS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.AUTHENTICATION, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.ORDERS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.ACCOUNTS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.TRANSACTION, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.PAYERS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.ACTIONS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.MERCHANTS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.PAYMENT_METHODS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.TRANSFERS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.DISPUTES, List.of("from_stage_time_created"))
    );

    public static final Map<String, List<String>> DELTA_QUERY_PARAMS_BY_ENTITY = Map.ofEntries(
            Map.entry(HeartsLandPosEntityNames.LINKS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.AUTHENTICATION, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.ORDERS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.ACCOUNTS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.TRANSACTION, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.PAYERS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.ACTIONS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.MERCHANTS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.PAYMENT_METHODS, List.of("from_time_last_updated")),
            Map.entry(HeartsLandPosEntityNames.TRANSFERS, List.of(HISTORICAL_SYNC_QUERY_PARAM)),
            Map.entry(HeartsLandPosEntityNames.DISPUTES, List.of("from_stage_time_created"))
    );

    private HeartsLandPosConstants() {}
}