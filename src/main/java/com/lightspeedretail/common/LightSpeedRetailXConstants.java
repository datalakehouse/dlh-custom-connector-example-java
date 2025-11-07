package com.lightspeedretail.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class LightSpeedRetailXConstants {
    private LightSpeedRetailXConstants() {
    }

    public static final String CONNECTOR_NAME = "LIGHTSPEED_RETAIL_X";
    public static final String OUTPUT_PATH = "/home/nihar/work/aicg/output/";

    public static final class LightSpeedXRetailAPI {
        private LightSpeedXRetailAPI() {
        }

        public static final String TIMEZONE = "UTC";
        public static final String API_KEY = "lsxs_at_e9frlhXOGwRFdSPkLVk4GuYmRb9eWS3w";
        public static final String API_URL = "https://developerdemoeumnh2.retail.lightspeed.app/api";
    }

    public static final class LightSpeedXRetailEntityNames {
        private LightSpeedXRetailEntityNames() {
        }

        public static final String EVENTS = "SECURITY_EVENTS";
        public static final String BRAND = "BRANDS";
        public static final String CHANNEL_REQUESTS = "CHANNEL_REQUESTS";
        public static final String CHANNELS = "CHANNELS";
        public static final String CONSIGNMENT_PRODUCTS = "CONSIGNMENT_PRODUCTS";
        public static final String CONSIGNMENTS = "CONSIGNMENTS";
        public static final String CUSTOMER_GROUPS = "CUSTOMER_GROUPS";
        public static final String CUSTOMERS = "CUSTOMERS";
        public static final String INVENTORY = "INVENTORY";
        public static final String OUTLET_TAXES = "OUTLET_TAXES";
        public static final String OUTLETS = "OUTLETS";
        public static final String PAYMENT_TYPES = "PAYMENT_TYPES";
        public static final String PAYMENT = "PAYMENT";
        public static final String SALES_PICK_LISTS = "SALES_PICK_LISTS";
        public static final String SALES_PICK_LISTS_LINE_ITEMS = "SALES_PICK_LISTS_LINE_ITEMS";
        public static final String PRICE_BOOK = "PRICE_BOOKS";
        public static final String PRICE_BOOKS_OUTLETS = "PRICE_BOOKS_OUTLETS";
        public static final String PRICE_BOOKS_CUSTOMER_GROUPS = "PRICE_BOOKS_CUSTOMER_GROUPS";
        public static final String PRICE_BOOK_PRODUCTS = "PRICE_BOOK_PRODUCTS";
        public static final String PRODUCT_CATEGORIES = "PRODUCT_CATEGORIES";
        public static final String PRODUCT_CATEGORY_PATH = "PRODUCT_CATEGORY_PATH";
        public static final String PRODUCT_IMAGES = "PRODUCT_IMAGES";
        public static final String PRODUCT_TYPES = "PRODUCT_TYPES";
        public static final String PRODUCTS = "PRODUCTS";
        public static final String PRODUCT_INVENTORY = "PRODUCT_INVENTORY";
        public static final String PRODUCTS_VARIANT_OPTIONS = "PRODUCTS_VARIANT_OPTIONS";
        public static final String PRODUCTS_IMAGES = "PRODUCTS_IMAGES";
        public static final String PRODUCT_SUPPLIERS = "PRODUCT_SUPPLIERS";
        public static final String PRODUCT_PRICE_STANDARD = "PRODUCT_PRICE_STANDARD";
        public static final String PROMOTIONS = "PROMOTIONS";
        public static final String PROMO_CODE = "PROMO_CODE";
        public static final String QUOTES = "QUOTES";
        public static final String QUOTES_PRODUCTS = "QUOTES_PRODUCTS";
        public static final String QUOTES_PRODUCT_TAX_COMPONENTS = "QUOTES_PRODUCT_TAX_COMPONENTS";
        public static final String REGISTERS = "REGISTERS";
        public static final String REGISTER_PAYMENT_SUMMARY = "REGISTER_PAYMENT_SUMMARY";
        public static final String RETAILER = "RETAILER";
        public static final String SALES = "SALES";
        public static final String SALES_LINE_ITEMS = "SALES_LINE_ITEMS";
        public static final String SALES_PAYMENTS = "SALES_PAYMENTS";
        public static final String SALES_TAXES = "SALES_TAXES";
        public static final String SERIAL_NUMBERS = "SERIALNUMBERS";
        public static final String SHIFTS = "SHIFTS";
        public static final String SUPPLIERS = "SUPPLIERS";
        public static final String TAGS = "TAGS";
        public static final String TAXES = "TAXES";
        public static final String TAX_RATES = "TAX_RATES";
        public static final String USER = "USER";
        public static final String USER_ROLES = "USER_ROLES";
        public static final String USER_SALE_TOTALS = "USER_SALE_TOTALS";
        public static final String VARIANT_ATTRIBUTES = "VARIANT_ATTRIBUTES";
        public static final String GIFT_CARDS = "GIFT_CARDS";
        public static final String GIFT_CARDS_GIFT_CARD_TRANSACTIONS = "GIFT_CARDS_GIFT_CARD_TRANSACTIONS";
        public static final String PARTNER_SUBSCRIPTION = "PARTNER/BILLING/SUBSCRIPTIONS";
        public static final String PARTNER_SUBSCRIPTION_INFO = "PARTNER_SUBSCRIPTION_INFO";
        public static final String PARTNER_SUBSCRIPTION_INFO_COMPONENTS = "PARTNER_SUBSCRIPTION_INFO_COMPONENTS";
        public static final String PARTNER_SUBSCRIPTION_TOKEN = "PARTNER_SUBSCRIPTION_TOKEN";
        public static final String CUSTOMER_STORE_CREDITS = "STORE_CREDITS";
        public static final String CUSTOMER_STORE_CREDIT_TRANSACTIONS = "CUSTOMER_STORE_CREDIT_TRANSACTIONS";
        public static final String CUSTOMER_STORE_CREDITS_REPORTS = "STORE_CREDITS/REPORT";
        public static final String CUSTOM_FIELDS = "WORKFLOWS/CUSTOM_FIELDS";
        public static final String CUSTOM_FIELD_VALUES = "WORKFLOWS/CUSTOM_FIELDS/VALUES";
        public static final String REMOTE_RULES = "WORKFLOWS/REMOTE_RULES";
        public static final String WORKFLOW_RULES = "WORKFLOWS/RULES";
        public static final String WEBHOOKS = "WEBHOOKS";
        public static final String SALES_FULFILLMENT = "SALES_FULFILLMENT";
        public static final String SALES_FULFILLMENT_LINE_ITEMS = "SALES_FULFILLMENT_LINE_ITEMS";
    }

    public static final class LightSpeedXRetailHeaders {
        private LightSpeedXRetailHeaders() {
        }

        public static final String[] EVENTS = {"ID", "USER_ID", "TYPE", "ENTITY_ID", "ACTION", "IP_ADDRESS", "USER_AGENT", "OCCURRED_AT", "CREATED_AT", "DATA", "OLD_DATA", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] BRAND = {"ID", "NAME", "DELETED_AT", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CHANNEL_REQUESTS = {"ID", "GROUPING_ID", "REQUEST_METHOD", "STATUS_CODE", "URL", "REQUEST", "RESPONSE", "OCCURRED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CHANNELS = {"ID", "STORE_URL", "CHANNEL_TYPE", "REGISTER_ID", "PAYMENT_TYPE_ID", "INVENTORY_OUTLET_IDS", "CREATED_AT", "SALES_LAST_IMPORTED_AT", "PRODUCTS_LAST_IMPORTED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CONSIGNMENT_PRODUCTS = {"PRODUCT_ID", "COUNT", "RECEIVED", "COST", "IS_INCLUDED", "STATUS", "CREATED_AT", "UPDATED_AT", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CONSIGNMENTS = {"ID", "OUTLET_ID", "NAME", "CONSIGNMENT_DATE", "SHOW_INACTIVE", "DUE_AT", "RECEIVED_AT", "TYPE", "STATUS", "SUPPLIER_ID", "SOURCE_OUTLET_ID", "SUPPLIER_INVOICE", "REFERENCE", "TOTAL_COUNT_GAIN", "TOTAL_COST_GAIN", "TOTAL_COUNT_LOSS", "TOTAL_COST_LOSS", "TOTAL_SENT_COUNT", "TOTAL_SENT_COST", "TOTAL_RECEIVED_COUNT", "TOTAL_RECEIVED_COST", "CREATED_AT", "UPDATED_AT", "DELETED_AT", "VERSION", "FILTERS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOMER_GROUPS = {"ID", "NAME", "GROUP_ID", "RETAILER_ID", "CREATED_AT", "UPDATED_AT", "DELETED_AT", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOMERS = {"ID", "FIRST_NAME", "LAST_NAME", "CUSTOMER_CODE", "CUSTOMER_GROUP_ID", "ENABLE_LOYALTY", "EMAIL", "NOTE", "GENDER", "DATE_OF_BIRTH", "COMPANY_NAME", "DO_NOT_EMAIL", "PHONE", "MOBILE", "FAX", "TWITTER", "WEBSITE", "PHYSICAL_ADDRESS_1", "PHYSICAL_ADDRESS_2", "PHYSICAL_SUBURB", "PHYSICAL_CITY", "PHYSICAL_POSTCODE", "PHYSICAL_STATE", "PHYSICAL_COUNTRY_ID", "POSTAL_ADDRESS_1", "POSTAL_ADDRESS_2", "POSTAL_SUBURB", "POSTAL_CITY", "POSTAL_POSTCODE", "POSTAL_STATE", "POSTAL_COUNTRY_ID", "CUSTOM_FIELD_1", "CUSTOM_FIELD_2", "CUSTOM_FIELD_3", "CUSTOM_FIELD_4", "YEAR_TO_DATE", "BALANCE", "LOYALTY_BALANCE", "ON_ACCOUNT_LIMIT", "TAX_ID", "CREATED_AT", "UPDATED_AT", "DELETED_AT", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] INVENTORY = {"ID", "PRODUCT_ID", "OUTLET_ID", "INVENTORY_LEVEL", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] OUTLET_TAXES = {"OUTLET_ID", "PRODUCT_ID", "TAX_ID", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] OUTLETS = {"ID", "NAME", "DEFAULT_TAX_ID", "CURRENCY", "CURRENCY_SYMBOL", "DISPLAY_PRICES", "TIME_ZONE", "PHYSICAL_ADDRESS_1", "PHYSICAL_ADDRESS_2", "PHYSICAL_SUBURB", "PHYSICAL_CITY", "PHYSICAL_POSTCODE", "PHYSICAL_STATE", "PHYSICAL_COUNTRY_ID", "VERSION", "ATTRIBUTES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAYMENT_TYPES = {"ID", "NAME", "TYPE_ID", "CONFIG_URL", "CONFIG_PRINT", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAYMENT = {"ID", "REGISTER_ID", "OUTLET_ID", "RETAILER_PAYMENT_TYPE_ID", "PAYMENT_TYPE_ID", "NAME", "AMOUNT", "PAYMENT_DATE", "EXTERNAL_ATTRIBUTES", "FEE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SALES_PICK_LISTS = {"ID", "SALE_ID", "OUTLET_ID", "USER_ID", "STATUS", "TYPE", "CREATED_AT", "NOTE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SALES_PICK_LISTS_LINE_ITEMS = {"ID", "SALES_PICK_LISTS_ID", "PRODUCT_ID", "QUANTITY", "PICKED_QUANTITY", "SALE_LINE_ITEM_ID", "NOTE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRICE_BOOK = {"ID", "NAME", "TYPE", "RESTRICT_TO_PLATFORM_KEY", "RESTRICT_TO_PLATFORM_LABEL", "CUSTOMER_GROUP_ID", "OUTLET_ID", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRICE_BOOKS_OUTLETS = {"ID", "PRICE_BOOKS_ID", "NAME", "DEFAULT_TAX_ID", "CURRENCY", "DISPLAY_PRICES", "TIME_ZONE", "PHYSICAL_SUBURB", "PHYSICAL_CITY", "PHYSICAL_POSTCODE", "PHYSICAL_STATE", "EMAIL", "DELETED_AT", "LATITUDE", "LONGITUDE", "VERSION", "PHYSICAL_ADDRESS_1", "PHYSICAL_ADDRESS_2", "PHYSICAL_COUNTRY_ID", "ATTRIBUTES", "CURRENCY_SYMBOL", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRICE_BOOKS_CUSTOMER_GROUPS = {"ID", "PRICE_BOOKS_ID", "NAME", "GROUP_ID", "VERSION", "CREATED_AT", "UPDATED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRICE_BOOK_PRODUCTS = {"ID", "PRODUCT_ID", "PRICE_BOOK_ID", "DEFAULT_DISPLAY_PRICE", "PRICE", "RETAIL_TAX", "LOYALTY_VALUE", "TAX_ID", "ROUNDING", "ADJUSTMENT_TYPE", "ADJUSTMENT", "DISCOUNT", "MIN_UNITS", "MAX_UNITS", "VERSION", "CREATED_AT", "UPDATED_AT", "DELETED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCT_CATEGORIES = {"ID", "NAME", "ROOT_CATEGORY_ID", "PARENT_CATEGORY_ID", "LEAF_CATEGORY", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCT_CATEGORY_PATH = {"ID", "PRODUCT_CATEGORIES_ID", "PRODUCT_TYPE_ID", "NAME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCT_IMAGES = {"ID", "VERSION", "PRODUCT_ID", "POSITION", "STATUS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCT_TYPES = {"ID", "NAME", "VERSION", "LEAF_CATEGORY", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCTS = {"ID", "CLASSIFICATION", "NAME", "VARIANT_NAME", "HANDLE", "SKU_NUMBER", "SUPPLIER_CODE", "ACTIVE", "HAS_INVENTORY", "IS_COMPOSITE", "COMPOSITE_BOM", "DESCRIPTION", "IMAGE_URL", "CREATED_AT", "UPDATED_AT", "DELETED_AT", "SOURCE", "SUPPLY_PRICE", "VERSION", "TYPE_ID", "BRAND_ID", "QUOTE_ID", "ACCOUNT_CODE_SALES", "ACCOUNT_CODE_PURCHASE", "HAS_VARIANTS", "VARIANTS", "PRODUCT_CODES", "BUTTON_ORDER", "PRICE_INCLUDING_TAX", "PRICE_EXCLUDING_TAX", "ATTRIBUTES", "SUPPLIER_ID", "PRODUCT_TYPE_ID", "BRAND_ID", "PRICE_OUTLET_TAX_EXCLUSIVE", "PRICE_OUTLET_LOYALTY_AMOUNT", "PRICE_OUTLETS", "IS_ACTIVE", "TRACKS_INVENTORY", "IMAGE_THUMBNAIL_URL", "TAG_IDS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCT_INVENTORY = {"ID", "OUTLET_ID", "PRODUCT_ID", "INVENTORY_LEVEL", "CURRENT_AMOUNT", "VERSION", "AVERAGE_COST", "REORDER_POINT", "REORDER_AMOUNT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCTS_VARIANT_OPTIONS = {"ID", "PRODUCTS_ID", "NAME", "VALUE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCTS_CATEGORY = {"ID", "PRODUCTS_ID", "NAME", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCTS_IMAGES = {"ID", "PRODUCTS_ID", "PROVIDER", "PROVIDER_ID", "PROVIDER_GROUP", "URL", "VERSION", "SIZES_SS", "SIZES_STANDARD", "SIZES_ST", "SIZES_ORIGINAL", "SIZES_THUMB", "SIZES_SL", "SIZES_SM", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCT_SUPPLIERS = {"ID", "PRODUCT_ID", "SUPPLIER_ID", "SUPPLIER_NAME", "CODE", "PRICE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PRODUCT_PRICE_STANDARD = {"TAX_ID", "PRODUCT_ID", "IS_DEFAULT", "TAX_INCLUSIVE", "TAX_EXCLUSIVE", "LOYALTY_AMOUNT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PROMOTIONS = {"ID", "NAME", "DESCRIPTION", "START_TIME", "END_TIME", "CONDITION_TYPE", "CONDITION_QUANTITY", "CONDITION_MIN_PRICE", "CONDITION_INCLUDE", "CONDITION_EXCLUDE", "CONDITION_MIN_QUANTITY", "CONDITION_MAX_QUANTITY", "ACTION_TYPE", "ACTION_QUANTITY", "ACTION_VALUE", "ACTION_INCLUDE", "ACTION_EXCLUDE", "ACTION_MIN_QUANTITY", "ACTION_MAX_QUANTITY", "LOYALTY_MULTIPLIER", "OUTLET_IDS", "CHANNELS", "CUSTOMER_GROUP_IDS", "STATUS", "USE_PROMO_CODE", "PROMO_CODE_SUMMARY", "RECURRING_PROMOTION_ID", "RECURRING_PROMOTION_OCCURRENCE_TYPE", "RECURRING_PROMOTION_START_TIME", "RECURRING_PROMOTION_END_TIME", "RECURRING_PROMOTION_OCCURRENCES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PROMO_CODE = {"ID", "PROMOTION_ID", "CREATED_USER_ID", "CODE", "REDEEMED", "LIMIT", "CREATED_AT", "GROUP_NAME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] QUOTES = {"ID", "STATUS", "CUSTOMER_ID", "REGISTER_ID", "RETAILER_ID", "USER_ID", "OUTLET_ID", "TOTAL_TAX", "LOYALTY", "TOTAL_PRICE", "EXPIRED_AT", "SHORT_CODE", "NOTE", "DELETED_AT", "CREATED_AT", "UPDATED_AT", "SALES_IDS", "GRAND_TOTAL", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] QUOTES_PRODUCTS = {"ID", "PRODUCT_ID", "QUOTE_ID", "PRICE", "DISCOUNT", "TAX", "TAX_ID", "QUANTITY", "SEQUENCE", "NOTE", "SALESPERSON_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] QUOTES_PRODUCT_TAX_COMPONENTS = {"ID", "QUOTE_PRODUCT_ID", "RATE_ID", "TOTAL_TAX", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] REGISTERS = {"ID", "NAME", "OUTLET_ID", "ASK_FOR_NOTE_ON_SAVE", "PRINT_NOTE_ON_RECEIPT", "ASK_FOR_USER_ON_SALE", "SHOW_DISCOUNTS_ON_RECEIPTS", "PRINT_RECEIPT", "EMAIL_RECEIPT", "INVOICE_PREFIX", "INVOICE_SUFFIX", "INVOICE_SEQUENCE", "BUTTON_LAYOUT_ID", "IS_OPEN", "IS_QUICK_KEYS_ENABLED", "RECEIPT_TEMPLATE_ID", "REGISTER_OPEN_TIME", "REGISTER_OPEN_SEQUENCE_ID", "CASH_MANAGED_PAYMENT_TYPE_ID", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] REGISTER_PAYMENT_SUMMARY = {"REGISTER_OPEN_TIME", "REGISTER_CLOSURE_SEQUENCE_NUMBER", "REGISTER_CLOSURE_ID", "PAYMENTS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] RETAILER = {"ID", "DOMAIN_PREFIX", "STORE_URL", "ACCOUNT_STATUS", "ACCOUNT_TYPE", "NAME", "VERSION", "DISCOUNT_PRODUCT_ID", "NO_TAX_GROUP_ID", "LOYALTY_ENABLED", "LOYALTY_RATIO", "LOYALTY_CLAIM_URL", "LOYALTY_SEND_WELCOME_EMAIL", "SKU_SEQUENCE_ENABLED", "SKU_SEQUENCE_VALUE", "CULTURE", "TIMEZONE", "TAX_EXCLUSIVE", "COUNTRY", "CURRENCY_CODE", "CURRENCY_SYMBOL", "STORE_CREDIT", "GIFT_CARDS_ENABLED", "GIFT_CARDS_NEVER_ENABLED", "CREATED_AT", "ACTIVATED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SALES = {"ID", "OUTLET_ID", "REGISTER_ID", "USER_ID", "CUSTOMER_ID", "INVOICE_NUMBER", "STATUS", "STATE", "ATTRIBUTES", "NOTE", "SHORT_CODE", "TOTAL_PRICE", "TOTAL_TAX", "TOTAL_LOYALTY", "CREATED_AT", "UPDATED_AT", "SALE_DATE", "VERSION", "RECEIPT_NUMBER", "ADJUSTMENTS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SALES_LINE_ITEMS = {"ID", "SALES_ID", "PRODUCT_ID", "TAX_ID", "DISCOUNT_TOTAL", "DISCOUNT", "PRICE_TOTAL", "PRICE", "COST_TOTAL", "COST", "TAX_TOTAL", "TAX", "QUANTITY", "LOYALTY_VALUE", "PRICE_SET", "STATUS", "SEQUENCE", "TAX_COMPONENTS", "UNIT_COST", "UNIT_DISCOUNT", "UNIT_LOYALTY_VALUE", "UNIT_PRICE", "UNIT_TAX", "TOTAL_COST", "TOTAL_DISCOUNT", "TOTAL_LOYALTY_VALUE", "TOTAL_PRICE", "TOTAL_TAX", "IS_RETURN", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SALES_PAYMENTS = {"ID", "SALES_ID", "REGISTER_ID", "OUTLET_ID", "RETAILER_PAYMENT_TYPE_ID", "PAYMENT_TYPE_ID", "NAME", "AMOUNT", "PAYMENT_DATE", "EXTERNAL_ATTRIBUTES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SALES_TAXES = {"ID", "SALES_ID", "AMOUNT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SERIAL_NUMBERS = {"ID", "CODE", "PRODUCT_ID", "OUTLET_ID", "SALE_ID", "LINE_ITEM_ID", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SHIFTS = {"ID", "RETAILER_ID", "USER_ID", "TIME_IN", "TIME_OUT", "UPDATED_AT", "CREATED_AT", "UPDATED_BY", "VERSION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SUPPLIERS = {"ID", "NAME", "SOURCE", "DESCRIPTION", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TAGS = {"ID", "NAME", "DELETED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TAXES = {"ID", "NAME", "VERSION", "IS_DEFAULT", "DISPLAY_NAME", "DELETED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TAX_RATES = {"ID", "RATE", "NAME", "DISPLAY_NAME", "TAX_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] USER = {"ID", "USERNAME", "DISPLAY_NAME", "EMAIL", "EMAIL_VERIFIED_AT", "SWITCH_ID", "ACCOUNT_TYPE", "ENABLED", "RESTRICTED_OUTLET_ID", "RESTRICTED_OUTLET_IDS", "PERMISSIONS", "RULES", "IS_PRIMARY_USER", "IMAGE_SOURCE", "IMAGE_SS", "IMAGE_STANDARD", "IMAGE_ST", "IMAGE_ORIGINAL", "IMAGE_THUMB", "IMAGE_SL", "IMAGE_SM", "TARGET_DAILY", "TARGET_WEEKLY", "TARGET_MONTHLY", "SEEN_AT", "CREATED_AT", "UPDATED_AT", "DELETED_AT", "TIME_UNTIL_DELETION", "VERSION", "ENABLED_MFA", "REQUIRE_PASSWORD_CHANGE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] USER_ROLES = {"ID", "USER_ID", "NAME", "SYSTEM_ROLE_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] USER_SALE_TOTALS = {"USER_ID", "LABEL", "DATE_FROM", "DATE_TO", "TOTAL", "COST", "TAX", "REVENUE", "DISCOUNTS", "LOYALTY", "SALE_COUNT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] VARIANT_ATTRIBUTES = {"ID", "NAME", "DELETED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] GIFT_CARDS = {"ID", "NUMBER", "SALE_ID", "CREATED_AT", "EXPIRES_AT", "SALE_SOURCE", "STATUS", "BALANCE", "TOTAL_SOLD", "TOTAL_REDEEMED", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] GIFT_CARDS_GIFT_CARD_TRANSACTIONS = {"ID", "GIFT_CARDS_ID", "AMOUNT", "TYPE", "USER_ID", "CREATED_AT", "CLIENT_ID", "SOURCE", "SOURCE_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PARTNER_SUBSCRIPTION = {"APPLICATION_ID", "APPLICATION_NAME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PARTNER_SUBSCRIPTION_INFO = {"ID", "PARTNER_SUBSCRIPTION_ID", "CHARGE_INTERVAL", "CHARGE_INTERVAL_UNIT", "CURRENCY", "NEXT_ASSESSMENT_AT", "PRICE_IN_CENTS", "PRODUCT_FAMILY_NAME", "PRODUCT_NAME", "PRODUCT_PRICE_POINT_NAME", "STATE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PARTNER_SUBSCRIPTION_INFO_COMPONENTS = {"PARTNER_SUBSCRIPTION_INFO_ID", "CHARGE_INTERVAL_UNIT", "COMPONENT_NAME", "QUANTITY", "UNIT_PRICE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PARTNER_SUBSCRIPTION_TOKEN = {"COMPONENTS", "CREATED_AT", "IS_USED", "PRODUCT_HANDLE", "PRODUCT_PRICE_POINT_HANDLE", "RETAILER_ID", "RETURN_URL", "SUBSCRIPTION_ID", "TOKEN", "USED_AT", "VEND_APPLICATION_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOMER_STORE_CREDITS = {"CUSTOMER_ID", "BALANCE", "TOTAL_CREDIT_ISSUED", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOMER_STORE_CREDIT_TRANSACTIONS = {"CUSTOMER_ID", "AMOUNT", "TYPE", "NOTES", "USER_ID", "CLIENT_ID", "CREATED_AT", "SALE_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOMER_STORE_CREDITS_REPORTS = {"TOTAL_VALUE_ISSUED", "TOTAL_VALUE_REDEEMED", "OUTSTANDING_BALANCE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOM_FIELDS = {"ID", "ENTITY", "NAME", "TITLE", "TYPE", "VISIBLE_IN_UI", "EDITABLE_IN_UI", "CREATED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOM_FIELD_VALUES = {"DEFINITION_ID", "NAME", "TITLE", "TYPE", "STRING_VALUE", "INTEGER_VALUE", "BOOLEAN_VALUE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] REMOTE_RULES = {"ID", "URL", "OAUTH_APPLICATION_ID", "CREATED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] WORKFLOW_RULES = {"ID", "EVENT_TYPE", "REMOTE_RULE_ID", "CREATED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] WEBHOOKS = {"ID", "RETAILER_ID", "ACTIVE", "URL", "TYPE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SALES_FULFILLMENT = {"ID", "SALE_ID", "OUTLET_ID", "USER_ID", "STATUS", "CREATED_AT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SALES_FULFILLMENT_LINE_ITEMS = {"ID", "SALES_FULFILLMENT_ID", "PRODUCT_ID", "QUANTITY", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};

    }

    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.<String, String[]>ofEntries(
            entry(LightSpeedXRetailEntityNames.CUSTOMERS, LightSpeedXRetailHeaders.CUSTOMERS),
            entry(LightSpeedXRetailEntityNames.REGISTERS, LightSpeedXRetailHeaders.REGISTERS),
            entry(LightSpeedXRetailEntityNames.PRODUCT_TYPES, LightSpeedXRetailHeaders.PRODUCT_TYPES),
            entry(LightSpeedXRetailEntityNames.PRODUCTS, LightSpeedXRetailHeaders.PRODUCTS),
            entry(LightSpeedXRetailEntityNames.OUTLETS, LightSpeedXRetailHeaders.OUTLETS),
            entry(LightSpeedXRetailEntityNames.PAYMENT_TYPES, LightSpeedXRetailHeaders.PAYMENT_TYPES),
            entry(LightSpeedXRetailEntityNames.BRAND, LightSpeedXRetailHeaders.BRAND),
            entry(LightSpeedXRetailEntityNames.CHANNEL_REQUESTS, LightSpeedXRetailHeaders.CHANNEL_REQUESTS),
            entry(LightSpeedXRetailEntityNames.CHANNELS, LightSpeedXRetailHeaders.CHANNELS),
            entry(LightSpeedXRetailEntityNames.CONSIGNMENTS, LightSpeedXRetailHeaders.CONSIGNMENTS),
            entry(LightSpeedXRetailEntityNames.CUSTOMER_GROUPS, LightSpeedXRetailHeaders.CUSTOMER_GROUPS),
            entry(LightSpeedXRetailEntityNames.INVENTORY, LightSpeedXRetailHeaders.INVENTORY),
            entry(LightSpeedXRetailEntityNames.OUTLET_TAXES, LightSpeedXRetailHeaders.OUTLET_TAXES),
            entry(LightSpeedXRetailEntityNames.PRICE_BOOK, LightSpeedXRetailHeaders.PRICE_BOOK),
            entry(LightSpeedXRetailEntityNames.PRICE_BOOK_PRODUCTS, LightSpeedXRetailHeaders.PRICE_BOOK_PRODUCTS),
            entry(LightSpeedXRetailEntityNames.PROMOTIONS, LightSpeedXRetailHeaders.PROMOTIONS),
            entry(LightSpeedXRetailEntityNames.QUOTES, LightSpeedXRetailHeaders.QUOTES),
            entry(LightSpeedXRetailEntityNames.RETAILER, LightSpeedXRetailHeaders.RETAILER),
            entry(LightSpeedXRetailEntityNames.SALES, LightSpeedXRetailHeaders.SALES),
            entry(LightSpeedXRetailEntityNames.SERIAL_NUMBERS, LightSpeedXRetailHeaders.SERIAL_NUMBERS),
            entry(LightSpeedXRetailEntityNames.SHIFTS, LightSpeedXRetailHeaders.SHIFTS),
            entry(LightSpeedXRetailEntityNames.SUPPLIERS, LightSpeedXRetailHeaders.SUPPLIERS),
            entry(LightSpeedXRetailEntityNames.TAGS, LightSpeedXRetailHeaders.TAGS),
            entry(LightSpeedXRetailEntityNames.TAXES, LightSpeedXRetailHeaders.TAXES),
            entry(LightSpeedXRetailEntityNames.USER, LightSpeedXRetailHeaders.USER),
            entry(LightSpeedXRetailEntityNames.VARIANT_ATTRIBUTES, LightSpeedXRetailHeaders.VARIANT_ATTRIBUTES),
            entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION, LightSpeedXRetailHeaders.PARTNER_SUBSCRIPTION),
            entry(LightSpeedXRetailEntityNames.EVENTS, LightSpeedXRetailHeaders.EVENTS),
            entry(LightSpeedXRetailEntityNames.REMOTE_RULES, LightSpeedXRetailHeaders.REMOTE_RULES),
            entry(LightSpeedXRetailEntityNames.WORKFLOW_RULES, LightSpeedXRetailHeaders.WORKFLOW_RULES),
            entry(LightSpeedXRetailEntityNames.WEBHOOKS, LightSpeedXRetailHeaders.WEBHOOKS),
            entry(LightSpeedXRetailEntityNames.CUSTOM_FIELDS, LightSpeedXRetailHeaders.CUSTOM_FIELDS),
            entry(LightSpeedXRetailEntityNames.CUSTOM_FIELD_VALUES, LightSpeedXRetailHeaders.CUSTOM_FIELD_VALUES),
            entry(LightSpeedXRetailEntityNames.CONSIGNMENT_PRODUCTS, LightSpeedXRetailHeaders.CONSIGNMENT_PRODUCTS),
            entry(LightSpeedXRetailEntityNames.SALES_PICK_LISTS, LightSpeedXRetailHeaders.SALES_PICK_LISTS),
            entry(LightSpeedXRetailEntityNames.PRODUCT_INVENTORY, LightSpeedXRetailHeaders.PRODUCT_INVENTORY),
            entry(LightSpeedXRetailEntityNames.USER_SALE_TOTALS, LightSpeedXRetailHeaders.USER_SALE_TOTALS),
            entry(LightSpeedXRetailEntityNames.CUSTOMER_STORE_CREDITS, LightSpeedXRetailHeaders.CUSTOMER_STORE_CREDITS),
            entry(LightSpeedXRetailEntityNames.CUSTOMER_STORE_CREDITS_REPORTS, LightSpeedXRetailHeaders.CUSTOMER_STORE_CREDITS_REPORTS),
            entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_INFO, LightSpeedXRetailHeaders.PARTNER_SUBSCRIPTION_INFO)
            // ---- NOT WORKING

//            entry(LightSpeedXRetailEntityNames.PAYMENT, LightSpeedXRetailHeaders.PAYMENT), //https://{domain_prefix}.retail.lightspeed.app/api/2.0/payments/{payment_id}
//            entry(LightSpeedXRetailEntityNames.SALES_PICK_LISTS, LightSpeedXRetailHeaders.SALES_PICK_LISTS), //https://{domain_prefix}.retail.lightspeed.app/api/2.0/sales/{sale_id}/pick_lists
//            entry(LightSpeedXRetailEntityNames.SALES_PICK_LISTS_LINE_ITEMS, LightSpeedXRetailHeaders.SALES_PICK_LISTS_LINE_ITEMS),
//            entry(LightSpeedXRetailEntityNames.PRICE_BOOKS_OUTLETS, LightSpeedXRetailHeaders.PRICE_BOOKS_OUTLETS),  // NOT FOUND
//            entry(LightSpeedXRetailEntityNames.PRICE_BOOKS_CUSTOMER_GROUPS, LightSpeedXRetailHeaders.PRICE_BOOKS_CUSTOMER_GROUPS), //https://{domain_prefix}.retail.lightspeed.app/api/2.0/price_books
//            entry(LightSpeedXRetailEntityNames.PRODUCT_CATEGORY_PATH, LightSpeedXRetailHeaders.PRODUCT_CATEGORY_PATH), //[Part-of] https://{domain_prefix}.retail.lightspeed.app/api/2.0/product_categories
//            entry(LightSpeedXRetailEntityNames.PRODUCTS_VARIANT_OPTIONS, LightSpeedXRetailHeaders.PRODUCTS_VARIANT_OPTIONS), // NOT FOUND
//           entry(LightSpeedXRetailEntityNames.PRODUCTS_IMAGES, LightSpeedXRetailHeaders.PRODUCTS_IMAGES), //https://{domain_prefix}.retail.lightspeed.app/api/2.0/product_images/{product_image_id}
//            entry(LightSpeedXRetailEntityNames.PRODUCT_SUPPLIERS, LightSpeedXRetailHeaders.PRODUCT_SUPPLIERS),
//            entry(LightSpeedXRetailEntityNames.PRODUCT_PRICE_STANDARD, LightSpeedXRetailHeaders.PRODUCT_PRICE_STANDARD),

//           entry(LightSpeedXRetailEntityNames.PROMO_CODE, LightSpeedXRetailHeaders.PROMO_CODE), // NOT FOUND
//           entry(LightSpeedXRetailEntityNames.PRODUCT_CATEGORIES, LightSpeedXRetailHeaders.PRODUCT_CATEGORIES),
//            entry(LightSpeedXRetailEntityNames.QUOTES_PRODUCTS, LightSpeedXRetailHeaders.QUOTES_PRODUCTS),
//            entry(LightSpeedXRetailEntityNames.QUOTES_PRODUCT_TAX_COMPONENTS, LightSpeedXRetailHeaders.QUOTES_PRODUCT_TAX_COMPONENTS),
//            entry(LightSpeedXRetailEntityNames.REGISTER_PAYMENT_SUMMARY, LightSpeedXRetailHeaders.REGISTER_PAYMENT_SUMMARY),
//            entry(LightSpeedXRetailEntityNames.SALES_LINE_ITEMS, LightSpeedXRetailHeaders.SALES_LINE_ITEMS),
//            entry(LightSpeedXRetailEntityNames.SALES_PAYMENTS, LightSpeedXRetailHeaders.SALES_PAYMENTS),
//            entry(LightSpeedXRetailEntityNames.SALES_TAXES, LightSpeedXRetailHeaders.SALES_TAXES),
//            entry(LightSpeedXRetailEntityNames.TAX_RATES, LightSpeedXRetailHeaders.TAX_RATES),
//            entry(LightSpeedXRetailEntityNames.GIFT_CARDS, LightSpeedXRetailHeaders.GIFT_CARDS),
//            entry(LightSpeedXRetailEntityNames.GIFT_CARDS_GIFT_CARD_TRANSACTIONS, LightSpeedXRetailHeaders.GIFT_CARDS_GIFT_CARD_TRANSACTIONS),
//            entry(LightSpeedXRetailEntityNames.USER_ROLES, LightSpeedXRetailHeaders.USER_ROLES), // NOT FOUND
//            entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_INFO_COMPONENTS, LightSpeedXRetailHeaders.PARTNER_SUBSCRIPTION_INFO_COMPONENTS),
//            entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_TOKEN, LightSpeedXRetailHeaders.PARTNER_SUBSCRIPTION_TOKEN),
//            entry(LightSpeedXRetailEntityNames.CUSTOMER_STORE_CREDIT_TRANSACTIONS, LightSpeedXRetailHeaders.CUSTOMER_STORE_CREDIT_TRANSACTIONS),

//            entry(LightSpeedXRetailEntityNames.SALES_FULFILLMENT, LightSpeedXRetailHeaders.SALES_FULFILLMENT),
//            entry(LightSpeedXRetailEntityNames.SALES_FULFILLMENT_LINE_ITEMS, LightSpeedXRetailHeaders.SALES_FULFILLMENT_LINE_ITEMS)

    );

    public static final Map<String, String> ENTITY_VERSION_MAP = Map.<String, String>ofEntries(
            Map.entry(LightSpeedXRetailEntityNames.EVENTS, "2.0"),                // (example)
            Map.entry(LightSpeedXRetailEntityNames.BRAND, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CHANNEL_REQUESTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CHANNELS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CONSIGNMENT_PRODUCTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CONSIGNMENTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CUSTOMER_GROUPS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CUSTOMERS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.INVENTORY, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.OUTLET_TAXES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.OUTLETS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PAYMENT_TYPES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PAYMENT, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.SALES_PICK_LISTS, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.SALES_PICK_LISTS_LINE_ITEMS, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRICE_BOOK, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRICE_BOOKS_OUTLETS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRICE_BOOKS_CUSTOMER_GROUPS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRICE_BOOK_PRODUCTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCT_CATEGORIES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCT_CATEGORY_PATH, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCT_IMAGES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCT_TYPES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCT_INVENTORY, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCTS_VARIANT_OPTIONS, "3.0"),
            //Map.entry(LightSpeedXRetailEntityNames.PRODUCTS_CATEGORY, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCTS_IMAGES, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCT_SUPPLIERS, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCT_PRICE_STANDARD, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.PROMOTIONS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PROMO_CODE, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.QUOTES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.QUOTES_PRODUCTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.QUOTES_PRODUCT_TAX_COMPONENTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.REGISTERS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.REGISTER_PAYMENT_SUMMARY, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.RETAILER, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.SALES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.SALES_LINE_ITEMS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.SALES_PAYMENTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.SALES_TAXES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.SERIAL_NUMBERS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.SHIFTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.SUPPLIERS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.TAGS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.TAXES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.TAX_RATES, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.USER, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.USER_ROLES, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.USER_SALE_TOTALS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.VARIANT_ATTRIBUTES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.GIFT_CARDS, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.GIFT_CARDS_GIFT_CARD_TRANSACTIONS, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_INFO, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_INFO_COMPONENTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_TOKEN, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CUSTOMER_STORE_CREDITS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CUSTOMER_STORE_CREDIT_TRANSACTIONS, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.CUSTOMER_STORE_CREDITS_REPORTS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CUSTOM_FIELDS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.CUSTOM_FIELD_VALUES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.REMOTE_RULES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.WORKFLOW_RULES, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.WEBHOOKS, "2.0"),
            Map.entry(LightSpeedXRetailEntityNames.SALES_FULFILLMENT, "3.0"),
            Map.entry(LightSpeedXRetailEntityNames.SALES_FULFILLMENT_LINE_ITEMS, "3.0")
    );

    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.<String,  List<String>>ofEntries(
            Map.entry(LightSpeedXRetailEntityNames.CONSIGNMENTS, List.of(LightSpeedXRetailEntityNames.CONSIGNMENT_PRODUCTS)),
            Map.entry(LightSpeedXRetailEntityNames.SALES, List.of(LightSpeedXRetailEntityNames.SALES_PICK_LISTS)),
            Map.entry(LightSpeedXRetailEntityNames.PRODUCTS, List.of(LightSpeedXRetailEntityNames.PRODUCT_INVENTORY)),
            Map.entry(LightSpeedXRetailEntityNames.USER, List.of(LightSpeedXRetailEntityNames.USER_SALE_TOTALS)),
            Map.entry(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION, List.of(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_INFO))
            );

    public static Map<String, String> ENTITY_API_PATH_MAP = new HashMap<>();

    public static final String[] dlhCommonColumns = new String[]{};

    public static List<String> excludedEntities = List.of();

    public static String getUrl(String entityName){
        return ENTITY_VERSION_MAP.get(entityName) + "/" + entityName.toLowerCase();
    }

    public static Map<String, String> getEntityApiPathMap(){
        ENTITY_API_PATH_MAP.put(LightSpeedXRetailEntityNames.CONSIGNMENT_PRODUCTS,
                ENTITY_VERSION_MAP.get(LightSpeedXRetailEntityNames.CONSIGNMENT_PRODUCTS)
                        +"/consignments/{consignment_id}/products");

        ENTITY_API_PATH_MAP.put(LightSpeedXRetailEntityNames.SALES_PICK_LISTS,
                ENTITY_VERSION_MAP.get(LightSpeedXRetailEntityNames.SALES_PICK_LISTS)
                        +"/sales/{sale_id}/pick_lists");

        ENTITY_API_PATH_MAP.put(LightSpeedXRetailEntityNames.PRODUCT_INVENTORY,
                ENTITY_VERSION_MAP.get(LightSpeedXRetailEntityNames.PRODUCT_INVENTORY)
                        +"/products/{product_id}/inventory");

        ENTITY_API_PATH_MAP.put(LightSpeedXRetailEntityNames.USER_SALE_TOTALS,
                ENTITY_VERSION_MAP.get(LightSpeedXRetailEntityNames.USER_SALE_TOTALS)
                        +"/users/{user_id}/sale_totals");

        ENTITY_API_PATH_MAP.put(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_INFO,
                ENTITY_VERSION_MAP.get(LightSpeedXRetailEntityNames.PARTNER_SUBSCRIPTION_INFO)
                        +"/partner/billing/subscriptions/{subscription_id}");

        for(String entity: HEADERS_BY_ENTITY.keySet()){
            if(!ENTITY_API_PATH_MAP.containsKey(entity)) {
                ENTITY_API_PATH_MAP.put(entity, getUrl(entity));
            }
        }
        return ENTITY_API_PATH_MAP;
    }
}