package io.datalakehouse.common;

import java.util.List;
import java.util.Map;

public class GorgiasConstants {

    public static final String CONNECTOR_NAME = "GORGIAS";
    public static final String[] ID_HEADERS = {"ID"};
    public static final String API_PREFIX = "api";

    public static final class GorgiasEntityNames {

        public static final String VIEWS = "VIEWS";
        public static final String TEAMS = "TEAMS";
        public static final String WIDGETS = "WIDGETS";
        public static final String VOICE_CALL_EVENTS = "VOICE_CALL_EVENTS";
        public static final String TICKET_TAGS = "TICKET_TAGS";
        public static final String TICKET_FIELD_VALUES = "TICKET_FIELD_VALUES";
        public static final String ACCOUNT = "ACCOUNT";
        public static final String RULES = "RULES";
        public static final String USERS = "USERS";
        public static final String CUSTOMERS = "CUSTOMERS";
        public static final String ACCOUNT_SETTINGS = "ACCOUNT_SETTINGS";
        public static final String VOICE_CALLS = "VOICE_CALLS";
        public static final String JOBS = "JOBS";
        public static final String TAGS = "TAGS";
        public static final String CUSTOMER_FIELD_VALUES = "CUSTOMER_FIELD_VALUES";
        public static final String TICKET_MESSAGES = "TICKET_MESSAGES";
        public static final String SURVEYS = "SURVEYS";
        public static final String VOICE_CALL_RECORDINGS = "VOICE_CALL_RECORDINGS";
        public static final String TICKETS = "TICKETS";
        public static final String MACROS = "MACROS";
        public static final String INTEGRATIONS = "INTEGRATIONS";
        public static final String VIEWS_ITEMS = "VIEWS_ITEMS";

        private GorgiasEntityNames() {}
    }

    public static final class GorgiasHeaders {

