package io.datalakehouse.common;

import java.util.List;
import java.util.Map;

public class BambooHRConstants {

    public static final String CONNECTOR_NAME = "BAMBOO_HR";
    public static final String[] ID_HEADERS = {"ID", "EMPLOYEE_ID"};
    public static final String URI_PREFIX_WITH_VERSION = "api/v1/";

    // Pagination constants
    public static final String PAGE_AFTER = "page[after]";
    public static final String PAGE_LIMIT = "page[limit]";
    public static final int MAX_PAGE_SIZE = 50;
    public static final String META = "meta";
    public static final String PAGE = "page";
    public static final String NEXT_CURSOR = "nextCursor";
    public static final String DATA = "data";

    public static final class BambooHREntityNames {

        public static final String APPLICANT_STATUSES = "APPLICANT_STATUSES";
        public static final String BENEFIT_COVERAGES = "BENEFIT_COVERAGES";
        public static final String BENEFIT_DEDUCTION_TYPES = "BENEFIT_DEDUCTION_TYPES";
        public static final String COMPANY_BENEFITS = "COMPANY_BENEFITS";
        public static final String COMPANY_FILE_CATEGORIES = "COMPANY_FILE_CATEGORIES";
        public static final String COMPANY_FILES = "COMPANY_FILES";
        public static final String COMPANY_INFORMATION = "COMPANY_INFORMATION";
        public static final String COMPANY_LOCATIONS = "COMPANY_LOCATIONS";
        public static final String STATES = "STATES";
        public static final String COUNTRIES = "COUNTRIES";
        public static final String EMPLOYEE_BENEFITS = "EMPLOYEE_BENEFITS";
        public static final String EMPLOYEE_DEPENDENTS = "EMPLOYEE_DEPENDENTS";
        public static final String EMPLOYEE_FILE_CATEGORIES = "EMPLOYEE_FILE_CATEGORIES";
        public static final String EMPLOYEE_TRAINING_TYPES = "EMPLOYEE_TRAINING_TYPES";
        public static final String EMPLOYEE_TRAININGS = "EMPLOYEE_TRAININGS";
        public static final String EMPLOYEES = "EMPLOYEES";
        public static final String GOALS = "GOALS";
        public static final String JOB_APPLICATIONS = "JOB_APPLICATIONS";
        public static final String JOB_SUMMARY = "JOB_SUMMARY";
        public static final String MEMBER_BENEFITS = "MEMBER_BENEFITS";
        public static final String MILESTONES = "MILESTONES";
        public static final String TIME_OFF_POLICIES = "TIME_OFF_POLICIES";
        public static final String TIME_OFF_RECORDS = "TIME_OFF_RECORDS";
        public static final String TIME_OFF_REQUESTS = "TIME_OFF_REQUESTS";
        public static final String TIME_SHEET_ENTRIES = "TIME_SHEET_ENTRIES";
        public static final String USERS = "USERS";

        private BambooHREntityNames() {}
    }

    public static final class BambooHRHeaders {

