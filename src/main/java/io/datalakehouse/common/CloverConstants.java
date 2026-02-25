package io.datalakehouse.common;

import java.util.List;
import java.util.Map;

public class CloverConstants {

    public static final String CONNECTOR_NAME = "CLOVER";
    public static final String[] ID_HEADERS = {"ID"};
    public static final long DEFAULT_HISTORICAL_DAYS = 90L;
    public static final String DELTA_HEADER = "filter=modifiedTime>";

    public static String getCloverBaseUrl(String environment) {
        if (environment == null || environment.isBlank()) {
            return "https://apisandbox.dev.clover.com"; // default
        }

        return switch (environment.trim().toUpperCase()) {
            case "PROD", "PRODUCTION" -> "https://api.clover.com";
            case "DEMO", "SANDBOX" -> "https://apisandbox.dev.clover.com";
            default -> throw new IllegalArgumentException("Unsupported Clover environment: " + environment);
        };
    }

    // Pagination constants
    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";
    public static final int MAX_PAGE_SIZE = 500;
    public static final int DEFAULT_PAGE_SIZE = 300;
    public static final String ELEMENTS = "elements";

    public static final class CloverEntityNames {

        public static final String TAX_RATES = "TAX_RATES";
        public static final String TAG_INVENTORY_ITEMS = "TAG_INVENTORY_ITEMS";
        public static final String EMPLOYEE_SHIFTS = "EMPLOYEE_SHIFTS";
        public static final String ORDER_LINE_ITEMS = "ORDER_LINE_ITEMS";
        public static final String SYSTEM_ORDER_TYPES = "SYSTEM_ORDER_TYPES";
        public static final String PAYMENTS = "PAYMENTS";
        public static final String CREDIT_REFUNDS = "CREDIT_REFUNDS";
        public static final String ORDERS = "ORDERS";
        public static final String ATTRIBUTES = "ATTRIBUTES";
        public static final String ITEM_GROUPS = "ITEM_GROUPS";
        public static final String INVENTORY_ITEMS = "INVENTORY_ITEMS";
        public static final String DISCOUNTS = "DISCOUNTS";
        public static final String MERCHANTS = "MERCHANTS";
        public static final String TAGS = "TAGS";
        public static final String DEVICES = "DEVICES";
        public static final String MERCHANT_PROPERTIES = "MERCHANT_PROPERTIES";
        public static final String MERCHANT_ROLES = "MERCHANT_ROLES";
        public static final String MERCHANT_GATEWAY = "MERCHANT_GATEWAY";
        public static final String EMPLOYEES = "EMPLOYEES";
        public static final String TIP_SUGGESTIONS = "TIP_SUGGESTIONS";
        public static final String MODIFIER_GROUP_ITEMS = "MODIFIER_GROUP_ITEMS";
        public static final String TENDERS = "TENDERS";
        public static final String MODIFIER_GROUPS = "MODIFIER_GROUPS";
        public static final String MODIFIERS = "MODIFIERS";
        public static final String MERCHANTS_DEFAULT_SERVICE_CHARGE = "MERCHANTS_DEFAULT_SERVICE_CHARGE";
        public static final String AUTHORIZATIONS = "AUTHORIZATIONS";
        public static final String CASH_EVENTS = "CASH_EVENTS";
        public static final String REFUNDS = "REFUNDS";
        public static final String CATEGORIES = "CATEGORIES";
        public static final String VOIDED_LINE_ITEMS = "VOIDED_LINE_ITEMS";
        public static final String OPTIONS = "OPTIONS";
        public static final String CUSTOMER = "CUSTOMER";
        public static final String ORDER_TYPES = "ORDER_TYPES";
        public static final String CATEGORY_ITEMS = "CATEGORY_ITEMS";
        public static final String ITEM_STOCKS = "ITEM_STOCKS";

        private CloverEntityNames() {}
    }

    public static final class CloverHeaders {

