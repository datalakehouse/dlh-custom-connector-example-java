package io.datalakehouse.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreshDeskConstants {

    public static final String CONNECTOR_NAME = "FRESHDESK";
    public static final String URI_PREFIX_WITH_VERSION = "api/v2/";
    public static final String ID_HEADER = "ID";

    public static final class FreshDeskEntityNames {

        public static final String ACCOUNT = "ACCOUNT";
        public static final String ADMIN_GROUPS = "ADMIN_GROUPS";
        public static final String AGENT = "AGENT";
        public static final String BUSINESS_HOURS = "BUSINESS_HOURS";
        public static final String CANNED_RESPONSE_FOLDERS = "CANNED_RESPONSE_FOLDERS";
        public static final String CANNED_RESPONSES = "CANNED_RESPONSES";
        public static final String COMPANIES = "COMPANIES";
        public static final String COMPANY_FIELDS = "COMPANY_FIELDS";
        public static final String CONTACT_FIELDS = "CONTACT_FIELDS";
        public static final String CONTACTS = "CONTACTS";
        public static final String CUSTOM_OBJECT_SCHEMAS = "CUSTOM_OBJECT_SCHEMAS";
        public static final String CUSTOM_OBJECT_SCHEMA_RECORDS = "CUSTOM_OBJECT_SCHEMA_RECORDS";
        public static final String DISCUSSION_CATEGORIES_FORUMS = "DISCUSSION_CATEGORIES_FORUMS";
        public static final String DISCUSSION_FORUMS_TOPICS = "DISCUSSION_FORUMS_TOPICS";
        public static final String DISCUSSION_TOPIC_COMMENTS = "DISCUSSION_TOPIC_COMMENTS";
        public static final String DISCUSSIONS_CATEGORIES = "DISCUSSIONS_CATEGORIES";
        public static final String EMAIL_CONFIGS = "EMAIL_CONFIGS";
        public static final String EMAIL_MAILBOXES = "EMAIL_MAILBOXES";
        public static final String GROUPS = "GROUPS";
        public static final String GROUPS_AGENTS = "GROUPS_AGENTS";
        public static final String PRODUCTS = "PRODUCTS";
        public static final String ROLES = "ROLES";
        public static final String SCENARIO_AUTOMATION = "SCENARIO_AUTOMATION";
        public static final String SKILLS = "SKILLS";
        public static final String SLA_POLICIES = "SLA_POLICIES";
        public static final String SOLUTIONS_CATEGORIES = "SOLUTIONS_CATEGORIES";
        public static final String SURVEYS_SATISFACTION_RATINGS = "SURVEYS_SATISFACTION_RATINGS";
        public static final String SURVEYS = "SURVEYS";
        public static final String TICKET_FIELDS_SECTIONS = "TICKET_FIELDS_SECTIONS";
        public static final String TICKET_FIELDS = "TICKET_FIELDS";
        public static final String TICKET_FORMS = "TICKET_FORMS";
        public static final String TICKETS_CONVERSATIONS = "TICKETS_CONVERSATIONS";
        public static final String TICKETS_SATISFACTION_RATINGS = "TICKETS_SATISFACTION_RATINGS";
        public static final String TICKETS_SUMMARY = "TICKETS_SUMMARY";
        public static final String TICKETS_TIME_ENTRIES = "TICKETS_TIME_ENTRIES";
        public static final String TICKETS = "TICKETS";
        public static final String TIME_ENTRIES = "TIME_ENTRIES";

        private FreshDeskEntityNames() {}
    }

    public static final class FreshDeskHeaders {

        public static final String[] ACCOUNT = {"ORGANISATION_ID","ORGANISATION_NAME","ACCOUNT_ID","ACCOUNT_NAME","ACCOUNT_DOMAIN","BUNDLE_ID","HIPAA_COMPLIANT","TOTAL_AGENTS_FULL_TIME","TOTAL_AGENTS_OCCASIONAL","TOTAL_AGENTS_FIELD_SERVICE","TOTAL_AGENTS_COLLABORATORS","TIMEZONE","DATA_CENTER","TIER_TYPE","ADDRESS_COUNTRY","ADDRESS_STATE","ADDRESS_CITY","ADDRESS_STREET","ADDRESS_POSTAL_CODE","ADDRESS_ID","CONTACT_PERSON_FIRST_NAME","CONTACT_PERSON_LAST_NAME","CONTACT_PERSON_EMAIL","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] ADMIN_GROUPS = {"ID","NAME","DESCRIPTION","ESCALATE_TO","UNASSIGNED_FOR","AGENT_IDS","CREATED_AT","UPDATED_AT","ALLOW_AGENTS_TO_CHANGE_AVAILABILITY","BUSINESS_CALENDAR_ID","TYPE","AUTOMATIC_AGENT_ASSIGNMENT_ENABLED","AUTOMATIC_AGENT_ASSIGNMENT_TYPE","AUTOMATIC_AGENT_ASSIGNMENT_SETTINGS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] AGENT = {"ID","AVAILABLE","OCCASIONAL","SIGNATURE","TICKET_SCOPE","CREATED_AT","UPDATED_AT","AVAILABLE_SINCE","TYPE","CONTACT_ACTIVE","CONTACT_EMAIL","CONTACT_JOB_TITLE","CONTACT_LANGUAGE","CONTACT_LAST_LOGIN_AT","CONTACT_MOBILE","CONTACT_NAME","CONTACT_PHONE","CONTACT_TIME_ZONE","CONTACT_CREATED_AT","CONTACT_UPDATED_AT","FOCUS_MODE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] BUSINESS_HOURS = {"ID","NAME","DESCRIPTION","TIME_ZONE","IS_DEFAULT","BUSINESS_HOURS_MONDAY_START_TIME","BUSINESS_HOURS_MONDAY_END_TIME","BUSINESS_HOURS_TUESDAY_START_TIME","BUSINESS_HOURS_TUESDAY_END_TIME","BUSINESS_HOURS_WEDNESDAY_START_TIME","BUSINESS_HOURS_WEDNESDAY_END_TIME","BUSINESS_HOURS_THURSDAY_START_TIME","BUSINESS_HOURS_THURSDAY_END_TIME","BUSINESS_HOURS_FRIDAY_START_TIME","BUSINESS_HOURS_FRIDAY_END_TIME","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CANNED_RESPONSE_FOLDERS = {"ID","NAME","PERSONAL","RESPONSES_COUNT","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CANNED_RESPONSES = {"ID","NAME","CANNED_RESPONSES","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] COMPANIES = {"ID","NAME","DESCRIPTION","DOMAINS","NOTE","CREATED_AT","UPDATED_AT","CUSTOM_FIELDS","HEALTH_SCORE","ACCOUNT_TIER","RENEWAL_DATE","INDUSTRY","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_FIELDS = {"ID","NAME","LABEL","POSITION","REQUIRED_FOR_AGENTS","AGENTS_CAN_EDIT","DISPLAYED_FOR_AGENTS","QUICK_ADD_FOR_AGENT","UNIQUE","TYPE","DEFAULT","CREATED_AT","UPDATED_AT","CHOICES","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CONTACT_FIELDS = {"ID","EDITABLE_IN_SIGNUP","NAME","LABEL","POSITION","REQUIRED_FOR_AGENTS","AGENTS_CAN_EDIT","DISPLAYED_FOR_AGENTS","QUICK_ADD_FOR_AGENT","UNIQUE","TYPE","DEFAULT","CUSTOMERS_CAN_EDIT","LABEL_FOR_CUSTOMERS","REQUIRED_FOR_CUSTOMERS","DISPLAYED_FOR_CUSTOMERS","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CONTACTS = {"ID","ACTIVE","ADDRESS","COMPANY_ID","DESCRIPTION","EMAIL","CONTACT_TYPE","JOB_TITLE","LANGUAGE","MOBILE","NAME","PHONE","TIME_ZONE","TWITTER_ID","SOCIAL_HANDLER","CREATED_AT","UPDATED_AT","OTHER_COMPANIES","CUSTOM_FIELDS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CUSTOM_OBJECT_SCHEMAS = {"ID","NAME","PREFIX","TITLE","DESCRIPTION","VERSION","DELETED","CREATED_TIME","UPDATED_TIME","FIELDS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CUSTOM_OBJECT_SCHEMA_RECORDS = {"DISPLAY_ID","CREATED_TIME","UPDATED_TIME","DATA","METADATA","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] DISCUSSION_CATEGORIES_FORUMS = {"ID","NAME","DESCRIPTION","POSITION","FORUM_CATEGORY_ID","FORUM_TYPE","FORUM_VISIBILITY","TOPICS_COUNT","POSTS_COUNT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] DISCUSSION_FORUMS_TOPICS = {"ID","TITLE","FORUM_ID","USER_ID","LOCKED","PUBLISHED","STAMP_TYPE","REPLIED_BY","POSTS_COUNT","HITS","USER_VOTES","MERGED_TOPIC_ID","STICKY","CREATED_AT","UPDATED_AT","REPLIED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] DISCUSSION_TOPIC_COMMENTS = {"ID","BODY_TEXT","BODY","TOPIC_ID","FORUM_ID","USER_ID","ANSWER","PUBLISHED","SPAM","TRASH","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] DISCUSSIONS_CATEGORIES = {"ID","NAME","DESCRIPTION","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMAIL_CONFIGS = {"ID","NAME","PRODUCT_ID","TO_EMAIL","REPLY_EMAIL","GROUP_ID","PRIMARY_ROLE","ACTIVE","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMAIL_MAILBOXES = {"ID","NAME","SUPPORT_EMAIL","GROUP_ID","DEFAULT_REPLY_EMAIL","ACTIVE","MAILBOX_TYPE","CREATED_AT","UPDATED_AT","PRODUCT_ID","CUSTOM_MAILBOX","FRESHDESK_MAILBOX","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] GROUPS = {"ID","NAME","DESCRIPTION","BUSINESS_HOUR_ID","ESCALATE_TO","UNASSIGNED_FOR","AUTO_TICKET_ASSIGN","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] GROUPS_AGENTS = {"ID","TICKET_SCOPE","WRITE_ACCESS","ROLE_IDS","CONTACT_NAME","CONTACT_EMAIL","CONTACT_AVATAR","CREATED_AT","UPDATED_AT","FRESHCALLER_AGENT","FRESHCHAT_AGENT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PRODUCTS = {"ID","NAME","DESCRIPTION","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] ROLES = {"ID","NAME","DESCRIPTION","DEFAULT","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] SCENARIO_AUTOMATION = {"ID","NAME","DESCRIPTION","ACTIONS","PRIVATE","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] SKILLS = {"ID","NAME","RANK","CREATED_AT","UPDATED_AT","AGENTS","MATCH_TYPE","CONDITIONS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] SLA_POLICIES = {"ID","NAME","DESCRIPTION","ACTIVE","IS_DEFAULT","POSITION","SLA_TARGET","APPLICABLE_TO","ESCALATION","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] SOLUTIONS_CATEGORIES = {"ID","NAME","DESCRIPTION","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] SURVEYS_SATISFACTION_RATINGS = {"ID","SURVEY_ID","USER_ID","AGENT_ID","FEEDBACK","GROUP_ID","TICKET_ID","RATINGS","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] SURVEYS = {"ID","TITLE","QUESTIONS","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TICKET_FIELDS_SECTIONS = {"ID","LABEL","PARENT_TICKET_FIELD_ID","CHOICE_IDS","TICKET_FIELD_IDS","IS_FSM","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TICKET_FIELDS = {"ID","DESCRIPTION","LABEL","NAME","POSITION","TYPE","LABEL_FOR_CUSTOMERS","DEFAULT","REQUIRED_FOR_CLOSURE","REQUIRED_FOR_AGENTS","REQUIRED_FOR_CUSTOMERS","CUSTOMERS_CAN_EDIT","DISPLAYED_TO_CUSTOMERS","CREATED_AT","UPDATED_AT","CHOICES","NESTED_TICKETS_FIELDS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TICKET_FORMS = {"ID","NAME","TITLE","DEFAULT","DESCRIPTION","CREATED_AT","UPDATED_AT","LAST_UPDATED_BY","PORTALS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TICKETS_CONVERSATIONS = {"ID","BODY_TEXT","BODY","STRUCTURED_BODY","INCOMING","PRIVATE","USER_ID","SUPPORT_EMAIL","SOURCE","TICKET_ID","CREATED_AT","UPDATED_AT","FROM_EMAIL","TO_EMAILS","CC_EMAILS","BCC_EMAILS","ATTACHMENTS","LAST_EDITED_AT","LAST_EDITED_USER_ID","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TICKETS_SATISFACTION_RATINGS = {"ID","SURVEY_ID","USER_ID","AGENT_ID","FEEDBACK","GROUP_ID","TICKET_ID","RATINGS","CREATED_AT","UPDATED_AT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TICKETS_SUMMARY = {"ID","BODY","BODY_TEXT","USER_ID","TICKET_ID","CREATED_AT","UPDATED_AT","ATTACHMENTS","LAST_EDITED_AT","LAST_EDITED_USER_ID","CLOUD_FILES","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TICKETS_TIME_ENTRIES = {"ID","BILLABLE","NOTE","TIMER_RUNNING","AGENT_ID","TICKET_ID","TIME_SPENT","CREATED_AT","UPDATED_AT","EXECUTED_AT","START_TIME","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TICKETS = {"ID","CC_EMAILS","FWD_EMAILS","REPLY_CC_EMAILS","FR_ESCALATED","SPAM","EMAIL_CONFIG_ID","GROUP_ID","PRIORITY","REQUESTER_ID","RESPONDER_ID","SOURCE","STATUS","SUBJECT","TO_EMAILS","PRODUCT_ID","TYPE","CREATED_AT","UPDATED_AT","DUE_BY","FR_DUE_BY","IS_ESCALATED","STRUCTURED_DESCRIPTION","CUSTOM_FIELDS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TIME_ENTRIES = {"ID","BILLABLE","NOTE","TIMER_RUNNING","AGENT_ID","TICKET_ID","TIME_SPENT","CREATED_AT","UPDATED_AT","EXECUTED_AT","START_TIME","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};

        private FreshDeskHeaders() {}
    }

    public static List<String> excludedEntities = List.of();

    // Define entity dependencies
    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.ofEntries(
            Map.entry(FreshDeskEntityNames.TICKETS, List.of(FreshDeskEntityNames.TICKETS_CONVERSATIONS,
                    FreshDeskEntityNames.TICKETS_SATISFACTION_RATINGS,
                    FreshDeskEntityNames.TICKETS_TIME_ENTRIES)),
            Map.entry(FreshDeskEntityNames.CANNED_RESPONSE_FOLDERS, List.of(FreshDeskEntityNames.CANNED_RESPONSES)),
            Map.entry(FreshDeskEntityNames.TICKET_FIELDS, List.of(FreshDeskEntityNames.TICKET_FIELDS_SECTIONS)),
            Map.entry(FreshDeskEntityNames.DISCUSSIONS_CATEGORIES,
                    List.of(FreshDeskEntityNames.DISCUSSION_CATEGORIES_FORUMS)),
            Map.entry(FreshDeskEntityNames.DISCUSSION_CATEGORIES_FORUMS,
                    List.of(FreshDeskEntityNames.DISCUSSION_FORUMS_TOPICS)),
            Map.entry(FreshDeskEntityNames.DISCUSSION_FORUMS_TOPICS,
                    List.of(FreshDeskEntityNames.DISCUSSION_TOPIC_COMMENTS)),
            Map.entry(FreshDeskEntityNames.ADMIN_GROUPS, List.of(FreshDeskEntityNames.GROUPS_AGENTS))
                                                                                       );


    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.ofEntries(
        Map.entry(FreshDeskEntityNames.ACCOUNT, FreshDeskHeaders.ACCOUNT),
        Map.entry(FreshDeskEntityNames.ADMIN_GROUPS, FreshDeskHeaders.ADMIN_GROUPS),
        Map.entry(FreshDeskEntityNames.AGENT, FreshDeskHeaders.AGENT),
        Map.entry(FreshDeskEntityNames.BUSINESS_HOURS, FreshDeskHeaders.BUSINESS_HOURS),
        Map.entry(FreshDeskEntityNames.CANNED_RESPONSE_FOLDERS, FreshDeskHeaders.CANNED_RESPONSE_FOLDERS),
        Map.entry(FreshDeskEntityNames.CANNED_RESPONSES, FreshDeskHeaders.CANNED_RESPONSES),
        Map.entry(FreshDeskEntityNames.COMPANIES, FreshDeskHeaders.COMPANIES),
        Map.entry(FreshDeskEntityNames.COMPANY_FIELDS, FreshDeskHeaders.COMPANY_FIELDS),
        Map.entry(FreshDeskEntityNames.CONTACT_FIELDS, FreshDeskHeaders.CONTACT_FIELDS),
        Map.entry(FreshDeskEntityNames.CONTACTS, FreshDeskHeaders.CONTACTS),
        Map.entry(FreshDeskEntityNames.DISCUSSION_CATEGORIES_FORUMS, FreshDeskHeaders.DISCUSSION_CATEGORIES_FORUMS),
        Map.entry(FreshDeskEntityNames.DISCUSSION_FORUMS_TOPICS, FreshDeskHeaders.DISCUSSION_FORUMS_TOPICS),
        Map.entry(FreshDeskEntityNames.DISCUSSION_TOPIC_COMMENTS, FreshDeskHeaders.DISCUSSION_TOPIC_COMMENTS),
        Map.entry(FreshDeskEntityNames.DISCUSSIONS_CATEGORIES, FreshDeskHeaders.DISCUSSIONS_CATEGORIES),
        Map.entry(FreshDeskEntityNames.EMAIL_CONFIGS, FreshDeskHeaders.EMAIL_CONFIGS),
        Map.entry(FreshDeskEntityNames.EMAIL_MAILBOXES, FreshDeskHeaders.EMAIL_MAILBOXES),
        Map.entry(FreshDeskEntityNames.GROUPS, FreshDeskHeaders.GROUPS),
        Map.entry(FreshDeskEntityNames.GROUPS_AGENTS, FreshDeskHeaders.GROUPS_AGENTS),
        Map.entry(FreshDeskEntityNames.PRODUCTS, FreshDeskHeaders.PRODUCTS),
        Map.entry(FreshDeskEntityNames.ROLES, FreshDeskHeaders.ROLES),
        Map.entry(FreshDeskEntityNames.SCENARIO_AUTOMATION, FreshDeskHeaders.SCENARIO_AUTOMATION),
        Map.entry(FreshDeskEntityNames.SKILLS, FreshDeskHeaders.SKILLS),
        Map.entry(FreshDeskEntityNames.SLA_POLICIES, FreshDeskHeaders.SLA_POLICIES),
        Map.entry(FreshDeskEntityNames.SOLUTIONS_CATEGORIES, FreshDeskHeaders.SOLUTIONS_CATEGORIES),
        Map.entry(FreshDeskEntityNames.SURVEYS_SATISFACTION_RATINGS, FreshDeskHeaders.SURVEYS_SATISFACTION_RATINGS),
        Map.entry(FreshDeskEntityNames.SURVEYS, FreshDeskHeaders.SURVEYS),
        Map.entry(FreshDeskEntityNames.TICKET_FIELDS, FreshDeskHeaders.TICKET_FIELDS),
        Map.entry(FreshDeskEntityNames.TICKET_FORMS, FreshDeskHeaders.TICKET_FORMS),
        Map.entry(FreshDeskEntityNames.TIME_ENTRIES, FreshDeskHeaders.TIME_ENTRIES),
        Map.entry(FreshDeskEntityNames.TICKETS, FreshDeskHeaders.TICKETS),
        Map.entry(FreshDeskEntityNames.TICKETS_CONVERSATIONS, FreshDeskHeaders.TICKETS_CONVERSATIONS),
        Map.entry(FreshDeskEntityNames.TICKETS_SATISFACTION_RATINGS, FreshDeskHeaders.TICKETS_SATISFACTION_RATINGS),
        Map.entry(FreshDeskEntityNames.TICKETS_TIME_ENTRIES, FreshDeskHeaders.TICKETS_TIME_ENTRIES)
            //            Map.entry(FreshDeskEntityNames.TICKETS_SUMMARY, FreshDeskHeaders.TICKETS_SUMMARY),
            //        Map.entry(FreshDeskEntityNames.CUSTOM_OBJECT_SCHEMAS, FreshDeskHeaders.CUSTOM_OBJECT_SCHEMAS),
            //        Map.entry(FreshDeskEntityNames.CUSTOM_OBJECT_SCHEMA_RECORDS, FreshDeskHeaders.CUSTOM_OBJECT_SCHEMA_RECORDS),
            //            Map.entry(FreshDeskEntityNames.TICKET_FIELDS_SECTIONS, FreshDeskHeaders.TICKET_FIELDS_SECTIONS),
                                                                               );

    public static final Map<String, String[]> DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(FreshDeskEntityNames.TICKETS, FreshDeskHeaders.TICKETS),
            Map.entry(FreshDeskEntityNames.CONTACTS, FreshDeskHeaders.CONTACTS),
            Map.entry(FreshDeskEntityNames.COMPANIES, FreshDeskHeaders.COMPANIES));

    public static final Map<String, String[]> NON_DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(FreshDeskEntityNames.ACCOUNT, FreshDeskHeaders.ACCOUNT),
            Map.entry(FreshDeskEntityNames.ADMIN_GROUPS, FreshDeskHeaders.ADMIN_GROUPS),
            Map.entry(FreshDeskEntityNames.AGENT, FreshDeskHeaders.AGENT),
            Map.entry(FreshDeskEntityNames.BUSINESS_HOURS, FreshDeskHeaders.BUSINESS_HOURS),
            Map.entry(FreshDeskEntityNames.CANNED_RESPONSE_FOLDERS, FreshDeskHeaders.CANNED_RESPONSE_FOLDERS),
            Map.entry(FreshDeskEntityNames.CANNED_RESPONSES, FreshDeskHeaders.CANNED_RESPONSES),
            Map.entry(FreshDeskEntityNames.COMPANY_FIELDS, FreshDeskHeaders.COMPANY_FIELDS),
            Map.entry(FreshDeskEntityNames.CONTACT_FIELDS, FreshDeskHeaders.CONTACT_FIELDS),
            Map.entry(FreshDeskEntityNames.DISCUSSION_CATEGORIES_FORUMS, FreshDeskHeaders.DISCUSSION_CATEGORIES_FORUMS),
            Map.entry(FreshDeskEntityNames.DISCUSSION_FORUMS_TOPICS, FreshDeskHeaders.DISCUSSION_FORUMS_TOPICS),
            Map.entry(FreshDeskEntityNames.DISCUSSION_TOPIC_COMMENTS, FreshDeskHeaders.DISCUSSION_TOPIC_COMMENTS),
            Map.entry(FreshDeskEntityNames.DISCUSSIONS_CATEGORIES, FreshDeskHeaders.DISCUSSIONS_CATEGORIES),
            Map.entry(FreshDeskEntityNames.EMAIL_CONFIGS, FreshDeskHeaders.EMAIL_CONFIGS),
            Map.entry(FreshDeskEntityNames.EMAIL_MAILBOXES, FreshDeskHeaders.EMAIL_MAILBOXES),
            Map.entry(FreshDeskEntityNames.GROUPS, FreshDeskHeaders.GROUPS),
            Map.entry(FreshDeskEntityNames.GROUPS_AGENTS, FreshDeskHeaders.GROUPS_AGENTS),
            Map.entry(FreshDeskEntityNames.PRODUCTS, FreshDeskHeaders.PRODUCTS),
            Map.entry(FreshDeskEntityNames.ROLES, FreshDeskHeaders.ROLES),
            Map.entry(FreshDeskEntityNames.SCENARIO_AUTOMATION, FreshDeskHeaders.SCENARIO_AUTOMATION),
            Map.entry(FreshDeskEntityNames.SKILLS, FreshDeskHeaders.SKILLS),
            Map.entry(FreshDeskEntityNames.SLA_POLICIES, FreshDeskHeaders.SLA_POLICIES),
            Map.entry(FreshDeskEntityNames.SOLUTIONS_CATEGORIES, FreshDeskHeaders.SOLUTIONS_CATEGORIES),
            Map.entry(FreshDeskEntityNames.SURVEYS, FreshDeskHeaders.SURVEYS),
            Map.entry(FreshDeskEntityNames.TICKET_FIELDS, FreshDeskHeaders.TICKET_FIELDS),
            Map.entry(FreshDeskEntityNames.TICKET_FORMS, FreshDeskHeaders.TICKET_FORMS));

    public static String getUriPath(String entityName){
        return  URI_PREFIX_WITH_VERSION + entityName.toLowerCase();
    }

    public static Map<String, String> ENTITY_API_PATH_MAP = new HashMap<>();
    public static Map<String, String> getEntityApiPathMap() {

        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.ADMIN_GROUPS, URI_PREFIX_WITH_VERSION + "admin/groups");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.AGENT, URI_PREFIX_WITH_VERSION + "agents");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.GROUPS_AGENTS, URI_PREFIX_WITH_VERSION + "admin/groups/{groups_id}/agents");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.SKILLS, URI_PREFIX_WITH_VERSION + "admin/skills");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.TICKETS_CONVERSATIONS, URI_PREFIX_WITH_VERSION + "tickets/{tickets_id}/conversations");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.CANNED_RESPONSES, URI_PREFIX_WITH_VERSION + "canned_response_folders/{canned_response_folders_id}");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.DISCUSSIONS_CATEGORIES, URI_PREFIX_WITH_VERSION + "discussions/categories");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.DISCUSSION_CATEGORIES_FORUMS, URI_PREFIX_WITH_VERSION +
                "discussions/categories/{discussions_categories_id}/forums");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.DISCUSSION_FORUMS_TOPICS, URI_PREFIX_WITH_VERSION +
                "discussions/forums/{discussions_forums_id}/topics");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.DISCUSSION_TOPIC_COMMENTS, URI_PREFIX_WITH_VERSION +
                "discussions/topics/{discussion_forum_topics_id}/comments");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.EMAIL_MAILBOXES, URI_PREFIX_WITH_VERSION + "email/mailboxes");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.SCENARIO_AUTOMATION, URI_PREFIX_WITH_VERSION + "scenario_automations");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.TICKET_FORMS, URI_PREFIX_WITH_VERSION + "ticket-forms");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.TICKET_FIELDS_SECTIONS, URI_PREFIX_WITH_VERSION + "admin/ticket_fields/{ticket_fields_id}/sections");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.TICKETS_SATISFACTION_RATINGS, URI_PREFIX_WITH_VERSION + "tickets/{ticket_id}/satisfaction_ratings");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.TICKETS_TIME_ENTRIES, URI_PREFIX_WITH_VERSION + "tickets/{ticket_id}/time_entries");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.SURVEYS_SATISFACTION_RATINGS, URI_PREFIX_WITH_VERSION + "surveys/satisfaction_ratings");
        ENTITY_API_PATH_MAP.put(FreshDeskEntityNames.SOLUTIONS_CATEGORIES, URI_PREFIX_WITH_VERSION + "solutions/categories");

        HEADERS_BY_ENTITY.keySet().forEach(entity -> ENTITY_API_PATH_MAP.putIfAbsent(entity, getUriPath(entity)));
        return ENTITY_API_PATH_MAP;
    }

    private  FreshDeskConstants() {}
}