        public static final String[] APPLICANT_STATUSES = {"ID", "CODE", "DESCRIPTION", "ENABLED", "MANAGEABLE", "NAME", "TRANSLATED_NAME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] BENEFIT_COVERAGES = {"ID", "SHORT_NAME", "DESCRIPTION", "SORT_ORDER", "BENEFIT_PLAN_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] BENEFIT_DEDUCTION_TYPES = {"ID", "DEDUCTION_TYPE_NAME", "DEFAULT_DEDUCTION_CODE", "ALLOWABLE_BENEFIT_TYPES", "NON_BENEFIT_DEDUCTION_TYPE", "CAN_BE_COLLECTED_BY_TRAX", "ADDITIONAL_DESCRIPTION", "HIDE_ANNUAL_MAX", "MANAGED_DEDUCTION_TYPE", "SUB_TYPES", "SUB_TYPE_TEXT", "DEDUCTION_NOTE", "DEDUCTION_NOTE_LINK", "DEDUCTION_NOTE_LINK_TEXT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_BENEFITS = {"ID", "BENEFIT_VENDOR_ID", "COMPANY_DEDUCTION_ID", "DEDUCTION_TYPE_ID", "END_DATE", "NAME", "START_DATE", "COMPANY_BENEFITS_TYPE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_FILE_CATEGORIES = {"ID", "CAN_UPLOAD_FILES", "NAME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_FILES = {"ID", "COMPANY_FILE_CATEGORIES_ID", "NAME", "ORIGINAL_FILE_NAME", "COMPANY_FILES_SIZE", "DATE_CREATED", "CREATED_BY", "SHARE_WITH_EMPLOYEES", "CAN_RENAME_FILE", "CAN_DELETE_FILE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_INFORMATION = {"LEGAL_NAME", "DISPLAY_NAME", "ADDRESS_LINE1", "ADDRESS_LINE2", "ADDRESS_CITY", "ADDRESS_STATE", "ADDRESS_ZIP", "PHONE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_LOCATIONS = {"ID", "NAME", "DESCRIPTION", "CITY", "STATE_ID", "COUNTRY_ID", "ZIPCODE", "ADDRESS_LINE1", "ADDRESS_LINE2", "PHONE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] STATES = {"ID", "COUNTRY_ID", "LABEL", "ISO", "NAME", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COUNTRIES = {"ID", "NAME", "ISO_CODE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_BENEFITS = {"EMPLOYEE_ID", "COMPANY_BENEFIT_ID", "COMPANY_BENEFIT_NAME", "COVERAGE_LEVEL", "DEDUCTION_END_DATE", "DEDUCTION_START_DATE", "ENROLLMENT_STATUS", "EFFECTIVE_DATE", "CURRENCY_CODE", "EMPLOYEE_AMOUNT", "EMPLOYEE_AMOUNT_TYPE", "EMPLOYEE_PERCENT_BASED_ON", "EMPLOYEE_CAP_AMOUNT", "EMPLOYEE_CAP_AMOUNT_TYPE", "EMPLOYEE_ANNUAL_MAX", "COMPANY_AMOUNT", "COMPANY_AMOUNT_TYPE", "COMPANY_PERCENT_BASED_ON", "COMPANY_CAP_AMOUNT", "COMPANY_CAP_AMOUNT_TYPE", "COMPANY_ANNUAL_MAX", "BENEFIT_PLAN_COVERAGE_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_DEPENDENTS = {"ID", "EMPLOYEE_ID", "FIRST_NAME", "MIDDLE_NAME", "LAST_NAME", "RELATIONSHIP", "GENDER", "MASKED_SSN", "MASKED_SIN", "DATE_OF_BIRTH", "ADDRESS_LINE1", "ADDRESS_LINE2", "CITY", "EMPLOYEE_DEPENDENTS_STATE", "ZIP_CODE", "HOME_PHONE", "COUNTRY", "IS_US_CITIZEN", "IS_STUDENT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_FILE_CATEGORIES = {"ID", "EMPLOYEE_ID", "NAME", "CAN_RENAME_CATEGORY", "CAN_DELETE_CATEGORY", "CAN_UPLOAD_FILES", "DISPLAY_IF_EMPTY", "FILES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_TRAINING_TYPES = {"ID", "NAME", "RENEWABLE", "FREQUENCY", "DUE_FROM_HIRE_DATE", "EMPLOYEE_TRAINING_TYPES_REQUIRED", "CATEGORY_ID", "CATEGORY_NAME", "LINK_URL", "DESCRIPTION", "ALLOW_EMPLOYEES_TO_MARK_COMPLETE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_TRAININGS = {"ID", "EMPLOYEE_ID", "COMPLETED", "NOTES", "INSTRUCTOR", "CREDITS", "EMPLOYEE_TRAININGS_HOURS", "COST", "EMPLOYEE_TRAININGS_TYPE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEES = {"EMPLOYEE_ID", "FIRST_NAME", "LAST_NAME", "PREFERRED_NAME", "PHOTO_URL", "JOB_TITLE_NAME", "STATUS", "_RESTRICTED_FIELDS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] GOALS = {"ID", "EMPLOYEE_ID", "TITLE", "DESCRIPTION", "PERCENT_COMPLETE", "ALIGNS_WITH_OPTION_ID", "SHARED_WITH_EMPLOYEE_IDS", "DUE_DATE", "COMPLETION_DATE", "STATUS", "ACTIONS_CAN_EDIT_GOAL_PROGRESS_BAR", "ACTIONS_CAN_EDIT_GOAL_MILESTONE_PROGRESS_BAR", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] JOB_APPLICATIONS = {"ID", "APPLIED_DATE", "STATUS_ID", "STATUS_LABEL", "RATING", "APPLICANT_ID", "APPLICANT_FIRST_NAME", "APPLICANT_LAST_NAME", "APPLICANT_AVATAR", "APPLICANT_EMAIL", "APPLICANT_SOURCE", "JOB_ID", "JOB_TITLE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] JOB_SUMMARY = {"ID", "TITLE_ID", "TITLE_LABEL", "POSTED_DATE", "LOCATION_ID", "DEPARTMENT_ID", "DEPARTMENT_LABEL", "STATUS_ID", "STATUS_LABEL", "HIRING_LEAD_EMPLOYEE_ID", "NEW_APPLICANTS_COUNT", "ACTIVE_APPLICANTS_COUNT", "TOTAL_APPLICANTS_COUNT", "POSTING_URL", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MEMBER_BENEFITS = {"MEMBER_ID", "SUBSCRIBER_ID", "PLANS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] MILESTONES = {"ID", "EMPLOYEE_GOAL_ID", "TITLE", "CURRENT_VALUE", "START_VALUE", "END_VALUE", "COMPLETED_DATE_TIME", "LAST_UPDATE_DATE_TIME", "LAST_UPDATE_USER_ID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TIME_OFF_POLICIES = {"ID", "TIME_OFF_TYPE_ID", "NAME", "EFFECTIVE_DATE", "TIME_OFF_POLICIES_TYPE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TIME_OFF_RECORDS = {"ID", "TIME_OFF_RECORDS_TYPE", "EMPLOYEE_ID", "NAME", "TIME_OFF_RECORDS_START", "TIME_OFF_RECORDS_END", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TIME_OFF_REQUESTS = {"ID", "EMPLOYEE_ID", "STATUS_LAST_CHANGED", "STATUS_LAST_CHANGED_BY_USER_ID", "STATUS_STATUS", "NAME", "TIME_OFF_REQUESTS_START", "TIME_OFF_REQUESTS_END", "CREATED", "TYPE_ID", "TYPE_NAME", "TYPE_ICON", "AMOUNT_UNIT", "AMOUNT_AMOUNT", "ACTIONS_VIEW", "ACTIONS_EDIT", "ACTIONS_CANCEL", "ACTIONS_APPROVE", "ACTIONS_DENY", "ACTIONS_BYPASS", "DATES", "NOTES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] TIME_SHEET_ENTRIES = {"ID", "EMPLOYEE_ID", "TIME_SHEET_ENTRIES_TYPE", "TIME_SHEET_ENTRIES_DATE", "TIME_SHEET_ENTRIES_START", "TIME_SHEET_ENTRIES_END", "TIME_SHEET_ENTRIES_TIMEZONE", "TIME_SHEET_ENTRIES_HOURS", "NOTE", "PROJECT_INFO_PROJECT_ID", "PROJECT_INFO_PROJECT_NAME", "PROJECT_INFO_TASK_ID", "PROJECT_INFO_TASK_NAME", "APPROVED_AT", "APPROVED", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] USERS = {"ID", "EMPLOYEE_ID", "FIRST_NAME", "LAST_NAME", "EMAIL", "STATUS", "LAST_LOGIN", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};

        private BambooHRHeaders() {}
    }

    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.ofEntries(
            Map.entry(BambooHREntityNames.COUNTRIES, List.of(BambooHREntityNames.STATES)),
            Map.entry(BambooHREntityNames.EMPLOYEES, List.of(BambooHREntityNames.EMPLOYEE_DEPENDENTS,
                    BambooHREntityNames.EMPLOYEE_FILE_CATEGORIES, BambooHREntityNames.EMPLOYEE_TRAININGS))
    );

    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(BambooHREntityNames.APPLICANT_STATUSES, BambooHRHeaders.APPLICANT_STATUSES),
            Map.entry(BambooHREntityNames.BENEFIT_COVERAGES, BambooHRHeaders.BENEFIT_COVERAGES),
            Map.entry(BambooHREntityNames.BENEFIT_DEDUCTION_TYPES, BambooHRHeaders.BENEFIT_DEDUCTION_TYPES),
            Map.entry(BambooHREntityNames.COMPANY_BENEFITS, BambooHRHeaders.COMPANY_BENEFITS),
            Map.entry(BambooHREntityNames.COMPANY_FILE_CATEGORIES, BambooHRHeaders.COMPANY_FILE_CATEGORIES),
            Map.entry(BambooHREntityNames.COMPANY_INFORMATION, BambooHRHeaders.COMPANY_INFORMATION),
            Map.entry(BambooHREntityNames.COMPANY_LOCATIONS, BambooHRHeaders.COMPANY_LOCATIONS),
            Map.entry(BambooHREntityNames.STATES, BambooHRHeaders.STATES),
            Map.entry(BambooHREntityNames.COUNTRIES, BambooHRHeaders.COUNTRIES),
            Map.entry(BambooHREntityNames.EMPLOYEE_BENEFITS, BambooHRHeaders.EMPLOYEE_BENEFITS),
            Map.entry(BambooHREntityNames.EMPLOYEE_DEPENDENTS, BambooHRHeaders.EMPLOYEE_DEPENDENTS),
            Map.entry(BambooHREntityNames.EMPLOYEE_FILE_CATEGORIES, BambooHRHeaders.EMPLOYEE_FILE_CATEGORIES),
            Map.entry(BambooHREntityNames.EMPLOYEE_TRAINING_TYPES, BambooHRHeaders.EMPLOYEE_TRAINING_TYPES),
            Map.entry(BambooHREntityNames.EMPLOYEE_TRAININGS, BambooHRHeaders.EMPLOYEE_TRAININGS),
            Map.entry(BambooHREntityNames.EMPLOYEES, BambooHRHeaders.EMPLOYEES),
            Map.entry(BambooHREntityNames.GOALS, BambooHRHeaders.GOALS),
            Map.entry(BambooHREntityNames.JOB_APPLICATIONS, BambooHRHeaders.JOB_APPLICATIONS),
            Map.entry(BambooHREntityNames.JOB_SUMMARY, BambooHRHeaders.JOB_SUMMARY),
            Map.entry(BambooHREntityNames.MEMBER_BENEFITS, BambooHRHeaders.MEMBER_BENEFITS),
            Map.entry(BambooHREntityNames.TIME_OFF_POLICIES, BambooHRHeaders.TIME_OFF_POLICIES),
            Map.entry(BambooHREntityNames.TIME_OFF_RECORDS, BambooHRHeaders.TIME_OFF_RECORDS),
            Map.entry(BambooHREntityNames.TIME_OFF_REQUESTS, BambooHRHeaders.TIME_OFF_REQUESTS),
            Map.entry(BambooHREntityNames.TIME_SHEET_ENTRIES, BambooHRHeaders.TIME_SHEET_ENTRIES),
            Map.entry(BambooHREntityNames.USERS, BambooHRHeaders.USERS)
    );

    public static final List<String> PAGINATION_HEADERS = List.of(
            BambooHREntityNames.EMPLOYEES, BambooHREntityNames.JOB_APPLICATIONS, BambooHREntityNames.MEMBER_BENEFITS,
            BambooHREntityNames.TIME_OFF_REQUESTS
    );

    public static final Map<String, String> MAPPING_TABLES_MAP = Map.ofEntries(
            Map.entry(BambooHREntityNames.COMPANY_FILE_CATEGORIES, BambooHREntityNames.COMPANY_FILES),
            Map.entry(BambooHREntityNames.GOALS, BambooHREntityNames.MILESTONES)
    );

    public static final Map<String, String[]> CHILD_TABLE_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(BambooHREntityNames.COMPANY_FILES, BambooHRHeaders.COMPANY_FILES),
            Map.entry(BambooHREntityNames.MILESTONES, BambooHRHeaders.MILESTONES)
    );

    public static final Map<String, String> ENTITY_API_PATH_MAP = Map.ofEntries(
            Map.entry(BambooHREntityNames.APPLICANT_STATUSES, URI_PREFIX_WITH_VERSION + "applicant_tracking/statuses"),
            Map.entry(BambooHREntityNames.BENEFIT_COVERAGES, URI_PREFIX_WITH_VERSION + "benefitcoverages"),
            Map.entry(BambooHREntityNames.BENEFIT_DEDUCTION_TYPES, URI_PREFIX_WITH_VERSION+ "benefits/settings/deduction_types/all"),
            Map.entry(BambooHREntityNames.COMPANY_BENEFITS, URI_PREFIX_WITH_VERSION + "benefit/company_benefit"),
            Map.entry(BambooHREntityNames.COMPANY_FILE_CATEGORIES, URI_PREFIX_WITH_VERSION + "files/view"),
            Map.entry(BambooHREntityNames.COMPANY_INFORMATION, URI_PREFIX_WITH_VERSION + "company_information"),
            Map.entry(BambooHREntityNames.COMPANY_LOCATIONS, URI_PREFIX_WITH_VERSION + "applicant_tracking/locations"),
            Map.entry(BambooHREntityNames.STATES,URI_PREFIX_WITH_VERSION + "meta/provinces/{country_id}"),
            Map.entry(BambooHREntityNames.COUNTRIES,URI_PREFIX_WITH_VERSION + "meta/countries/options"),
            Map.entry(BambooHREntityNames.EMPLOYEE_BENEFITS,URI_PREFIX_WITH_VERSION + "benefit/employee_benefit"),
            Map.entry(BambooHREntityNames.EMPLOYEE_DEPENDENTS,URI_PREFIX_WITH_VERSION + "employeedependents?employeeid={employee_id}"),
            Map.entry(BambooHREntityNames.EMPLOYEE_FILE_CATEGORIES,URI_PREFIX_WITH_VERSION + "employees/{employee_id}/files/view"),
            Map.entry(BambooHREntityNames.EMPLOYEE_TRAINING_TYPES,URI_PREFIX_WITH_VERSION + "training/type"), // id as Object Key
            Map.entry(BambooHREntityNames.EMPLOYEE_TRAININGS,URI_PREFIX_WITH_VERSION + "training/record/employee/{employee_id}"), //id as object key
            Map.entry(BambooHREntityNames.EMPLOYEES,URI_PREFIX_WITH_VERSION + "employees"),
            Map.entry(BambooHREntityNames.GOALS,URI_PREFIX_WITH_VERSION + "performance/employees/{employeeId}/goals"),
            Map.entry(BambooHREntityNames.JOB_APPLICATIONS,URI_PREFIX_WITH_VERSION + "applicant_tracking/applications"),
            Map.entry(BambooHREntityNames.JOB_SUMMARY,URI_PREFIX_WITH_VERSION + "applicant_tracking/jobs"),
            Map.entry(BambooHREntityNames.MEMBER_BENEFITS,URI_PREFIX_WITH_VERSION + "benefits/member-benefits"),
            Map.entry(BambooHREntityNames.TIME_OFF_POLICIES,URI_PREFIX_WITH_VERSION + "meta/time_off/policies"),
            Map.entry(BambooHREntityNames.TIME_OFF_RECORDS,URI_PREFIX_WITH_VERSION + "time_off/whos_out"),
            Map.entry(BambooHREntityNames.TIME_OFF_REQUESTS,URI_PREFIX_WITH_VERSION + "time_off/requests"),
            Map.entry(BambooHREntityNames.TIME_SHEET_ENTRIES,URI_PREFIX_WITH_VERSION + "api/v1/time_tracking/timesheet_entries"),
            Map.entry(BambooHREntityNames.USERS,URI_PREFIX_WITH_VERSION + "meta/users") // Id as Object Key
    );

    public static final List<String> excludedEntities = List.of(BambooHREntityNames.TIME_SHEET_ENTRIES,
            BambooHREntityNames.GOALS, BambooHREntityNames.EMPLOYEE_BENEFITS);

    public static final Map<String, List<String>> CUSTOM_QUERY_PARAM_BY_ENTITY = Map.ofEntries(
            Map.entry(BambooHREntityNames.MEMBER_BENEFITS , List.of("calendarYear")),
            Map.entry(BambooHREntityNames.TIME_OFF_RECORDS , List.of("start", "end")),
            Map.entry(BambooHREntityNames.TIME_OFF_REQUESTS , List.of("start", "end"))
    );

    private BambooHRConstants() {}
}