        public static final String[] TAX_RATES = {"ID", "NAME", "RATE", "IS_DEFAULT", "TAX_AMOUNT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TAG_INVENTORY_ITEMS = {"TAG_ID", "ITEM_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_SHIFTS = {"ID", "EMPLOYEE_ID", "CASH_TIPS_COLLECTED", "SERVER_BANKING", "IN_TIME", "OVERRIDE_IN_TIME", "OVERRIDE_IN_EMPLOYEE_ID", "OUT_TIME", "OVERRIDE_OUT_TIME", "OVERRIDE_OUT_EMPLOYEE_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ORDER_LINE_ITEMS = {"ID", "ORDER_REF_ID", "COLOR_CODE", "NAME", "PRICE", "PRICE_WITH_MODIFIERS", "PRICE_WITH_MODIFIERS_AND_ITEM_AND_ORDER_DISCOUNTS", "UNIT_QTY", "ITEM_CODE", "NOTE", "PRINTED", "EXCHANGED_LINE_ITEM_ID", "USER_DATA", "CREATED_TIME", "ORDER_CLIENT_CREATED_TIME", "IS_AGE_RESTRICTED", "AGE_RESTRICTED_TYPE", "MINIMUM_AGE", "DISCOUNTS", "ORDER_LEVEL_DISCOUNTS", "DISCOUNT_AMOUNT", "ORDER_LEVEL_DISCOUNT_AMOUNT", "EXCHANGED", "MODIFICATIONS", "TAGS", "REFUNDED", "REFUND_ID", "IS_REVENUE", "TAX_RATES", "PAYMENTS", "REVENUE_AMOUNT", "QUANTITY_SOLD", "PRINT_GROUP_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAYMENTS = {"ID", "ORDER_ID", "DEVICE_ID", "TENDER_ID", "AMOUNT", "TIP_AMOUNT", "TAX_AMOUNT", "CASHBACK_AMOUNT", "CASH_TENDERED", "EXTERNAL_PAYMENT_ID", "EMPLOYEE_ID", "CREATED_TIME", "CLIENT_CREATED_TIME", "GATEWAY_PROCESSING_TIME", "MODIFIED_TIME", "OFFLINE", "RESULT", "CARD_TRANSACTION", "SERVICE_CHARGE_ID", "SERVICE_CHARGE_NAME", "SERVICE_CHARGE_AMOUNT", "ATTRIBUTES", "ADDITIONAL_CHARGES", "TAX_RATES", "NOTE", "LINE_ITEM_PAYMENTS", "AUTHORIZATION_ID", "VOID_PAYMENT_REF_ID", "VOID_REASON", "VOID_REASON_DETAILS", "DCC_INFO", "TRANSACTION_SETTINGS", "GERMAN_INFO", "APP_TRACKING", "CASH_ADVANCE_EXTRA", "TRANSACTION_INFO", "SIGNATURE_DISCLAIMER", "EXTERNAL_REFERENCE_ID", "MERCHANT_ID", "INCREMENTS", "PURCHASE_CARD_L2", "PURCHASE_CARD_L3", "OCEAN_GATEWAY_INFO", "TERMINAL_MANAGEMENT_COMPONENTS", "EMI_INFO", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CREDIT_REFUNDS = {"ID", "ORDER_REF_ID", "DEVICE_ID", "CREATED_TIME", "CLIENT_CREATED_TIME", "CREDIT_ID", "EMPLOYEE_ID", "GERMAN_INFO", "APP_TRACKING", "TRANSACTION_INFO", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ORDERS = {"ID", "CURRENCY", "CUSTOMERS", "EMPLOYEE_ID", "TOTAL", "EXTERNAL_REFERENCE_ID", "UNPAID_BALANCE", "PAYMENT_STATE", "TITLE", "NOTE", "ORDER_TYPE_ID", "TAX_REMOVED", "IS_VAT", "ORDERS_STATE", "MANUAL_TRANSACTION", "GROUP_LINE_ITEMS", "TEST_MODE", "PAY_TYPE", "CREATED_TIME", "CLIENT_CREATED_TIME", "MODIFIED_TIME", "DELETED_TIMESTAMP", "SERVICE_CHARGE", "ADDITIONAL_CHARGES", "DEVICE_ID", "MERCHANT_ID", "PRINT_GROUPS", "ORDER_FULFILLMENT_EVENT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ATTRIBUTES = {"ID", "NAME", "ITEM_GROUP_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SYSTEM_ORDER_TYPES = {"ID", "LABEL_KEY", "IS_QSR", "IS_FSR", "IS_RETAIL", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ITEM_GROUPS = {"ID", "NAME", "ATTRIBUTES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] INVENTORY_ITEMS = {"ID", "HIDDEN", "AVAILABLE", "AUTO_MANAGE", "ITEM_GROUP_ID", "NAME", "ALTERNATE_NAME", "CODE", "SKU", "PRICE", "PRICE_TYPE", "DEFAULT_TAX_RATES", "UNIT_NAME", "COST", "IS_REVENUE", "STOCK_COUNT", "TAX_RATES", "CANONICAL_ID", "ITEM_STOCK_STOCK_COUNT", "ITEM_STOCK_QUANTITY", "ITEM_STOCK_MODIFIED_TIME", "MODIFIED_TIME", "DELETED_TIME", "PRICE_WITHOUT_VAT", "COLOR_CODE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] DISCOUNTS = {"ID", "NAME", "DISCOUNTS_TYPE", "DISCOUNTS_PERCENTAGE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MERCHANTS = {"ID", "NAME", "RESELLER_ID", "OWNER_ID", "OWNER_NAME", "OWNER_NICKNAME", "OWNER_CUSTOM_ID", "OWNER_EMAIL", "OWNER_INVITE_SENT", "OWNER_CLAIMED_TIME", "OWNER_DELETED_TIME", "OWNER_PIN", "OWNER_UNHASHED_PIN", "OWNER_IS_OWNER", "ADDRESS", "MERCHANT_PLAN_ID", "CREATED_TIME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TAGS = {"ID", "NAME", "SHOW_IN_REPORTING", "PRINTERS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] DEVICES = {"ID", "NAME", "MODEL", "MERCHANT_ID", "ORDER_PREFIX", "TERMINAL_ID", "TERMINAL_PREFIX", "DEVICES_SERIAL", "BUILD_NUMBER", "SECURE_ID", "BUILD_TYPE", "CPU_ID", "IMEI", "IMSI", "SIM_ICCID", "DEVICE_CERTIFICATE", "PED_CERTIFICATE", "DEVICE_TYPE_NAME", "PRODUCT_NAME", "PIN_DISABLED", "OFFLINE_PAYMENTS", "OFFLINE_PAYMENTS_ALL", "OFFLINE_PAYMENTS_LIMIT", "OFFLINE_PAYMENTS_PROMPT_THRESHOLD", "OFFLINE_PAYMENTS_TOTAL_PAYMENTS_LIMIT", "OFFLINE_PAYMENTS_LIMIT_DEFAULT", "OFFLINE_PAYMENTS_PROMPT_THRESHOLD_DEFAULT", "OFFLINE_PAYMENTS_TOTAL_PAYMENTS_LIMIT_DEFAULT", "OFFLINE_PAYMENTS_MAX_LIMIT", "OFFLINE_PAYMENTS_MAX_TOTAL_PAYMENTS_LIMIT", "SHOW_OFFLINE_PAYMENTS", "MAX_OFFLINE_DAYS", "ALLOW_STORE_AND_FORWARD", "SECURE_REPORTS", "BUNDLE_INDICATOR", "DEVICE_TYPE_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MERCHANT_PROPERTIES = {"MERCHANT_REF_ID", "DEFAULT_CURRENCY", "TIPS_ENABLED", "RECEIPT_PROPERTIES", "SUMMARY_HOUR", "SIGNATURE_THRESHOLD", "HAS_DEFAULT_EMPLOYEE", "TIP_RATE_DEFAULT", "ON_PAPER_TIP_SIGNATURES", "AUTO_LOGOUT", "ORDER_TITLE", "ORDER_TITLE_MAX", "RESET_ON_REPORTING_TIME", "NOTES_ON_ORDERS", "DELETE_ORDERS", "REMOVE_TAX_ENABLED", "GROUP_LINE_ITEMS", "ALTERNATE_INVENTORY_NAMES", "AUTO_PRINT", "INFOLEASE_SUPPRESS_BILLING", "INFOLEASE_SUPPRESS_PLAN_BILLING", "SHIPPING_ADDRESS", "MARKETING_ENABLED", "SUPPORT_PHONE", "SUPPORT_EMAIL", "MANUAL_CLOSEOUT", "MANUAL_CLOSEOUT_PER_DEVICE", "AUTO_CLOSEOUT_TIMEZONE", "SHOW_CLOSEOUT_ORDERS", "SEND_CLOSEOUT_EMAIL", "STAY_IN_CATEGORY", "LOCALE", "TIMEZONE", "VAT", "VAT_TAX_NAME", "APP_BILLING_SYSTEM", "ABA_ACCOUNT_NUMBER", "DDA_ACCOUNT_NUMBER", "TRACK_STOCK", "UPDATE_STOCK", "ALLOW_CLOCK_OUT_WITH_OPEN_ORDERS", "LOG_IN_CLOCK_IN_PROMPT", "PIN_LENGTH", "CASH_BACK_ENABLED", "CASH_BACK_OPTIONS", "MAX_CASH_BACK", "HIERARCHY", "HAS_CONSENTED", "MERCHANT_BOARDING_STATUS", "PRINTED_FIRST_DATA_RECEIPT_LOGO_ENABLED", "MERCHANT_PRIVACY_POLICY_URL", "DISABLE_PRINT_TAXES_PAYMENT_ON_RECEIPTS", "LIMP_MODE_ALLOWED2", "ORDER_CHANGE_REASON_SETTING", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MERCHANT_ROLES = {"ID", "NAME", "SYSTEM_ROLE", "MERCHANT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MERCHANT_GATEWAY = {"PAYMENT_PROCESSOR_NAME", "AUTHORIZATION_FRONT_END", "ACQUIRING_BACK_END", "PAYMENT_GATEWAY_API", "ACCOUNT_NAME", "ALT_MID", "MID", "FNS", "TID", "STORE_ID", "SUPPORTS_TIPPING", "FRONTEND_MID", "BACKEND_MID", "MCC", "TOKEN_TYPE", "GROUP_ID", "DEBIT_KEY_CODE", "SRED_CODE", "SUPPORTS_TIP_ADJUST", "SUPPORTS_NAKED_CREDIT", "SUPPORTS_MULTI_PAY_TOKEN", "SUPPORTS_PREAUTH_OVERAGE", "CLOSING_TIME", "TELECHECK_ICA_MID", "TELECHECK_CBP_MID", "TELECHECK_PPD_MID", "NEW_BATCH_CLOSE_ENABLED", "PRODUCTION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEES = {"ID", "NAME", "NICKNAME", "CUSTOM_ID", "EMAIL", "INVITE_SENT", "CLAIMED_TIME", "DELETED_TIME", "PIN", "EMPLOYEES_ROLE", "IS_OWNER", "SHIFTS", "EMPLOYEE_CARDS", "MERCHANT_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TIP_SUGGESTIONS = {"ID", "NAME", "TIP_SUGGESTIONS_PERCENTAGE", "AMOUNT", "IS_ENABLED", "FLAT_TIP", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MODIFIER_GROUP_ITEMS = {"ID", "MODIFIER_GROUP_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TENDERS = {"ID", "EDITABLE", "LABEL_KEY", "LABEL", "OPENS_CASH_DRAWER", "SUPPORTS_TIPPING", "TENDERS_ENABLED", "VISIBLE", "INSTRUCTIONS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MODIFIER_GROUPS = {"ID", "NAME", "ALTERNATE_NAME", "MIN_REQUIRED", "MAX_ALLOWED", "SHOW_BY_DEFAULT", "MODIFIER_IDS", "SORT_ORDER", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MODIFIERS = {"ID", "NAME", "ALTERNATE_NAME", "AVAILABLE", "PRICE", "MODIFIER_GROUP_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MERCHANTS_DEFAULT_SERVICE_CHARGE = {"ID", "NAME", "MERCHANTS_DEFAULT_SERVICE_CHARGE_ENABLED", "MERCHANTS_DEFAULT_SERVICE_CHARGE_PERCENTAGE", "PERCENTAGE_DECIMAL", "IS_AUTO_APPLIED", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] AUTHORIZATIONS = {"ID", "PAYMENT_ID", "TAB_NAME", "AMOUNT", "CARD_TYPE", "LAST4", "AUTHCODE", "TOKEN", "AUTHORIZATIONS_TYPE", "NOTE", "EXTERNAL_REFERENCE_ID", "CLOSING_PAYMENT_ID", "CREATED_TIME", "ADDITIONAL_CHARGES", "TRANSACTION_SEQUENCE_COUNTER", "ACQUIRER_TERMINAL_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CASH_EVENTS = {"CASH_EVENTS_TYPE", "AMOUNT_CHANGE", "CASH_EVENTS_TIMESTAMP", "NOTE", "EMPLOYEE_ID", "DEVICE_ID", "MERCHANT_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] REFUNDS = {"ID", "ORDER_REF_ID", "DEVICE_ID", "AMOUNT", "TAX_AMOUNT", "TIP_AMOUNT", "CREATED_TIME", "CLIENT_CREATED_TIME", "GATEWAY_PROCESSING_TIME", "PAYMENT_ID", "EMPLOYEE_ID", "LINE_ITEMS", "OVERRIDE_MERCHANT_TENDER_ID", "TAXABLE_AMOUNT_RATES", "SERVICE_CHARGE_AMOUNT_ID", "SERVICE_CHARGE_AMOUNT_NAME", "SERVICE_CHARGE_AMOUNT_AMOUNT", "ADDITIONAL_CHARGES", "ATTRIBUTES", "GERMAN_INFO", "APP_TRACKING", "VOIDED", "VOID_REASON", "CARD_TRANSACTION", "TRANSACTION_INFO", "MERCHANT_ID", "EXTERNAL_REFERENCE_ID", "AUTH_CODE", "STATUS", "OCEAN_GATEWAY_INFO", "REASON", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CATEGORIES = {"ID", "NAME", "SORT_ORDER", "COLOR_CODE", "DELETED", "MODIFIED_TIME", "CANONICAL_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] VOIDED_LINE_ITEMS = {"MERCHANT_ID", "REASON", "REMOVED_BY_ID", "APPROVED_BY_ID", "DELETE_TYPE", "DEVICE_ID", "CREATED_BY_ID", "DELETED_TIME", "ENVIRONMENT", "CLIENT_EVENT_TYPE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] OPTIONS = {"ID", "NAME", "ATTRIBUTE_ID", "ITEMS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ITEM_STOCKS = {"ITEM_ID", "STOCK_COUNT", "QUANTITY", "MODIFIED_TIME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOMER = {"ID", "MERCHANT_ID", "FIRST_NAME", "LAST_NAME", "MARKETING_ALLOWED", "CUSTOMER_SINCE", "ADDRESSES", "EMAIL_ADDRESSES", "PHONE_NUMBERS", "CARDS", "METADATA", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ORDER_TYPES = {"ID", "LABEL_KEY", "LABEL", "TAXABLE", "IS_DEFAULT", "FILTER_CATEGORIES", "IS_HIDDEN", "FEE", "MIN_ORDER_AMOUNT", "MAX_ORDER_AMOUNT", "MAX_RADIUS", "AVG_ORDER_TIME", "HOURS_AVAILABLE", "CUSTOMER_ID_METHOD", "IS_DELETED", "SYSTEM_ORDER_TYPE_ID", "HOURS", "CATEGORIES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CATEGORY_ITEMS = {"ID", "CATEGORY_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};

        private CloverHeaders() {}
    }

    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.ofEntries(
            Map.entry(CloverEntityNames.EMPLOYEES, List.of(CloverEntityNames.EMPLOYEE_SHIFTS)),
            Map.entry(CloverEntityNames.ORDERS, List.of(CloverEntityNames.ORDER_LINE_ITEMS)),
            Map.entry(CloverEntityNames.CATEGORIES, List.of(CloverEntityNames.CATEGORY_ITEMS)),
            Map.entry(CloverEntityNames.MODIFIER_GROUPS, List.of(CloverEntityNames.MODIFIER_GROUP_ITEMS))
    );

    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(CloverEntityNames.TAX_RATES, CloverHeaders.TAX_RATES),
            Map.entry(CloverEntityNames.TAG_INVENTORY_ITEMS, CloverHeaders.TAG_INVENTORY_ITEMS),
            Map.entry(CloverEntityNames.ORDER_LINE_ITEMS, CloverHeaders.ORDER_LINE_ITEMS),
            Map.entry(CloverEntityNames.PAYMENTS, CloverHeaders.PAYMENTS),
            Map.entry(CloverEntityNames.CREDIT_REFUNDS, CloverHeaders.CREDIT_REFUNDS),
            Map.entry(CloverEntityNames.ORDERS, CloverHeaders.ORDERS),
            Map.entry(CloverEntityNames.ATTRIBUTES, CloverHeaders.ATTRIBUTES),
            Map.entry(CloverEntityNames.SYSTEM_ORDER_TYPES, CloverHeaders.SYSTEM_ORDER_TYPES),
            Map.entry(CloverEntityNames.ITEM_GROUPS, CloverHeaders.ITEM_GROUPS),
            Map.entry(CloverEntityNames.INVENTORY_ITEMS, CloverHeaders.INVENTORY_ITEMS),
            Map.entry(CloverEntityNames.DISCOUNTS, CloverHeaders.DISCOUNTS),
            Map.entry(CloverEntityNames.MERCHANTS, CloverHeaders.MERCHANTS),
            Map.entry(CloverEntityNames.TAGS, CloverHeaders.TAGS),
            Map.entry(CloverEntityNames.DEVICES, CloverHeaders.DEVICES),
            Map.entry(CloverEntityNames.MERCHANT_PROPERTIES, CloverHeaders.MERCHANT_PROPERTIES),
            Map.entry(CloverEntityNames.MERCHANT_ROLES, CloverHeaders.MERCHANT_ROLES),
            Map.entry(CloverEntityNames.MERCHANT_GATEWAY, CloverHeaders.MERCHANT_GATEWAY),
            Map.entry(CloverEntityNames.TIP_SUGGESTIONS, CloverHeaders.TIP_SUGGESTIONS),
            Map.entry(CloverEntityNames.MODIFIER_GROUP_ITEMS, CloverHeaders.MODIFIER_GROUP_ITEMS),
            Map.entry(CloverEntityNames.TENDERS, CloverHeaders.TENDERS),
            Map.entry(CloverEntityNames.MODIFIER_GROUPS, CloverHeaders.MODIFIER_GROUPS),
            Map.entry(CloverEntityNames.MODIFIERS, CloverHeaders.MODIFIERS),
            Map.entry(CloverEntityNames.MERCHANTS_DEFAULT_SERVICE_CHARGE, CloverHeaders.MERCHANTS_DEFAULT_SERVICE_CHARGE),
            Map.entry(CloverEntityNames.AUTHORIZATIONS, CloverHeaders.AUTHORIZATIONS),
            Map.entry(CloverEntityNames.CASH_EVENTS, CloverHeaders.CASH_EVENTS),
            Map.entry(CloverEntityNames.REFUNDS, CloverHeaders.REFUNDS),
            Map.entry(CloverEntityNames.CATEGORIES, CloverHeaders.CATEGORIES),
            Map.entry(CloverEntityNames.VOIDED_LINE_ITEMS, CloverHeaders.VOIDED_LINE_ITEMS),
            Map.entry(CloverEntityNames.OPTIONS, CloverHeaders.OPTIONS),
            Map.entry(CloverEntityNames.CUSTOMER, CloverHeaders.CUSTOMER),
            Map.entry(CloverEntityNames.ORDER_TYPES, CloverHeaders.ORDER_TYPES),
            Map.entry(CloverEntityNames.EMPLOYEE_SHIFTS, CloverHeaders.EMPLOYEE_SHIFTS),
            Map.entry(CloverEntityNames.EMPLOYEES, CloverHeaders.EMPLOYEES),
            Map.entry(CloverEntityNames.ITEM_STOCKS, CloverHeaders.ITEM_STOCKS),
            Map.entry(CloverEntityNames.CATEGORY_ITEMS, CloverHeaders.CATEGORY_ITEMS)
    );

    public static final Map<String, String[]> DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(CloverEntityNames.MERCHANT_ROLES, CloverHeaders.MERCHANT_ROLES),
            Map.entry(CloverEntityNames.EMPLOYEES, CloverHeaders.EMPLOYEES),
            Map.entry(CloverEntityNames.ITEM_GROUPS, CloverHeaders.ITEM_GROUPS),
            Map.entry(CloverEntityNames.INVENTORY_ITEMS, CloverHeaders.INVENTORY_ITEMS),
            Map.entry(CloverEntityNames.TAGS, CloverHeaders.TAGS),
            Map.entry(CloverEntityNames.CATEGORIES, CloverHeaders.CATEGORIES),
            Map.entry(CloverEntityNames.MODIFIER_GROUPS, CloverHeaders.MODIFIER_GROUPS),
            Map.entry(CloverEntityNames.MODIFIER_GROUP_ITEMS, CloverHeaders.MODIFIER_GROUP_ITEMS),
            Map.entry(CloverEntityNames.MODIFIERS, CloverHeaders.MODIFIERS),
            Map.entry(CloverEntityNames.ATTRIBUTES, CloverHeaders.ATTRIBUTES),
            Map.entry(CloverEntityNames.OPTIONS, CloverHeaders.OPTIONS),
            Map.entry(CloverEntityNames.DISCOUNTS, CloverHeaders.DISCOUNTS),
            Map.entry(CloverEntityNames.ORDERS, CloverHeaders.ORDERS),
            Map.entry(CloverEntityNames.PAYMENTS, CloverHeaders.PAYMENTS),
            Map.entry(CloverEntityNames.TENDERS, CloverHeaders.TENDERS),
            Map.entry(CloverEntityNames.EMPLOYEE_SHIFTS, CloverHeaders.EMPLOYEE_SHIFTS),
            Map.entry(CloverEntityNames.CATEGORY_ITEMS, CloverHeaders.CATEGORY_ITEMS)
    );

    public static final Map<String, String[]> NON_DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(CloverEntityNames.TAX_RATES, CloverHeaders.TAX_RATES),
            Map.entry(CloverEntityNames.TAG_INVENTORY_ITEMS, CloverHeaders.TAG_INVENTORY_ITEMS),
            Map.entry(CloverEntityNames.ORDER_LINE_ITEMS, CloverHeaders.ORDER_LINE_ITEMS),
            Map.entry(CloverEntityNames.CREDIT_REFUNDS, CloverHeaders.CREDIT_REFUNDS),
            Map.entry(CloverEntityNames.SYSTEM_ORDER_TYPES, CloverHeaders.SYSTEM_ORDER_TYPES),
            Map.entry(CloverEntityNames.MERCHANTS, CloverHeaders.MERCHANTS),
            Map.entry(CloverEntityNames.DEVICES, CloverHeaders.DEVICES),
            Map.entry(CloverEntityNames.MERCHANT_PROPERTIES, CloverHeaders.MERCHANT_PROPERTIES),
            Map.entry(CloverEntityNames.MERCHANT_GATEWAY, CloverHeaders.MERCHANT_GATEWAY),
            Map.entry(CloverEntityNames.TIP_SUGGESTIONS, CloverHeaders.TIP_SUGGESTIONS),
            Map.entry(CloverEntityNames.MERCHANTS_DEFAULT_SERVICE_CHARGE, CloverHeaders.MERCHANTS_DEFAULT_SERVICE_CHARGE),
            Map.entry(CloverEntityNames.AUTHORIZATIONS, CloverHeaders.AUTHORIZATIONS),
            Map.entry(CloverEntityNames.CASH_EVENTS, CloverHeaders.CASH_EVENTS),
            Map.entry(CloverEntityNames.REFUNDS, CloverHeaders.REFUNDS),
            Map.entry(CloverEntityNames.VOIDED_LINE_ITEMS, CloverHeaders.VOIDED_LINE_ITEMS),
            Map.entry(CloverEntityNames.CUSTOMER, CloverHeaders.CUSTOMER),
            Map.entry(CloverEntityNames.ORDER_TYPES, CloverHeaders.ORDER_TYPES),
            Map.entry(CloverEntityNames.ITEM_STOCKS, CloverHeaders.ITEM_STOCKS)
    );

    public static final Map<String, String> ENTITY_API_PATH_MAP = Map.ofEntries(
            Map.entry(CloverEntityNames.MERCHANT_PROPERTIES, "v3/merchants/{mId}/properties"),
            Map.entry(CloverEntityNames.MERCHANT_GATEWAY, "v3/merchants/{mId}/gateway"),
            Map.entry(CloverEntityNames.MERCHANTS, "v3/merchants/{mId}"),
            Map.entry(CloverEntityNames.MERCHANT_ROLES, "v3/merchants/{mId}/roles"),
            Map.entry(CloverEntityNames.MERCHANTS_DEFAULT_SERVICE_CHARGE, "v3/merchants/{mId}/default_service_charge"),
            Map.entry(CloverEntityNames.SYSTEM_ORDER_TYPES, "v3/merchants/{mId}/system_order_types"),
            Map.entry(CloverEntityNames.CASH_EVENTS, "v3/merchants/{mId}/cash_events"),
            Map.entry(CloverEntityNames.CUSTOMER, "v3/merchants/{mId}/customers"),
            Map.entry(CloverEntityNames.EMPLOYEES, "v3/merchants/{mId}/employees"),
            Map.entry(CloverEntityNames.INVENTORY_ITEMS, "v3/merchants/{mId}/items"),
            Map.entry(CloverEntityNames.EMPLOYEE_SHIFTS, "v3/merchants/{mId}/employees/{employee_id}/shifts"),
            Map.entry(CloverEntityNames.ITEM_STOCKS, "v3/merchants/{mId}/item_stocks"),
            Map.entry(CloverEntityNames.ITEM_GROUPS, "v3/merchants/{mId}/item_groups"),
            Map.entry(CloverEntityNames.TAGS, "v3/merchants/{mId}/tags"),
            Map.entry(CloverEntityNames.TAG_INVENTORY_ITEMS, "v3/merchants/{mId}/tag_items"),
            Map.entry(CloverEntityNames.TAX_RATES, "v3/merchants/{mId}/tax_rates"),
            Map.entry(CloverEntityNames.CATEGORIES, "v3/merchants/{mId}/categories"),
            Map.entry(CloverEntityNames.MODIFIER_GROUPS, "v3/merchants/{mId}/modifier_groups"),
            Map.entry(CloverEntityNames.MODIFIER_GROUP_ITEMS, "v3/merchants/{mId}/modifier_groups/{modifier_group_id}/items"),
            Map.entry(CloverEntityNames.MODIFIERS, "v3/merchants/{mId}/modifiers"),
            Map.entry(CloverEntityNames.ATTRIBUTES, "v3/merchants/{mId}/attributes"),
            Map.entry(CloverEntityNames.OPTIONS, "v3/merchants/{mId}/options"),
            Map.entry(CloverEntityNames.DISCOUNTS, "v3/merchants/{mId}/discounts"),
            Map.entry(CloverEntityNames.ORDERS, "v3/merchants/{mId}/orders"),
            Map.entry(CloverEntityNames.ORDER_LINE_ITEMS, "v3/merchants/{mId}/orders/{orderId}/line_items"),
            Map.entry(CloverEntityNames.VOIDED_LINE_ITEMS, "v3/merchants/{mId}/voided_line_items"),
            Map.entry(CloverEntityNames.PAYMENTS, "v3/merchants/{mId}/payments"),
            Map.entry(CloverEntityNames.AUTHORIZATIONS, "v3/merchants/{mId}/authorizations"),
            Map.entry(CloverEntityNames.CREDIT_REFUNDS, "v3/merchants/{mId}/credit_refunds"),
            Map.entry(CloverEntityNames.DEVICES, "v3/merchants/{mId}/devices"),
            Map.entry(CloverEntityNames.TIP_SUGGESTIONS, "v3/merchants/{mId}/tip_suggestions"),
            Map.entry(CloverEntityNames.TENDERS, "v3/merchants/{mId}/tenders"),
            Map.entry(CloverEntityNames.ORDER_TYPES, "v3/merchants/{mId}/order_types"),
            Map.entry(CloverEntityNames.REFUNDS, "v3/merchants/{mId}/refunds"),
            Map.entry(CloverEntityNames.CATEGORY_ITEMS, "v3/merchants/{mId}/categories/{category_id}/items")
    );

    public static final List<String> excludedEntities = List.of();

    public static Map<String, String> getEntityApiPathMap(String merchantId) {
        // Generate API path map with actual merchant ID
        return ENTITY_API_PATH_MAP.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .replace("{mId}", merchantId)
                ));
    }

    public static final Map<String, List<String>> CUSTOM_QUERY_PARAM_BY_ENTITY = Map.ofEntries(
            Map.entry(CloverEntityNames.ORDERS, List.of("filter=createdTime>")),
            Map.entry(CloverEntityNames.EMPLOYEE_SHIFTS, List.of("filter=in_time>")),
            Map.entry(CloverEntityNames.AUTHORIZATIONS, List.of("filter=clientCreatedTime>")),
            Map.entry(CloverEntityNames.REFUNDS, List.of("filter=createdTime>")),
            Map.entry(CloverEntityNames.PAYMENTS, List.of("filter=clientCreatedTime>"))
    );

    private CloverConstants() {}
}