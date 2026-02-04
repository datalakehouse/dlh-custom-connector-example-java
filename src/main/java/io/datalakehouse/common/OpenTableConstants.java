package io.datalakehouse.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenTableConstants {

    public static final String CONNECTOR_NAME = "OPEN_TABLE";
    public static final String[] ID_HEADERS = {"RID", "ID"};
    public static final long DEFAULT_HISTORICAL_DAYS = 90L;
    public static final String DELTA_QUERY_PARAM = "updated_after";
    public static final String BASE_URL = "https://platform.opentable.com";

    // Pagination constants
    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";
    public static final int MAX_PAGE_SIZE = 1000;
    public static final String HAS_NEXT_PAGE = "hasNextPage";
    public static final String ITEMS = "items";

    public static final class OpenTableEntityNames {

        public static final String CRM_GUESTS = "CRM_GUESTS";
        public static final String BOOKING_AVAILABILITY = "BOOKING_AVAILABILITY";
        public static final String REVIEWS_SUMMARY = "REVIEWS_SUMMARY";
        public static final String REVIEWS_DATA = "REVIEWS_DATA";
        public static final String SYNC_RESERVATIONS = "SYNC_RESERVATIONS";
        public static final String BOOKING_AVAILABILITY_METADATA = "BOOKING_AVAILABILITY_METADATA";
        public static final String BOOKING_ACTIVE_EXPERIENCES = "BOOKING_ACTIVE_EXPERIENCES";
        public static final String POS_RESTAURANTS = "POS_RESTAURANTS";
        public static final String SYNC_ORDERS = "SYNC_ORDERS";
        public static final String SYNC_ORDERS_POS_ORDER_ITEMS = "SYNC_ORDERS_POS_ORDER_ITEMS";
        public static final String DIRECTORY_RESTAURANTS = "DIRECTORY_RESTAURANTS";

        private OpenTableEntityNames() {}
    }

    public static final class OpenTableHeaders {

        public static final String[] BOOKING_AVAILABILITY = {"RID", "PARTY_SIZE", "BOOKING_AVAILABILITY_TIMES", "TIMES_AVAILABLE", "NO_AVAILABILITY_REASONS", "HREF", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CRM_GUESTS = {"ID", "HREF", "RID", "SEQUENCE_ID", "GPID", "FIRST_NAME", "LAST_NAME", "EMAIL", "EMAIL_OPTIN", "PHONE", "PHONE_TYPE", "TAGS", "BIRTH_DATE", "ANNIVERSARY_DATE", "UPDATED_AT", "FORGOTTEN", "ARCHIVED", "PRIMARY_GUEST", "ADDRESS", "MAIL_OPTED_IN", "COMPANY_NAME", "PHONE_NUMBERS", "CREATED_DATE", "UPDATED_AT_UTC", "MARKETING_OPTED_OUT", "NOTES", "NOTES_SPECIAL_RELATIONSHIP", "NOTES_FOOD_AND_DRINK", "NOTES_SEATING", "CREATED_DATE_UTC", "DATE_LAST_VISIT", "DATE_FIRST_VISIT", "DATE_LAST_VISIT_UTC", "DATE_FIRST_VISIT_UTC", "IS_HIDDEN", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] REVIEWS_SUMMARY = {"RID", "RATINGS_OVERALL_RATINGS_DISTRIBUTION", "RATINGS_DINERS_RECOMMENDATIONS_COUNT", "RATINGS_DINERS_RECOMMENDATIONS_YES", "RATINGS_DINERS_RECOMMENDATIONS_NO", "RATINGS_COUNT", "RATINGS_OVERALL", "RATINGS_FOOD_RATING", "RATINGS_FOOD_COUNT", "RATINGS_SERVICE_RATING", "RATINGS_SERVICE_COUNT", "RATINGS_AMBIENCE_RATING", "RATINGS_AMBIENCE_COUNT", "RATINGS_VALUE_RATING", "RATINGS_VALUE_COUNT", "RATINGS_NOISE_RATING", "RATINGS_NOISE_COUNT", "RATING_ONLY_REVIEW_RATINGS_OVERALL_RATINGS_DISTRIBUTION", "RATING_ONLY_REVIEW_RATINGS_DINERS_RECOMMENDATIONS_COUNT", "RATING_ONLY_REVIEW_RATINGS_DINERS_RECOMMENDATIONS_YES", "RATING_ONLY_REVIEW_RATINGS_DINERS_RECOMMENDATIONS_NO", "RATING_ONLY_REVIEW_RATINGS_COUNT", "RATING_ONLY_REVIEW_RATINGS_OVERALL_RATING", "RATING_ONLY_REVIEW_RATINGS_OVERALL_COUNT", "RATING_ONLY_REVIEW_RATINGS_FOOD_RATING", "RATING_ONLY_REVIEW_RATINGS_FOOD_COUNT", "RATING_ONLY_REVIEW_RATINGS_SERVICE_RATING", "RATING_ONLY_REVIEW_RATINGS_SERVICE_COUNT", "RATING_ONLY_REVIEW_RATINGS_AMBIENCE_RATING", "RATING_ONLY_REVIEW_RATINGS_AMBIENCE_COUNT", "RATING_ONLY_REVIEW_RATINGS_VALUE_RATING", "RATING_ONLY_REVIEW_RATINGS_VALUE_COUNT", "RATING_ONLY_REVIEW_RATINGS_NOISE_RATING", "RATING_ONLY_REVIEW_RATINGS_NOISE_COUNT", "CATEGORY_DISTRIBUTION", "TOTAL_NUMBER_OF_REVIEWS", "ALL_TIME_TEXT_REVIEW_COUNT", "SHOW_REVIEWS", "START_DATE_UTC", "AWARDS", "RESTAURANT_NAME", "LOCALE", "LOOK_BACK_DAYS", "RATING_BASED_ON", "VERIFIED_REVIEWS", "RESPONSE_LINK", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] REVIEWS_DATA = {"COUNTRY_CODE", "CUSTOMER_NAME", "CUSTOMER_NICKNAME", "CUSTOMER_EMAIL_ADDRESS", "DOMAIN_ID", "FEATURED", "LOCALE", "REVIEWS_DATA_LANGUAGE", "METRO_ID", "MODERATION_STATE", "SIMPLIFIED_MODERATION_STATE", "RATING_OVERALL", "RATING_FOOD", "RATING_SERVICE", "RATING_AMBIENCE", "RATING_VALUE", "RATING_NOISE", "RECOMMENDED", "RESTAURANT_COMMENT", "RESTAURANT_ID", "DINED_DATE_TIME", "RESERVATION_ID", "REVIEW_ID", "REVIEW_TEXT", "REVIEW_TITLE", "REVIEW_TYPE", "SUBMISSION_DATE_TIME_UTC", "CUSTOM_QUESTIONS", "NEIGHBORHOOD_ID", "GP_ID", "LAST_MODIFIED_DATE_TIME_UTC", "TAG_VOTES", "HELPFULNESS_UP", "HELPFULNESS_DOWN", "HELPFULNESS_SCORE", "UPDATED_BY_DINER_DATE_TIME_UTC", "IS_DRAFT", "CATEGORIES", "DINER_INITIALS", "DINER_METRO_ID", "DINER_IS_VIP", "PHOTOS", "ORIGINAL_SUBMISSION_DATE_TIME_UTC", "RESPONSE_LINK", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SYNC_RESERVATIONS = {"ID", "HREF", "RID", "SEQUENCE_ID", "GUEST_ID", "GUEST", "CONFIRMATION_ID", "SYNC_RESERVATIONS_STATE", "TABLE_NUMBER", "CREATED_DATE", "SCHEDULED_TIME", "PARTY_SIZE", "VISIT_TAGS", "ORIGIN", "UPDATED_AT", "SEATED_TIME", "DONE_TIME", "POS_DATA", "SCHEDULED_TIME_UTC", "MARKETING_OPTED_OUT", "GUEST_REQUEST", "VENUE_NOTES", "OPENTABLE_NOTES", "TABLE_CATEGORY", "SEATED_TIME_UTC", "DONE_TIME_UTC", "CREATED_DATE_UTC", "UPDATED_AT_UTC", "SERVER", "REFERRER", "ADDED_TO_WAITLIST", "ADDED_TO_WAITLIST_UTC", "ARRIVED_TIME", "ARRIVED_TIME_UTC", "CURRENCY_CODE", "CURRENCY_DENOMINATOR", "DEPOSIT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] BOOKING_AVAILABILITY_METADATA = {"ENVIRONMENTS", "ATTRIBUTES", "DINING_AREAS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] BOOKING_ACTIVE_EXPERIENCES = {"EXPERIENCE_ID", "ADD_ONS_GROUPS", "ADD_ONS_MAX_PER_RESERVATION", "ADD_ONS_SUMMARY_AVAILABLE", "ADD_ONS_SUMMARY_COUNT", "BOOKABLE", "CURRENCY", "DESCRIPTION", "NAME", "RID", "BOOKING_ACTIVE_EXPERIENCES_TYPE", "TYPE_ENUM", "TYPE_ID", "VERSION", "PRICE_INFO_PRICE_TYPE", "PRICE_INFO_PRE_PAYMENT_REQUIRED", "PRICE_INFO_CURRENCY_CODE", "PRICE_INFO_MULTIPLIER", "PRICE_INFO_GRATUITY_LABEL", "PRICE_INFO_GRATUITY_NUMERATOR", "PRICE_INFO_GRATUITY_DENOMINATOR", "PRICE_INFO_GRATUITY_MANDATORY", "PRICE_INFO_GRATUITY_TAXABLE", "PRICE_INFO_PRICES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] POS_RESTAURANTS = {"RID", "RESTAURANT_NAME", "SOURCE_LOCATION_ID", "POS_TYPE", "STATUS", "SOURCE_LOCATION_STATUS", "DATETIME_OF_FIRST_CHECK", "DATETIME_OF_LAST_CHECK", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SYNC_ORDERS = {"ID", "RID", "GUEST_ID", "CONFIRMATION_ID", "CURRENCY_CODE", "CURRENCY_DENOMINATOR", "CHECK_IDS", "POS_SUB_TOTAL", "POS_TAX", "POS_TIP", "POS_TOTAL_SPEND", "POS_ORDER_ITEMS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SYNC_ORDERS_POS_ORDER_ITEMS = {"ORDER_ID", "NAME", "QUANTITY", "COMMENT", "TICKET_ITEM_MODIFIERS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] DIRECTORY_RESTAURANTS = {"RID", "NAME", "ADDRESS", "ADDRESS2", "CITY", "DIRECTORY_RESTAURANTS_STATE", "COUNTRY", "LATITUDE", "LONGITUDE", "POSTAL_CODE", "PHONE_NUMBER", "METRO_NAME", "RESERVATION_URL", "PROFILE_URL", "NATURAL_RESERVATION_URL", "NATURAL_PROFILE_URL", "PROFILE_PHOTO_ID", "PROFILE_PHOTO_SIZES", "IS_RESTAURANT_IN_GROUP", "AGGREGATE_SCORE", "PRICE_QUARTILE", "REVIEW_COUNT", "CATEGORY", "DIRECTORY_RESTAURANTS_HOURS", "HOURS_RAW", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};

        private OpenTableHeaders() {}
    }

    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.ofEntries(
    );

    public static List<String> excludedEntities = List.of();

    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(OpenTableEntityNames.BOOKING_AVAILABILITY, OpenTableHeaders.BOOKING_AVAILABILITY),
            Map.entry(OpenTableEntityNames.REVIEWS_SUMMARY, OpenTableHeaders.REVIEWS_SUMMARY),
            Map.entry(OpenTableEntityNames.REVIEWS_DATA, OpenTableHeaders.REVIEWS_DATA),
            Map.entry(OpenTableEntityNames.SYNC_RESERVATIONS, OpenTableHeaders.SYNC_RESERVATIONS),
            Map.entry(OpenTableEntityNames.BOOKING_AVAILABILITY_METADATA, OpenTableHeaders.BOOKING_AVAILABILITY_METADATA),
            Map.entry(OpenTableEntityNames.BOOKING_ACTIVE_EXPERIENCES, OpenTableHeaders.BOOKING_ACTIVE_EXPERIENCES),
            Map.entry(OpenTableEntityNames.CRM_GUESTS, OpenTableHeaders.CRM_GUESTS),
            Map.entry(OpenTableEntityNames.POS_RESTAURANTS, OpenTableHeaders.POS_RESTAURANTS),
            Map.entry(OpenTableEntityNames.SYNC_ORDERS, OpenTableHeaders.SYNC_ORDERS),
            Map.entry(OpenTableEntityNames.DIRECTORY_RESTAURANTS, OpenTableHeaders.DIRECTORY_RESTAURANTS)
    );

    public static final Map<String, String[]> CHILD_TABLE_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(OpenTableEntityNames.SYNC_ORDERS, OpenTableHeaders.SYNC_ORDERS_POS_ORDER_ITEMS)
    );

    public static final Map<String, String[]> DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(OpenTableEntityNames.CRM_GUESTS, OpenTableHeaders.CRM_GUESTS),
            Map.entry(OpenTableEntityNames.SYNC_RESERVATIONS, OpenTableHeaders.SYNC_RESERVATIONS),
            Map.entry(OpenTableEntityNames.SYNC_ORDERS, OpenTableHeaders.SYNC_ORDERS)
    );

    public static final Map<String, String[]> NON_DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(OpenTableEntityNames.REVIEWS_SUMMARY, OpenTableHeaders.REVIEWS_SUMMARY),
            Map.entry(OpenTableEntityNames.REVIEWS_DATA, OpenTableHeaders.REVIEWS_DATA),
            Map.entry(OpenTableEntityNames.POS_RESTAURANTS, OpenTableHeaders.POS_RESTAURANTS),
            Map.entry(OpenTableEntityNames.DIRECTORY_RESTAURANTS, OpenTableHeaders.DIRECTORY_RESTAURANTS),
            Map.entry(OpenTableEntityNames.BOOKING_AVAILABILITY, OpenTableHeaders.BOOKING_AVAILABILITY),
            Map.entry(OpenTableEntityNames.BOOKING_ACTIVE_EXPERIENCES, OpenTableHeaders.BOOKING_ACTIVE_EXPERIENCES),
            Map.entry(OpenTableEntityNames.BOOKING_AVAILABILITY_METADATA, OpenTableHeaders.BOOKING_AVAILABILITY_METADATA)
    );

    public static Map<String, String> ENTITY_API_PATH_MAP = HashMap.newHashMap(HEADERS_BY_ENTITY.size());

    public static Map<String, String> getEntityApiPathMap(String restaurantId) {
        // Generate API path map
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.DIRECTORY_RESTAURANTS, "/sync/directory");
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.BOOKING_AVAILABILITY, "v2/availability/" + restaurantId);
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.REVIEWS_SUMMARY, "v1/partnerreviews/restaurantreviewsummary");
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.REVIEWS_DATA, "v1/partnerreviews/allreviews");
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.SYNC_RESERVATIONS, "sync/v2/reservations");
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.BOOKING_AVAILABILITY_METADATA, "v2/availability-metadata/" + restaurantId);
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.BOOKING_ACTIVE_EXPERIENCES, "v2/experiences/"+ restaurantId +"/active");
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.CRM_GUESTS,"sync/v2/guests");
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.POS_RESTAURANTS, "pos/restaurants");
        ENTITY_API_PATH_MAP.put(OpenTableEntityNames.SYNC_ORDERS, "sync/v2/pos-data");

        HEADERS_BY_ENTITY.keySet().forEach(entity -> ENTITY_API_PATH_MAP.putIfAbsent(entity, getUrl(entity)));
        return ENTITY_API_PATH_MAP;
    }

    public static String getUrl(String entityName) {
        return "v1/" + entityName.toLowerCase();
    }

    public static final Map<String, List<String>> CUSTOM_QUERY_PARAM_BY_ENTITY = Map.ofEntries(
            Map.entry(OpenTableEntityNames.BOOKING_AVAILABILITY, List.of("start_date_time", "include_experiences")),
            Map.entry(OpenTableEntityNames.REVIEWS_SUMMARY, List.of("Rid")),
            Map.entry(OpenTableEntityNames.REVIEWS_DATA, List.of("Rid", "StartDate")),
            Map.entry(OpenTableEntityNames.SYNC_RESERVATIONS, List.of("rid", "scheduled_time_from")),
            Map.entry(OpenTableEntityNames.CRM_GUESTS, List.of("rid")),
            Map.entry(OpenTableEntityNames.SYNC_ORDERS, List.of("rid", "scheduled_time_from"))
    );

    private OpenTableConstants() {}
}