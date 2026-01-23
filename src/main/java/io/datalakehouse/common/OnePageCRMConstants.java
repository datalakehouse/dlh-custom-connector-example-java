package io.datalakehouse.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnePageCRMConstants {

    public static final String CONNECTOR_NAME = "ONE_PAGE_CRM";
    public static final String BASE_API_URL = "https://app.onepagecrm.com/api/v3";

    public static final String PER_PAGE = "per_page";
    public static final String PAGE = "page";
    public static final int MAX_PAGE_SIZE = 100;
    public static final String PAGINATION_META_TOTAL_COUNT = "total_count";
    public static final String PAGINATION_META_MAX_PAGE = "max_page";

    // Map of plural entity names to their singular forms
    // OnePageCRM API returns: [{"data": {"companies": [{"company": {...}}]}}]
    private static final Map<String, String> ENTITY_SINGULAR_MAP = new HashMap<>();

    static {
        // OnePageCRM entities
        ENTITY_SINGULAR_MAP.put("users", "user");
        ENTITY_SINGULAR_MAP.put("companies", "company");
        ENTITY_SINGULAR_MAP.put("contacts", "contact");
        ENTITY_SINGULAR_MAP.put("deals", "deal");
        ENTITY_SINGULAR_MAP.put("actions", "action");
        ENTITY_SINGULAR_MAP.put("notes", "note");
        ENTITY_SINGULAR_MAP.put("calls", "call");
        ENTITY_SINGULAR_MAP.put("leads", "lead");
        ENTITY_SINGULAR_MAP.put("pipelines", "pipeline");
        ENTITY_SINGULAR_MAP.put("tags", "tag");
        ENTITY_SINGULAR_MAP.put("custom_fields", "custom_field");
        ENTITY_SINGULAR_MAP.put("statuses", "status");
        ENTITY_SINGULAR_MAP.put("filters", "filter");
        ENTITY_SINGULAR_MAP.put("teams", "team");
    }

    /**
     * Gets the singular form of an entity from the map.
     * Falls back to removing trailing 's' if not in map.
     */
    public static String getSingularForm(String entityPlural) {
        String normalized = entityPlural.toLowerCase().replace("-", "_").replace("/", "_");
        return ENTITY_SINGULAR_MAP.getOrDefault(normalized,
                normalized.endsWith("s") ? normalized.substring(0, normalized.length() - 1) : normalized);
    }

    public static final class OnePageCRMEntityNames {
        public static final String ACTIONS = "ACTIONS";
        public static final String CALLS = "CALLS";
        public static final String COMPANIES = "COMPANIES";
        public static final String COMPANY_FIELDS = "COMPANY_FIELDS";
        public static final String CONTACTS = "CONTACTS";
        public static final String CUSTOM_FIELDS = "CUSTOM_FIELDS";
        public static final String DEAL_FIELDS = "DEAL_FIELDS";
        public static final String DEALS = "DEALS";
        public static final String FILTERS = "FILTERS";
        public static final String LEAD_SOURCES = "LEAD_SOURCES";
        public static final String MEETINGS = "MEETINGS";
        public static final String NOTES = "NOTES";
        public static final String NOTIFICATIONS = "NOTIFICATIONS";
        public static final String PIPELINES = "PIPELINES";
        public static final String PREDEFINED_ACTIONS = "PREDEFINED_ACTIONS";
        public static final String PREDEFINED_ITEMS = "PREDEFINED_ITEMS";
        public static final String PREDEFINED_ITEM_GROUPS = "PREDEFINED_ITEM_GROUPS";
        public static final String RELATIONSHIP_TYPES = "RELATIONSHIP_TYPES";
        public static final String STATUSES = "STATUSES";
        public static final String USERS = "USERS";
        public static final String DEALS_ITEMS = "DEALS_ITEMS";

        private OnePageCRMEntityNames() {}
    }

    public static final class OnePageCRMHeaders {
        public static final String[] ACTIONS = {"ID","ASSIGNEE_ID","CONTACT_ID","ACTIONS_TEXT","STATUS","ACTIONS_DATE","DONE","DONE_AT","CREATED_AT","MODIFIED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CALLS = {"ID","CONTACT_ID","CALLS_TEXT","CALL_RESULT","CALL_TIME_INT","VIA","PHONE_NUMBER","RECORDING_LINK","AUTHOR","ATTACHMENTS","CREATED_AT","MODIFIED_AT","LAST_TIMELINE_UPDATE","CALLS_INDEX","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] COMPANIES = {"ID","IMAGE","NAME","DESCRIPTION","PHONE","PHOTO_URL","URL","ADDRESS","ADDRESS_CITY","ADDRESS_STATE","ADDRESS_ZIP_CODE","ADDRESS_COUNTRY_CODE","COMPANY_FIELDS","SYNCING_STATUS","SYNCED_STATUS_ID","SYNCING_TAGS","SYNCED_TAGS","CONTACTS_COUNT","WON_DEALS_COUNT","TOTAL_WON_AMOUNT","PENDING_DEALS_COUNT","TOTAL_PENDING_AMOUNT","PENDING_DEALS","CREATED_AT","MODIFIED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_FIELDS = {"ID","NAME","COMPANY_FIELDS_TYPE","POSITION","CHOICES","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CONTACTS = {"ID","FIRST_NAME","LAST_NAME","JOB_TITLE","STARRED","PHOTO_URL","COMPANY_ID","COMPANY_NAME","URL","PHONES","EMAILS","ADDRESS_LIST","STATUS","STATUS_ID","TAGS","LEAD_SOURCE_ID","LEAD_SOURCE","BACKGROUND","OWNER_ID","CUSTOM_FIELDS","LETTER","PENDING_DEAL","TOTAL_PENDINGS","TOTAL_DEALS_COUNT","COMPANY_SIZE","SALES_CLOSED_FOR","CLOSED_SALES","GOOGLE_CONTACTS_DATA","CREATED_AT","MODIFIED_AT","ENHANCEABLE","LAST_TIMELINE_UPDATE","EMAIL_SYNC_AVAILABLE","EMAIL_SYNC_ENABLED","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CUSTOM_FIELDS = {"ID","NAME","CUSTOM_FIELDS_TYPE","POSITION","MANDATORY","CHOICES","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] DEAL_FIELDS = {"ID","NAME","DEAL_FIELD_TYPE","POSITION","CHOICES","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] DEALS = {"ID","CONTACT_ID","OWNER_ID","OWNER_NAME","OWNER_EMAIL","NAME","DEALS_TEXT","DEALS_DATE","CONTACT_INFO","AUTHOR","AMOUNT","MONTHS","TOTAL_AMOUNT","COST","TOTAL_COST","MARGIN","COMMISSION","COMMISSION_PERCENTAGE","COMMISSION_TYPE","COMMISSION_BASE","STATUS","HAS_RELATED_NOTES","ATTACHMENTS","DEAL_FIELDS","HAS_DEAL_ITEMS","CREATED_AT","MODIFIED_AT","PIPELINE_ID","SALES_PIPELINE_ID","LAST_TIMELINE_UPDATE","DEALS_STAGE","EXPECTED_CLOSE_DATE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] DEALS_ITEMS = {"ID","NAME","DESCRIPTION","COST","PRICE","AMOUNT","QTY","DEAL_ID","PREDEFINED_ITEM_ID","POSITION","CREATED_AT","MODIFIED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] FILTERS = {"ID","NAME","CONDITIONS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] LEAD_SOURCES = {"ID","LEAD_SOURCES_TEXT","COUNTS","TOTAL_COUNT","ACTION_STREAM_COUNT","TEAM_COUNTS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] MEETINGS = {"ID","CONTACT_ID","MEETING_TEXT","MEETING_TIME_INT","PLACE","AUTHOR","ATTACHMENTS","CREATED_AT","MODIFIED_AT","LAST_TIMELINE_UPDATE","MEETING_INDEX","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] NOTES = {"ID","CONTACT_ID","NOTES_TEXT","NOTES_DATE","LINKED_DEAL_ID","LINKED_DEAL_NAME","AUTHOR","ATTACHMENTS","CREATED_AT","MODIFIED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] NOTIFICATIONS = {"ID","NOTIFICATIONS_TYPE","NOTIFICATIONS_BY","NOTIFICATIONS_READ","USER_ID","CONTACT_ID","CREATED_AT","MODIFIED_AT","MOBILE_DATA_TITLE","MOBILE_DATA_BODY","MOBILE_DATA_ACTION_TYPE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PIPELINES = {"ID","NAME","PIPELINES_TYPE","PIPELINES_DEFAULT","WON_COLUMN_ENABLED","WON_COLUMN_NAME","STAGES","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PREDEFINED_ACTIONS = {"ID","PREDEFINED_ACTIONS_TEXT","PREDEFINED_ACTIONS_DAYS","POSITION","ACTION_GROUP_ID","VARIABLE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PREDEFINED_ITEMS = {"ID","NAME","DESCRIPTION","COST","PRICE","PREDEFINED_ITEMS_TYPE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PREDEFINED_ITEM_GROUPS = {"ID","NAME","POSITION","COUNT","ITEM_IDS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] RELATIONSHIP_TYPES = {"ID","SYMMETRICAL","RELATIONSHIP_VARIANTS","CREATED_AT","MODIFIED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] STATUSES = {"ID","STATUS","STATUSES_TEXT","DESCRIPTION","COLOR","COUNTS","TOTAL_COUNT","ACTION_STREAM_COUNT","TEAM_COUNTS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] USERS = {"ID","FIRST_NAME","LAST_NAME","EMAIL","COMPANY_NAME","ACCOUNT_RIGHTS","PHOTO_URL","COUNTRY_CODE","ACCOUNT_ROLE","BCC_EMAIL","GOOGLE_CONTACTS_EMAIL","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};

        private OnePageCRMHeaders() {}
    }

    public static List<String> excludedEntities = List.of();

    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(OnePageCRMEntityNames.ACTIONS, OnePageCRMHeaders.ACTIONS),
            Map.entry(OnePageCRMEntityNames.CALLS, OnePageCRMHeaders.CALLS),
            Map.entry(OnePageCRMEntityNames.COMPANIES, OnePageCRMHeaders.COMPANIES),
            Map.entry(OnePageCRMEntityNames.COMPANY_FIELDS, OnePageCRMHeaders.COMPANY_FIELDS),
            Map.entry(OnePageCRMEntityNames.CONTACTS, OnePageCRMHeaders.CONTACTS),
            Map.entry(OnePageCRMEntityNames.CUSTOM_FIELDS, OnePageCRMHeaders.CUSTOM_FIELDS),
            Map.entry(OnePageCRMEntityNames.DEAL_FIELDS, OnePageCRMHeaders.DEAL_FIELDS),
            Map.entry(OnePageCRMEntityNames.DEALS, OnePageCRMHeaders.DEALS),
            Map.entry(OnePageCRMEntityNames.FILTERS, OnePageCRMHeaders.FILTERS),
            Map.entry(OnePageCRMEntityNames.LEAD_SOURCES, OnePageCRMHeaders.LEAD_SOURCES),
            Map.entry(OnePageCRMEntityNames.MEETINGS, OnePageCRMHeaders.MEETINGS),
            Map.entry(OnePageCRMEntityNames.NOTES, OnePageCRMHeaders.NOTES),
            Map.entry(OnePageCRMEntityNames.NOTIFICATIONS, OnePageCRMHeaders.NOTIFICATIONS),
            Map.entry(OnePageCRMEntityNames.PIPELINES, OnePageCRMHeaders.PIPELINES),
            Map.entry(OnePageCRMEntityNames.PREDEFINED_ACTIONS, OnePageCRMHeaders.PREDEFINED_ACTIONS),
            Map.entry(OnePageCRMEntityNames.PREDEFINED_ITEMS, OnePageCRMHeaders.PREDEFINED_ITEMS),
            Map.entry(OnePageCRMEntityNames.PREDEFINED_ITEM_GROUPS, OnePageCRMHeaders.PREDEFINED_ITEM_GROUPS),
            Map.entry(OnePageCRMEntityNames.RELATIONSHIP_TYPES, OnePageCRMHeaders.RELATIONSHIP_TYPES),
            Map.entry(OnePageCRMEntityNames.STATUSES, OnePageCRMHeaders.STATUSES),
            Map.entry(OnePageCRMEntityNames.USERS, OnePageCRMHeaders.USERS));


    // Define delta headers by entity
    public static final Map<String, String[]> DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(OnePageCRMEntityNames.CONTACTS, OnePageCRMHeaders.CONTACTS),
            Map.entry(OnePageCRMEntityNames.ACTIONS, OnePageCRMHeaders.ACTIONS),
            Map.entry(OnePageCRMEntityNames.DEALS, OnePageCRMHeaders.DEALS),
            Map.entry(OnePageCRMEntityNames.NOTES, OnePageCRMHeaders.NOTES),
            Map.entry(OnePageCRMEntityNames.CALLS, OnePageCRMHeaders.CALLS),
            Map.entry(OnePageCRMEntityNames.MEETINGS, OnePageCRMHeaders.MEETINGS),
            Map.entry(OnePageCRMEntityNames.RELATIONSHIP_TYPES, OnePageCRMHeaders.RELATIONSHIP_TYPES));

    public static final Map<String, String[]> NON_DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(OnePageCRMEntityNames.COMPANIES, OnePageCRMHeaders.COMPANIES),
            Map.entry(OnePageCRMEntityNames.COMPANY_FIELDS, OnePageCRMHeaders.COMPANY_FIELDS),
            Map.entry(OnePageCRMEntityNames.CUSTOM_FIELDS, OnePageCRMHeaders.CUSTOM_FIELDS),
            Map.entry(OnePageCRMEntityNames.DEAL_FIELDS, OnePageCRMHeaders.DEAL_FIELDS),
            Map.entry(OnePageCRMEntityNames.FILTERS, OnePageCRMHeaders.FILTERS),
            Map.entry(OnePageCRMEntityNames.LEAD_SOURCES, OnePageCRMHeaders.LEAD_SOURCES),
            Map.entry(OnePageCRMEntityNames.NOTIFICATIONS, OnePageCRMHeaders.NOTIFICATIONS),
            Map.entry(OnePageCRMEntityNames.PIPELINES, OnePageCRMHeaders.PIPELINES),
            Map.entry(OnePageCRMEntityNames.PREDEFINED_ACTIONS, OnePageCRMHeaders.PREDEFINED_ACTIONS),
            Map.entry(OnePageCRMEntityNames.PREDEFINED_ITEMS, OnePageCRMHeaders.PREDEFINED_ITEMS),
            Map.entry(OnePageCRMEntityNames.PREDEFINED_ITEM_GROUPS, OnePageCRMHeaders.PREDEFINED_ITEM_GROUPS),
            Map.entry(OnePageCRMEntityNames.STATUSES, OnePageCRMHeaders.STATUSES),
            Map.entry(OnePageCRMEntityNames.USERS, OnePageCRMHeaders.USERS));

    public static String getUriPath(String entityName){
        return entityName.toLowerCase();
    }

    public static Map<String, String> ENTITY_API_PATH_MAP = new HashMap<>();
    public static Map<String, String> getEntityApiPathMap() {
        HEADERS_BY_ENTITY.keySet().forEach(entity -> ENTITY_API_PATH_MAP.putIfAbsent(entity, getUriPath(entity)));
        return ENTITY_API_PATH_MAP;
    }

    private OnePageCRMConstants() {}
}