        public static final String[] VIEWS = {"ID", "CATEGORY", "CREATED_DATETIME", "DEACTIVATED_DATETIME", "DECORATION_EMOJI", "FIELDS", "FILTERS", "NAME", "ORDER_BY", "ORDER_DIR", "SEARCH", "SHARED_WITH_TEAMS", "SHARED_WITH_USERS", "SLUG", "VIEWS_TYPE", "VISIBILITY", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TEAMS = {"ID", "NAME", "DESCRIPTION", "DECORATION", "MEMBERS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] WIDGETS = {"ID", "APP_ID", "CONTEXT", "CREATED_DATETIME", "DEACTIVATED_DATETIME", "WIDGETS_ORDER", "TEMPLATE_TYPE", "TEMPLATE_WIDGETS", "WIDGETS_TYPE", "UPDATED_DATETIME", "URI", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] VOICE_CALL_EVENTS = {"ID", "ACCOUNT_ID", "CALL_ID", "CREATED_DATETIME", "CUSTOMER_ID", "VOICE_CALL_EVENTS_TYPE", "USER_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TICKET_TAGS = {"ID", "TICKET_ID", "CREATED_DATETIME", "DECORATION", "DELETED_DATETIME", "DESCRIPTION", "NAME", "USAGE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TICKET_FIELD_VALUES = {"ID", "TICKET_ID", "FIELD_CREATED_DATETIME", "FIELD_DEACTIVATED_DATETIME", "FIELD_DEFINITION", "FIELD_DESCRIPTION", "FIELD_EXTERNAL_ID", "FIELD_ID", "FIELD_LABEL", "FIELD_MANAGED_TYPE", "FIELD_OBJECT_TYPE", "FIELD_PRIORITY", "FIELD_REQUIRED", "FIELD_REQUIREMENT_TYPE", "FIELD_UPDATED_DATETIME", "PREDICATION", "TICKET_FIELD_VALUES_VALUE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ACCOUNT = {"CREATED_DATETIME", "DEACTIVATED_DATETIME", "DOMAIN", "STATUS", "CURRENT_SUBSCRIPTION", "META", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] RULES = {"ID", "CODE", "CODE_AST_BODY", "CODE_AST_LOC", "CODE_AST_SOURCE_TYPE", "CODE_AST_TYPE", "CREATED_DATETIME", "DEACTIVATED_DATETIME", "DESCRIPTION", "EVENT_TYPES", "NAME", "RULES_PRIORITY", "RULES_TYPE", "UPDATED_DATETIME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] USERS = {"ID", "ACTIVE", "BIO", "CLIENT_ID", "COUNTRY", "CREATED_DATETIME", "DEACTIVATED_DATETIME", "EMAIL", "EXTERNAL_ID", "FIRSTNAME", "HAS_PASSWORD", "HAS_2FA_ENABLED", "USERS_LANGUAGE", "LASTNAME", "META", "NAME", "ROLE_NAME", "TIMEZONE", "UPDATED_DATETIME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOMERS = {"ID", "CREATED_DATETIME", "EMAIL", "ACTIVE", "EXTERNAL_ID", "FIRSTNAME", "USERS_LANGUAGE", "LASTNAME", "NAME", "TIMEZONE", "UPDATED_DATETIME", "META", "NOTE", "CUSTOM_FIELDS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] ACCOUNT_SETTINGS = {"ID", "ACCOUNT_SETTINGS_DATA", "ACCOUNT_SETTINGS_TYPE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] VOICE_CALLS = {"ID", "ANSWERED_BY_EXTERNAL_CUSTOMER_ID", "ANSWERED_BY_EXTERNAL_NUMBER", "COUNTRY_DESTINATION", "COUNTRY_SOURCE", "CREATED_DATETIME", "CUSTOMER_ID", "DIRECTION", "DURATION", "EXTERNAL_ID", "HAS_CALL_RECORDING", "HAS_VOICEMAIL", "INITIATED_BY_AGENT_ID", "INTEGRATION_ID", "LAST_ANSWERED_BY_AGENT_ID", "LAST_MONITORING_AGENT_ID", "LAST_RANG_AGENT_ID", "MONITORING_STATUS", "PHONE_NUMBER_DESTINATION", "PHONE_NUMBER_ID", "PHONE_NUMBER_SOURCE", "PROVIDER", "QUEUE_ID", "STARTED_DATETIME", "STATUS", "STATUS_IN_QUEUE", "SUMMARIES", "TERMINATION_STATUS", "TICKET_ID", "UPDATED_DATETIME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] JOBS = {"ID", "CANCEL_REQUESTED_DATETIME", "CANCELLED_DATETIME", "CREATED_DATETIME", "ENDED_DATETIME", "FAILED_DATETIME", "INFO_PROGRESS_COUNT", "LOCKED_DATETIME", "META_DESCRIPTION", "PARAMS_TICKET_IDS", "PARAMS_UPDATES_STATUS", "SCHEDULED_DATETIME", "STARTED_DATETIME", "STATUS", "JOBS_TYPE", "URI", "USER_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TAGS = {"ID", "CREATED_DATETIME", "DECORATION_COLOR", "DELETED_DATETIME", "DESCRIPTION", "NAME", "URI", "USAGE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CUSTOMER_FIELD_VALUES = {"CUSTOMER_ID", "FIELD_CREATED_DATETIME", "FIELD_DEACTIVATED_DATETIME", "FIELD_DEFINITION_DATA_TYPE", "FIELD_DEFINITION_INPUT_SETTINGS_PLACEHOLDER", "FIELD_DESCRIPTION", "FIELD_EXTERNAL_ID", "FIELD_ID", "FIELD_LABEL", "FIELD_MANAGED_TYPE", "FIELD_OBJECT_TYPE", "FIELD_PRIORITY", "FIELD_REQUIRED", "FIELD_UPDATED_DATETIME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TICKET_MESSAGES = {"ID", "ACTIONS", "ATTACHMENTS", "AUTH_CUSTOMER_IDENTITY_IDENTIFIER", "AUTH_CUSTOMER_IDENTITY_SERVICE_ID", "AUTH_CUSTOMER_IDENTITY_SERVICE_NAME", "BODY_HTML", "BODY_TEXT", "CHANNEL", "CREATED_DATETIME", "DELETED_DATETIME", "EXTERNAL_ID", "FAILED_DATETIME", "FROM_AGENT", "HEADERS", "INTEGRATION_ID", "INTENTS", "IS_RETRIABLE", "LAST_SENDING_ERROR", "MACROS", "MESSAGE_ID", "OPENED_DATETIME", "PUBLIC", "RECEIVER_ID", "RECEIVER_NAME", "RECEIVER_EMAIL", "REPLIED_BY", "REPLIED_TO", "RULE_ID", "SENDER_ID", "SENDER_NAME", "SENDER_EMAIL", "SENT_DATETIME", "SOURCE_FROM_NAME", "SOURCE_FROM_ADDRESS", "SOURCE_TO", "SOURCE_TYPE", "SOURCE_EXTRA", "STRIPPED_HTML", "STRIPPED_SIGNATURE", "STRIPPED_TEXT", "TICKET_MESSAGES_SUBJECT", "TICKET_ID", "VIA", "META", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] SURVEYS = {"ID", "BODY_TEXT", "CREATED_DATETIME", "CUSTOMER_ID", "META_EXTERNAL_DATA_ID", "SCORE", "SCORED_DATETIME", "SENT_DATETIME", "SHOULD_SEND_DATETIME", "TICKET_ID", "URI", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] VOICE_CALL_RECORDINGS = {"ID", "CALL_ID", "CREATED_DATETIME", "DELETED_DATETIME", "DURATION", "ERROR_CODE", "EXTERNAL_ID", "TRANSCRIPTION_STATUS", "VOICE_CALL_RECORDINGS_TYPE", "VOICE_CALL_RECORDINGS_URL", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TICKETS = {"ID", "ASSIGNEE_TEAM_ID", "ASSIGNEE_USER_ID", "CHANNEL", "CLOSED_DATETIME", "CREATED_DATETIME", "CUSTOM_FIELDS", "CUSTOMER_ID", "EXCERPT", "EXTERNAL_ID", "FROM_AGENT", "INTEGRATIONS_ADDRESS", "INTEGRATIONS_NAME", "INTEGRATIONS_TYPE", "IS_UNREAD", "TICKETS_LANGUAGE", "LAST_MESSAGE_DATETIME", "LAST_RECEIVED_MESSAGE_DATETIME", "LAST_SENT_MESSAGE_NOT_DELIVERED", "MESSAGES_COUNT", "OPENED_DATETIME", "TICKETS_PRIORITY", "SNOOZE_DATETIME", "SPAM", "STATUS", "TICKETS_SUBJECT", "SUMMARY_CONTENT", "SUMMARY_CREATED_DATETIME", "SUMMARY_TRIGGERED_BY", "SUMMARY_UPDATED_DATETIME", "TAGS", "TRASHED_DATETIME", "UPDATED_DATETIME", "URI", "VIA", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MACROS = {"ID", "ACTIONS", "ARCHIVED_DATETIME", "CREATED_DATETIME", "EXTERNAL_ID", "INTENT", "MACROS_LANGUAGE", "NAME", "UPDATED_DATETIME", "USAGE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] INTEGRATIONS = {"ID", "APPLICATION_ID", "BUSINESS_HOURS_ID", "CREATED_DATETIME", "DEACTIVATED_DATETIME", "DECORATION", "DESCRIPTION", "HTTP", "LOCKED_DATETIME", "META", "MANAGED", "NAME", "INTEGRATIONS_TYPE", "UPDATED_DATETIME", "USER_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] VIEWS_ITEMS = {"VIEWS_ID", "ASSIGNEE_TEAM_ID", "ASSIGNEE_USER_ID", "CUSTOM_FIELDS_6_ID", "CUSTOM_FIELDS_6_VALUE", "CUSTOMER_EMAIL", "CUSTOMER_FIRSTNAME", "CUSTOMER_ID", "CUSTOMER_LASTNAME", "CUSTOMER_NAME", "VIEWS_ITEMS_LANGUAGE", "REQUESTER_EMAIL", "REQUESTER_FIRSTNAME", "REQUESTER_ID", "REQUESTER_LASTNAME", "REQUESTER_NAME", "SUBJECT", "TAGS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};

        private GorgiasHeaders() {}
    }

    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.ofEntries(
            Map.entry(GorgiasEntityNames.CUSTOMERS, List.of(GorgiasEntityNames.CUSTOMER_FIELD_VALUES)),
            Map.entry(GorgiasEntityNames.TICKETS, List.of(GorgiasEntityNames.TICKET_TAGS, GorgiasEntityNames.TICKET_FIELD_VALUES, GorgiasEntityNames.TICKET_MESSAGES)),
            Map.entry(GorgiasEntityNames.VIEWS, List.of(GorgiasEntityNames.VIEWS_ITEMS))
    );

    public static final List<String> PAGINATION_HEADERS = List.of(
            GorgiasEntityNames.CUSTOMERS, GorgiasEntityNames.TICKETS, GorgiasEntityNames.SURVEYS,
            GorgiasEntityNames.CUSTOMER_FIELD_VALUES, GorgiasEntityNames.TICKET_TAGS,
            GorgiasEntityNames.TICKET_FIELD_VALUES, GorgiasEntityNames.TICKET_MESSAGES,
            GorgiasEntityNames.VOICE_CALL_EVENTS, GorgiasEntityNames.VOICE_CALL_RECORDINGS,
            GorgiasEntityNames.VOICE_CALLS, GorgiasEntityNames.JOBS, GorgiasEntityNames.MACROS,
            GorgiasEntityNames.INTEGRATIONS, GorgiasEntityNames.RULES, GorgiasEntityNames.TAGS,
            GorgiasEntityNames.VIEWS, GorgiasEntityNames.WIDGETS, GorgiasEntityNames.TEAMS
    );

    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(GorgiasEntityNames.VIEWS, GorgiasHeaders.VIEWS),
            Map.entry(GorgiasEntityNames.TEAMS, GorgiasHeaders.TEAMS),
            Map.entry(GorgiasEntityNames.WIDGETS, GorgiasHeaders.WIDGETS),
            Map.entry(GorgiasEntityNames.VOICE_CALL_EVENTS, GorgiasHeaders.VOICE_CALL_EVENTS),
            Map.entry(GorgiasEntityNames.TICKET_TAGS, GorgiasHeaders.TICKET_TAGS),
            Map.entry(GorgiasEntityNames.TICKET_FIELD_VALUES, GorgiasHeaders.TICKET_FIELD_VALUES),
            Map.entry(GorgiasEntityNames.ACCOUNT, GorgiasHeaders.ACCOUNT),
            Map.entry(GorgiasEntityNames.RULES, GorgiasHeaders.RULES),
            Map.entry(GorgiasEntityNames.USERS, GorgiasHeaders.USERS),
            Map.entry(GorgiasEntityNames.CUSTOMERS, GorgiasHeaders.CUSTOMERS),
            Map.entry(GorgiasEntityNames.ACCOUNT_SETTINGS, GorgiasHeaders.ACCOUNT_SETTINGS),
            Map.entry(GorgiasEntityNames.VOICE_CALLS, GorgiasHeaders.VOICE_CALLS),
            Map.entry(GorgiasEntityNames.JOBS, GorgiasHeaders.JOBS),
            Map.entry(GorgiasEntityNames.TAGS, GorgiasHeaders.TAGS),
            Map.entry(GorgiasEntityNames.CUSTOMER_FIELD_VALUES, GorgiasHeaders.CUSTOMER_FIELD_VALUES),
            Map.entry(GorgiasEntityNames.TICKET_MESSAGES, GorgiasHeaders.TICKET_MESSAGES),
            Map.entry(GorgiasEntityNames.SURVEYS, GorgiasHeaders.SURVEYS),
            Map.entry(GorgiasEntityNames.VOICE_CALL_RECORDINGS, GorgiasHeaders.VOICE_CALL_RECORDINGS),
            Map.entry(GorgiasEntityNames.TICKETS, GorgiasHeaders.TICKETS),
            Map.entry(GorgiasEntityNames.MACROS, GorgiasHeaders.MACROS),
            Map.entry(GorgiasEntityNames.INTEGRATIONS, GorgiasHeaders.INTEGRATIONS),
            Map.entry(GorgiasEntityNames.VIEWS_ITEMS, GorgiasHeaders.VIEWS_ITEMS)
    );

    public static final Map<String, String> ENTITY_API_PATH_MAP = Map.ofEntries(
            Map.entry(GorgiasEntityNames.VIEWS, API_PREFIX.concat("/views")),
            Map.entry(GorgiasEntityNames.TEAMS, API_PREFIX.concat("/teams")),
            Map.entry(GorgiasEntityNames.WIDGETS, API_PREFIX.concat("/widgets")),
            Map.entry(GorgiasEntityNames.VOICE_CALL_EVENTS, API_PREFIX.concat("/phone/voice-call-events")),
            Map.entry(GorgiasEntityNames.TICKET_TAGS, API_PREFIX.concat("/tickets/{ticket_id}/tags")),
            Map.entry(GorgiasEntityNames.TICKET_FIELD_VALUES, API_PREFIX.concat("/tickets/{ticket_id}/custom-fields")),
            Map.entry(GorgiasEntityNames.ACCOUNT, API_PREFIX.concat("/account")),
            Map.entry(GorgiasEntityNames.RULES, API_PREFIX.concat("/rules")),
            Map.entry(GorgiasEntityNames.USERS, API_PREFIX.concat("/users")),
            Map.entry(GorgiasEntityNames.CUSTOMERS, API_PREFIX.concat("/customers")),
            Map.entry(GorgiasEntityNames.ACCOUNT_SETTINGS, API_PREFIX.concat("/account/settings")),
            Map.entry(GorgiasEntityNames.VOICE_CALLS, API_PREFIX.concat("/phone/voice-calls")),
            Map.entry(GorgiasEntityNames.JOBS, API_PREFIX.concat("/jobs")),
            Map.entry(GorgiasEntityNames.TAGS, API_PREFIX.concat("/tags")),
            Map.entry(GorgiasEntityNames.CUSTOMER_FIELD_VALUES, API_PREFIX.concat("/customers/{customer_id}/custom-fields")),
            Map.entry(GorgiasEntityNames.TICKET_MESSAGES, API_PREFIX.concat("/tickets/{ticket_id}/messages")),
            Map.entry(GorgiasEntityNames.SURVEYS, API_PREFIX.concat("/satisfaction-surveys")),
            Map.entry(GorgiasEntityNames.VOICE_CALL_RECORDINGS, API_PREFIX.concat("/phone/voice-call-recordings")),
            Map.entry(GorgiasEntityNames.TICKETS, API_PREFIX.concat("/tickets")),
            Map.entry(GorgiasEntityNames.MACROS, API_PREFIX.concat("/macros")),
            Map.entry(GorgiasEntityNames.INTEGRATIONS, API_PREFIX.concat("/integrations")),
            Map.entry(GorgiasEntityNames.VIEWS_ITEMS, API_PREFIX.concat("/views/{views_id}/items"))
    );

    public static List<String> excludedEntities = List.of();

    private GorgiasConstants() {}
}