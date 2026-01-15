package io.datalakehouse.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GustoConstants {

    public static final String CONNECTOR_NAME = "GUSTO";
    public static final long DEFAULT_HISTORICAL_DAYS = 90L;

    public static String getGustoBaseUrl(String environment) {
        if (environment == null || environment.isBlank()) {
            return "https://api.gusto-demo.com"; // default
        }

        return switch (environment.trim().toUpperCase()) {
            case "PROD", "PRODUCTION" -> "https://api.gusto.com";
            case "DEMO", "SANDBOX" -> "https://api.gusto-demo.com";
            default -> throw new IllegalArgumentException("Unsupported Gusto environment: " + environment);
        };
    }

    public static final String API_VERSION_HEADER = "X-Gusto-API-Version";
    public static final String API_VERSION = "2025-06-15";
    public static final String[] ID_HEADERS = {"UUID", "ID"};
    public static final String TOKEN_INFO_URI = "/v1/token_info";
    public static final String COMPANY_ENDPOINT_PREFIX = "v1/companies/";
    public static final String EMPLOYEE_ENDPOINT_PREFIX = "v1/employees/{employee_id}";
    public static final String CONTRACTOR_PAYMENTS_DATA_KEY = "payments";


    public static final class GustoEntityNames {
        public static final String COMPANY = "COMPANY";
        public static final String COMPANY_ADMINS = "COMPANY_ADMINS";
        public static final String COMPANY_CUSTOM_FIELDS = "COMPANY_CUSTOM_FIELDS";
        public static final String COMPANY_LOCATIONS = "COMPANY_LOCATIONS";
        public static final String LOCATIONS_MINIMUM_WAGES = "LOCATIONS_MINIMUM_WAGES";
        public static final String PAY_SCHEDULES = "PAY_SCHEDULES";
        public static final String PAY_SCHEDULES_ASSIGNMENTS = "PAY_SCHEDULES_ASSIGNMENTS";
        public static final String EMPLOYEE_PAY_SCHEDULE_ASSIGNMENTS = "EMPLOYEE_PAY_SCHEDULE_ASSIGNMENTS";
        public static final String DEPARTMENT_PAY_SCHEDULE_ASSIGNMENTS = "DEPARTMENT_PAY_SCHEDULE_ASSIGNMENTS";
        public static final String PAY_PERIODS = "PAY_PERIODS";
        public static final String EARNING_TYPES = "EARNING_TYPES";
        public static final String PAYROLLS = "PAYROLLS";
        public static final String COMPANY_BENEFITS = "COMPANY_BENEFITS";
        public static final String BENEFITS = "BENEFITS";
        public static final String DEPARTMENTS = "DEPARTMENTS";
        public static final String TIMESHEETS = "TIMESHEETS";
        public static final String EMPLOYEES = "EMPLOYEES";
        public static final String EMPLOYEE_HOME_ADDRESSES = "EMPLOYEE_HOME_ADDRESSES";
        public static final String EMPLOYEE_WORK_ADDRESSES = "EMPLOYEE_WORK_ADDRESSES";
        public static final String EMPLOYEE_CUSTOM_FIELDS = "EMPLOYEE_CUSTOM_FIELDS";
        public static final String EMPLOYEE_TIME_OFF_ACTIVITIES = "EMPLOYEE_TIME_OFF_ACTIVITIES";
        public static final String EMPLOYEE_TERMINATIONS = "EMPLOYEE_TERMINATIONS";
        public static final String EMPLOYEE_REHIRE = "EMPLOYEE_REHIRE";
        public static final String JOBS = "JOBS";
        public static final String JOBS_COMPENSATIONS = "JOBS_COMPENSATIONS";
        public static final String EMPLOYEE_BENEFITS = "EMPLOYEE_BENEFITS";
        public static final String CONTRIBUTION_EXCLUSIONS = "CONTRIBUTION_EXCLUSIONS";
        public static final String GARNISHMENTS = "GARNISHMENTS";
        public static final String CONTRACTORS = "CONTRACTORS";
        public static final String CONTRACTOR_PAYMENTS = "CONTRACTOR_PAYMENTS";
        public static final String RECURRING_REIMBURSEMENTS = "RECURRING_REIMBURSEMENTS";
        public static final String TOKEN_INFO = "TOKEN_INFO";
        public static final String DEPARTMENT_EMPLOYEES = "DEPARTMENT_EMPLOYEES";
        public static final String DEPARTMENT_CONTRACTORS = "DEPARTMENT_CONTRACTORS";
        private GustoEntityNames() {
        }
    }

    public static final class GustoHeaders {

        public static final String[] COMPANY = {"UUID", "EIN", "ENTITY_TYPE", "TIER", "CONTRACTOR_ONLY", "IS_SUSPENDED",
                "COMPANY_STATUS", "NAME", "TRADE_NAME", "SLUG", "IS_PARTNER_MANAGED", "PAY_SCHEDULE_TYPE", "JOIN_DATE",
                "FUNDING_TYPE", "COMPENSATIONS", "PRIMARY_SIGNATORY", "PRIMARY_PAYROLL_ADMIN", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_ADMINS = {"UUID", "FIRST_NAME", "LAST_NAME", "EMAIL", "PHONE", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_CUSTOM_FIELDS = {"UUID", "NAME", "DESCRIPTION",
                "COMPANY_CUSTOM_FIELDS_TYPE", "SELECTION_OPTIONS", "__ROW_MD5", "__DLH_IS_DELETED",
                "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_LOCATIONS = {"UUID", "CREATED_AT", "UPDATED_AT", "COMPANY_UUID", "VERSION",
                "STREET_1", "STREET_2", "CITY", "COMPANY_LOCATIONS_STATE", "ZIP", "COUNTRY", "ACTIVE", "PHONE_NUMBER",
                "FILING_ADDRESS", "MAILING_ADDRESS", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS",
                "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] LOCATIONS_MINIMUM_WAGES = {"UUID", "AUTHORITY", "WAGE", "WAGE_TYPE",
                "EFFECTIVE_DATE", "NOTES", "LOCATION_UUID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS",
                "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAY_SCHEDULES = {"UUID", "VERSION", "FREQUENCY", "ANCHOR_PAY_DATE",
                "ANCHOR_END_OF_PAY_PERIOD", "DAY_1", "DAY_2", "NAME", "CUSTOM_NAME", "AUTO_PILOT", "ACTIVE", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAY_SCHEDULES_ASSIGNMENTS = {"PAY_SCHEDULES_ASSIGNMENTS_TYPE", "EMPLOYEES",
                "DEPARTMENTS", "HOURLY_PAY_SCHEDULE_UUID", "SALARIED_PAY_SCHEDULE_UUID", "DEFAULT_PAY_SCHEDULE_UUID",
                "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS",
                "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_PAY_SCHEDULE_ASSIGNMENTS = {"PAY_SCHEDULES_ASSIGNMENTS_TYPE",
                "EMPLOYEE_UUID", "PAY_SCHEDULE_UUID", "DEFAULT_PAY_SCHEDULE_UUID", "HOURLY_PAY_SCHEDULE_UUID",
                "SALARIED_PAY_SCHEDULE_UUID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS",
                "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] DEPARTMENT_PAY_SCHEDULE_ASSIGNMENTS = {"PAY_SCHEDULES_ASSIGNMENTS_TYPE",
                "DEPARTMENT_UUID", "PAY_SCHEDULE_UUID", "DEFAULT_PAY_SCHEDULE_UUID", "HOURLY_PAY_SCHEDULE_UUID",
                "SALARIED_PAY_SCHEDULE_UUID", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS",
                "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAY_PERIODS = {"START_DATE", "END_DATE", "PAY_SCHEDULE_UUID", "PAYROLL_UUID",
                "PAYROLL_CHECK_DATE", "PAYROLL_PROCESSED", "PAYROLL_DEADLINE", "PAYROLL_TYPE", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EARNING_TYPES = {"EARNING_TYPE", "UUID", "NAME", "__ROW_MD5", "__DLH_IS_DELETED",
                "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] PAYROLLS = {"UUID", "PAYROLL_UUID", "COMPANY_UUID", "OFF_CYCLE", "AUTO_PILOT",
                "PROCESSED", "PROCESSED_DATE", "CALCULATED_AT", "PAY_PERIOD_START_DATE", "PAY_PERIOD_END_DATE",
                "PAY_PERIOD_PAY_SCHEDULE_UUID", "CHECK_DATE", "PAYROLLS_EXTERNAL", "PAYROLL_DEADLINE", "CREATED_AT",
                "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS",
                "__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_BENEFITS = {"UUID", "VERSION", "COMPANY_UUID", "BENEFIT_TYPE", "ACTIVE",
                "DESCRIPTION", "SOURCE", "PARTNER_NAME", "DELETABLE", "SUPPORTS_PERCENTAGE_AMOUNTS",
                "RESPONSIBLE_FOR_EMPLOYER_TAXES", "RESPONSIBLE_FOR_EMPLOYEE_W2", "__ROW_MD5", "__DLH_IS_DELETED",
                "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] BENEFITS = {"BENEFIT_TYPE", "NAME", "DESCRIPTION", "PRETAX", "POSTTAX", "IMPUTED",
                "HEALTHCARE", "RETIREMENT", "YEARLY_LIMIT", "CATEGORY", "__ROW_MD5", "__DLH_IS_DELETED",
                "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] DEPARTMENTS = {"UUID", "COMPANY_UUID", "TITLE", "VERSION",
                "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS",
                "__DLH_IS_ACTIVE"};
        public static final String[] TIMESHEETS = {"UUID", "COMPANY_UUID", "STATUS", "TIME_ZONE", "ENTITY_TYPE",
                "VERSION", "JOB_UUID", "ENTITY_UUID", "SHIFT_STARTED_AT", "SHIFT_ENDED_AT", "CREATED_AT", "UPDATED_AT",
                "METADATA", "ENTRIES", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS",
                "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEES = {"UUID", "FIRST_NAME", "MIDDLE_INITIAL", "LAST_NAME", "EMAIL",
                "COMPANY_UUID", "MANAGER_UUID", "VERSION", "CURRENT_EMPLOYMENT_STATUS", "ONBOARDING_STATUS",
                "PREFERRED_FIRST_NAME", "DEPARTMENT_UUID", "EMPLOYEE_CODE", "PAYMENT_METHOD", "DEPARTMENT",
                "TERMINATED", "TWO_PERCENT_SHAREHOLDER", "ONBOARDED", "HISTORICAL", "HAS_SSN",
                "ONBOARDING_DOCUMENTS_CONFIG", "ELIGIBLE_PAID_TIME_OFF",
                "DATE_OF_BIRTH", "SSN", "PHONE", "WORK_EMAIL", "__ROW_MD5", "__DLH_IS_DELETED",
                "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_HOME_ADDRESSES = {"UUID", "VERSION", "EMPLOYEE_UUID", "STREET_1",
                "STREET_2", "CITY", "EMPLOYEE_HOME_ADDRESS_STATE", "ZIP", "COUNTRY", "ACTIVE", "EFFECTIVE_DATE",
                "COURTESY_WITHHOLDING", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS",
                "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_WORK_ADDRESSES = {"UUID", "EMPLOYEE_UUID", "LOCATION_UUID",
                "EFFECTIVE_DATE", "ACTIVE", "VERSION", "STREET_1", "STREET_2", "CITY", "EMPLOYEE_WORK_ADDRESS_STATE",
                "ZIP", "COUNTRY", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS",
                "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_CUSTOM_FIELDS = {"ID", "COMPANY_CUSTOM_FIELD_ID", "NAME", "DESCRIPTION",
                "EMPLOYEE_CUSTOM_FIELDS_TYPE", "EMPLOYEE_CUSTOM_FIELD_VALUE", "SELECTION_OPTIONS", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_TIME_OFF_ACTIVITIES = {"POLICY_UUID", "TIME_OFF_TYPE", "POLICY_NAME",
                "EVENT_TYPE", "EVENT_DESCRIPTION", "EFFECTIVE_TIME", "BALANCE", "BALANCE_CHANGE", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_TERMINATIONS = {"UUID", "EMPLOYEE_UUID", "VERSION", "ACTIVE",
                "CANCELABLE", "EFFECTIVE_DATE", "RUN_TERMINATION_PAYROLL", "__ROW_MD5", "__DLH_IS_DELETED",
                "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_REHIRE = {"VERSION", "EMPLOYEE_UUID", "ACTIVE", "EFFECTIVE_DATE",
                "FILE_NEW_HIRE_REPORT", "WORK_LOCATION_UUID", "TWO_PERCENT_SHAREHOLDER", "EMPLOYMENT_STATUS",
                "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS",
                "__DLH_IS_ACTIVE"};
        public static final String[] JOBS = {"UUID", "VERSION", "EMPLOYEE_UUID", "CURRENT_COMPENSATION_UUID",
                "PAYMENT_UNIT", "JOBS_PRIMARY", "TITLE", "STATE_WC_COVERED", "STATE_WC_CLASS_CODE",
                "TWO_PERCENT_SHAREHOLDER", "RATE", "HIRE_DATE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS",
                "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] JOBS_COMPENSATIONS = {"UUID", "EMPLOYEE_UUID", "VERSION", "PAYMENT_UNIT", "FLSA_STATUS",
                "JOB_UUID", "EFFECTIVE_DATE", "RATE", "ADJUST_FOR_MINIMUM_WAGE", "MINIMUM_WAGES", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_BENEFITS = {"UUID", "VERSION", "EMPLOYEE_UUID", "COMPANY_BENEFIT_UUID",
                "ACTIVE", "EMPLOYEE_DEDUCTION", "EMPLOYEE_DEDUCTION_ANNUAL_MAXIMUM", "RETIREMENT_LOAN_IDENTIFIER",
                "COMPANY_CONTRIBUTION_ANNUAL_MAXIMUM", "LIMIT_OPTION", "DEDUCT_AS_PERCENTAGE", "CATCH_UP",
                "COVERAGE_AMOUNT", "DEDUCTION_REDUCES_TAXABLE_INCOME", "COVERAGE_SALARY_MULTIPLIER",
                "EFFECTIVE_DATE", "EXPIRATION_DATE",
                "CONTRIBUTION_TYPE", "CONTRIBUTION_VALUE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS",
                "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CONTRIBUTION_EXCLUSIONS = {"CONTRIBUTION_UUID", "CONTRIBUTION_TYPE", "EXCLUDED",
                "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS",
                "__DLH_IS_ACTIVE"};
        public static final String[] GARNISHMENTS = {"UUID", "VERSION", "EMPLOYEE_UUID", "ACTIVE", "AMOUNT",
                "DESCRIPTION", "COURT_ORDERED", "TIMES", "RECURRING", "ANNUAL_MAXIMUM", "TOTAL_AMOUNT",
                "PAY_PERIOD_MAXIMUM", "DEDUCT_AS_PERCENTAGE", "GARNISHMENT_TYPE", "CHILD_SUPPORT_STATE",
                "CHILD_SUPPORT_PAYMENT_PERIOD", "CHILD_SUPPORT_CASE_NUMBER", "CHILD_SUPPORT_ORDER_NUMBER",
                "CHILD_SUPPORT_REMITTANCE_NUMBER", "CHILD_SUPPORT_FIPS_CODE", "__ROW_MD5", "__DLH_IS_DELETED",
                "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CONTRACTORS = {"UUID", "COMPANY_UUID", "WAGE_TYPE",
                "IS_ACTIVE", "VERSION", "CONTRACTORS_TYPE", "FIRST_NAME", "LAST_NAME", "MIDDLE_INITIAL",
                "BUSINESS_NAME", "EIN", "HAS_EIN", "HAS_SSN", "EMAIL", "FILE_NEW_HIRE_REPORT", "WORK_STATE",
                "DEPARTMENT_UUID", "DISMISSAL_DATE", "DEPARTMENT_TITLE", "START_DATE", "ONBOARDED",
                "ONBOARDING_STATUS", "PAYMENT_METHOD", "ADDRESS_STREET_1", "ADDRESS_STREET_2", "ADDRESS_CITY",
                "ADDRESS_STATE", "ADDRESS_ZIP", "ADDRESS_COUNTRY", "HOURLY_RATE", "__ROW_MD5", "__DLH_IS_DELETED",
                "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] CONTRACTOR_PAYMENTS = {"UUID", "CONTRACTOR_UUID", "BONUS",
                "CONTRACTOR_PAYMENTS_DATE", "HOURS", "PAYMENT_METHOD", "REIMBURSEMENT", "STATUS", "HOURLY_RATE",
                "MAY_CANCEL","WAGE","WAGE_TYPE","WAGE_TOTAL","__ROW_MD5","__DLH_IS_DELETED",
                "__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] RECURRING_REIMBURSEMENTS = {"UUID", "EMPLOYEE_UUID", "VERSION", "DESCRIPTION",
                "CREATED_AT", "UPDATED_AT", "AMOUNT", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS",
                "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] DEPARTMENT_EMPLOYEES = {"DEPARTMENT_UUID", "EMPLOYEE_UUID", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};
        public static final String[] DEPARTMENT_CONTRACTORS = {"DEPARTMENT_UUID", "CONTRACTOR_UUID", "__ROW_MD5",
                "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS", "__DLH_FINISH_TS", "__DLH_IS_ACTIVE"};

        private GustoHeaders() {
        }
    }


    public static List<String> excludedEntities = List.of();

    public static String getUrl(String entityName) {
        return "v1/" + entityName.toLowerCase();
    }

    // Define entity dependencies
    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.ofEntries(
            Map.entry(GustoEntityNames.EMPLOYEES,
                    List.of(GustoEntityNames.EMPLOYEE_HOME_ADDRESSES, GustoEntityNames.EMPLOYEE_WORK_ADDRESSES,
                            GustoEntityNames.EMPLOYEE_TERMINATIONS, GustoEntityNames.EMPLOYEE_REHIRE,
                            GustoEntityNames.JOBS, GustoEntityNames.GARNISHMENTS, GustoEntityNames.EMPLOYEE_BENEFITS,
                            GustoEntityNames.RECURRING_REIMBURSEMENTS, GustoEntityNames.EMPLOYEE_CUSTOM_FIELDS)),
            Map.entry(GustoEntityNames.COMPANY_LOCATIONS, List.of(GustoEntityNames.LOCATIONS_MINIMUM_WAGES)),
            Map.entry(GustoEntityNames.COMPANY_BENEFITS, List.of(GustoEntityNames.CONTRIBUTION_EXCLUSIONS)),
            Map.entry(GustoEntityNames.JOBS, List.of(GustoEntityNames.JOBS_COMPENSATIONS)));

    // Define headers by entity
    public static final Map<String, String[]> HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(GustoEntityNames.COMPANY, GustoHeaders.COMPANY),
            Map.entry(GustoEntityNames.COMPANY_ADMINS, GustoHeaders.COMPANY_ADMINS),
            Map.entry(GustoEntityNames.COMPANY_LOCATIONS, GustoHeaders.COMPANY_LOCATIONS),
            Map.entry(GustoEntityNames.PAY_SCHEDULES, GustoHeaders.PAY_SCHEDULES),
            Map.entry(GustoEntityNames.PAYROLLS, GustoHeaders.PAYROLLS),
            Map.entry(GustoEntityNames.EMPLOYEES, GustoHeaders.EMPLOYEES),
            Map.entry(GustoEntityNames.BENEFITS, GustoHeaders.BENEFITS),
            Map.entry(GustoEntityNames.COMPANY_BENEFITS, GustoHeaders.COMPANY_BENEFITS),
            Map.entry(GustoEntityNames.DEPARTMENTS, GustoHeaders.DEPARTMENTS),
            Map.entry(GustoEntityNames.TIMESHEETS, GustoHeaders.TIMESHEETS),
            Map.entry(GustoEntityNames.CONTRACTORS, GustoHeaders.CONTRACTORS),
            Map.entry(GustoEntityNames.EMPLOYEE_HOME_ADDRESSES, GustoHeaders.EMPLOYEE_HOME_ADDRESSES),
            Map.entry(GustoEntityNames.EMPLOYEE_WORK_ADDRESSES, GustoHeaders.EMPLOYEE_WORK_ADDRESSES),
            Map.entry(GustoEntityNames.JOBS, GustoHeaders.JOBS),
            Map.entry(GustoEntityNames.GARNISHMENTS, GustoHeaders.GARNISHMENTS),
            Map.entry(GustoEntityNames.EMPLOYEE_BENEFITS, GustoHeaders.EMPLOYEE_BENEFITS),
            Map.entry(GustoEntityNames.JOBS_COMPENSATIONS, GustoHeaders.JOBS_COMPENSATIONS),
            Map.entry(GustoEntityNames.PAY_PERIODS, GustoHeaders.PAY_PERIODS),
            Map.entry(GustoEntityNames.PAY_SCHEDULES_ASSIGNMENTS, GustoHeaders.PAY_SCHEDULES_ASSIGNMENTS),
            Map.entry(GustoEntityNames.EMPLOYEE_TERMINATIONS, GustoHeaders.EMPLOYEE_TERMINATIONS),
            Map.entry(GustoEntityNames.RECURRING_REIMBURSEMENTS, GustoHeaders.RECURRING_REIMBURSEMENTS),
            Map.entry(GustoEntityNames.LOCATIONS_MINIMUM_WAGES, GustoHeaders.LOCATIONS_MINIMUM_WAGES),
            Map.entry(GustoEntityNames.EMPLOYEE_CUSTOM_FIELDS, GustoHeaders.EMPLOYEE_CUSTOM_FIELDS),
            Map.entry(GustoEntityNames.EARNING_TYPES, GustoHeaders.EARNING_TYPES),
            Map.entry(GustoEntityNames.COMPANY_CUSTOM_FIELDS, GustoHeaders.COMPANY_CUSTOM_FIELDS),
            Map.entry(GustoEntityNames.CONTRIBUTION_EXCLUSIONS, GustoHeaders.CONTRIBUTION_EXCLUSIONS),
            Map.entry(GustoEntityNames.CONTRACTOR_PAYMENTS, GustoHeaders.CONTRACTOR_PAYMENTS),
            Map.entry(GustoEntityNames.EMPLOYEE_REHIRE, GustoHeaders.EMPLOYEE_REHIRE)
    );

    public static final Map<String, String[]> CHILD_TABLE_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(GustoEntityNames.EMPLOYEE_PAY_SCHEDULE_ASSIGNMENTS, GustoHeaders.EMPLOYEE_PAY_SCHEDULE_ASSIGNMENTS),
            Map.entry(GustoEntityNames.DEPARTMENT_PAY_SCHEDULE_ASSIGNMENTS, GustoHeaders.DEPARTMENT_PAY_SCHEDULE_ASSIGNMENTS),
            Map.entry(GustoEntityNames.DEPARTMENT_CONTRACTORS, GustoHeaders.DEPARTMENT_CONTRACTORS),
            Map.entry(GustoEntityNames.DEPARTMENT_EMPLOYEES, GustoHeaders.DEPARTMENT_EMPLOYEES)
    );

    // Define delta headers by entity
    public static final Map<String, String[]> DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(GustoEntityNames.PAY_PERIODS, GustoHeaders.PAY_PERIODS));

    public static final Map<String, String[]> NON_DELTA_HEADERS_BY_ENTITY = Map.ofEntries(
            Map.entry(GustoEntityNames.COMPANY, GustoHeaders.COMPANY),
            Map.entry(GustoEntityNames.COMPANY_ADMINS, GustoHeaders.COMPANY_ADMINS),
            Map.entry(GustoEntityNames.COMPANY_LOCATIONS, GustoHeaders.COMPANY_LOCATIONS),
            Map.entry(GustoEntityNames.PAY_SCHEDULES, GustoHeaders.PAY_SCHEDULES),
            Map.entry(GustoEntityNames.PAYROLLS, GustoHeaders.PAYROLLS),
            Map.entry(GustoEntityNames.EMPLOYEES, GustoHeaders.EMPLOYEES),
            Map.entry(GustoEntityNames.BENEFITS, GustoHeaders.BENEFITS),
            Map.entry(GustoEntityNames.COMPANY_BENEFITS, GustoHeaders.COMPANY_BENEFITS),
            Map.entry(GustoEntityNames.DEPARTMENTS, GustoHeaders.DEPARTMENTS),
            Map.entry(GustoEntityNames.TIMESHEETS, GustoHeaders.TIMESHEETS),
            Map.entry(GustoEntityNames.CONTRACTORS, GustoHeaders.CONTRACTORS),
            Map.entry(GustoEntityNames.EMPLOYEE_HOME_ADDRESSES, GustoHeaders.EMPLOYEE_HOME_ADDRESSES),
            Map.entry(GustoEntityNames.EMPLOYEE_WORK_ADDRESSES, GustoHeaders.EMPLOYEE_WORK_ADDRESSES),
            Map.entry(GustoEntityNames.JOBS, GustoHeaders.JOBS),
            Map.entry(GustoEntityNames.GARNISHMENTS, GustoHeaders.GARNISHMENTS),
            Map.entry(GustoEntityNames.EMPLOYEE_BENEFITS, GustoHeaders.EMPLOYEE_BENEFITS),
            Map.entry(GustoEntityNames.JOBS_COMPENSATIONS, GustoHeaders.JOBS_COMPENSATIONS),
            Map.entry(GustoEntityNames.PAY_SCHEDULES_ASSIGNMENTS, GustoHeaders.PAY_SCHEDULES_ASSIGNMENTS),
            Map.entry(GustoEntityNames.EMPLOYEE_TERMINATIONS, GustoHeaders.EMPLOYEE_TERMINATIONS),
            Map.entry(GustoEntityNames.RECURRING_REIMBURSEMENTS, GustoHeaders.RECURRING_REIMBURSEMENTS),
            Map.entry(GustoEntityNames.LOCATIONS_MINIMUM_WAGES, GustoHeaders.LOCATIONS_MINIMUM_WAGES),
            Map.entry(GustoEntityNames.EMPLOYEE_CUSTOM_FIELDS, GustoHeaders.EMPLOYEE_CUSTOM_FIELDS),
            Map.entry(GustoEntityNames.EARNING_TYPES, GustoHeaders.EARNING_TYPES),
            Map.entry(GustoEntityNames.COMPANY_CUSTOM_FIELDS, GustoHeaders.COMPANY_CUSTOM_FIELDS),
            Map.entry(GustoEntityNames.CONTRIBUTION_EXCLUSIONS, GustoHeaders.CONTRIBUTION_EXCLUSIONS),
            Map.entry(GustoEntityNames.CONTRACTOR_PAYMENTS, GustoHeaders.CONTRACTOR_PAYMENTS));


    public static Map<String, String> ENTITY_API_PATH_MAP = new HashMap<>();

    public static Map<String, String> getEntityApiPathMap(String companyId) {
        // Generate API path map

        ENTITY_API_PATH_MAP.put(GustoEntityNames.COMPANY, COMPANY_ENDPOINT_PREFIX + companyId);
        ENTITY_API_PATH_MAP.put(GustoEntityNames.COMPANY_ADMINS, COMPANY_ENDPOINT_PREFIX + companyId + "/admins");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.COMPANY_LOCATIONS, COMPANY_ENDPOINT_PREFIX + companyId + "/locations");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.PAY_SCHEDULES, COMPANY_ENDPOINT_PREFIX + companyId + "/pay_schedules");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.COMPANY_BENEFITS,
                COMPANY_ENDPOINT_PREFIX + companyId + "/company_benefits");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.DEPARTMENTS, COMPANY_ENDPOINT_PREFIX + companyId + "/departments");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EMPLOYEES, COMPANY_ENDPOINT_PREFIX + companyId + "/employees");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.PAYROLLS, COMPANY_ENDPOINT_PREFIX + companyId + "/payrolls");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.BENEFITS, "v1/benefits");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.TIMESHEETS,
                COMPANY_ENDPOINT_PREFIX + companyId + "/time_tracking/time_sheets");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.CONTRACTORS, COMPANY_ENDPOINT_PREFIX + companyId + "/contractors");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EMPLOYEE_HOME_ADDRESSES, EMPLOYEE_ENDPOINT_PREFIX + "/home_addresses");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EMPLOYEE_WORK_ADDRESSES, EMPLOYEE_ENDPOINT_PREFIX + "/work_addresses");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EMPLOYEE_TERMINATIONS, EMPLOYEE_ENDPOINT_PREFIX + "/terminations");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EMPLOYEE_REHIRE, EMPLOYEE_ENDPOINT_PREFIX + "/rehire");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.JOBS, EMPLOYEE_ENDPOINT_PREFIX + "/jobs");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.GARNISHMENTS, EMPLOYEE_ENDPOINT_PREFIX + "/garnishments");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EMPLOYEE_BENEFITS, EMPLOYEE_ENDPOINT_PREFIX + "/employee_benefits");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.RECURRING_REIMBURSEMENTS,
                EMPLOYEE_ENDPOINT_PREFIX + "/recurring_reimbursements");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.JOBS_COMPENSATIONS, "v1/jobs/{job_id}/compensations");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.PAY_PERIODS, COMPANY_ENDPOINT_PREFIX + companyId + "/pay_periods");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.PAY_SCHEDULES_ASSIGNMENTS,
                COMPANY_ENDPOINT_PREFIX + companyId + "/pay_schedules/assignments");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EMPLOYEE_TIME_OFF_ACTIVITIES,
                EMPLOYEE_ENDPOINT_PREFIX + "/time_off_activities");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.CONTRACTOR_PAYMENTS,
                COMPANY_ENDPOINT_PREFIX + companyId + "/contractor_payments");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.LOCATIONS_MINIMUM_WAGES,
                "v1/locations/{company_location_id}/minimum_wages");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EARNING_TYPES, COMPANY_ENDPOINT_PREFIX + companyId + "/earning_types");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.EMPLOYEE_CUSTOM_FIELDS,"v1/employees/{employee_id}/custom_fields");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.CONTRIBUTION_EXCLUSIONS,
                "v1/company_benefits/{company_benefit_id}/contribution_exclusions");
        ENTITY_API_PATH_MAP.put(GustoEntityNames.COMPANY_CUSTOM_FIELDS,COMPANY_ENDPOINT_PREFIX + companyId + "/custom_fields");

        HEADERS_BY_ENTITY.keySet().forEach(entity -> ENTITY_API_PATH_MAP.putIfAbsent(entity, getUrl(entity)));

        return ENTITY_API_PATH_MAP;
    }

    public static final Map<String, List<String>> MAPPING_TABLES_MAP = Map.ofEntries(
            Map.entry(GustoEntityNames.DEPARTMENTS, List.of(
                    GustoEntityNames.DEPARTMENT_CONTRACTORS, GustoEntityNames.DEPARTMENT_EMPLOYEES)),
            Map.entry(GustoEntityNames.PAY_SCHEDULES_ASSIGNMENTS, List.of(
                    GustoEntityNames.EMPLOYEE_PAY_SCHEDULE_ASSIGNMENTS,
                    GustoEntityNames.DEPARTMENT_PAY_SCHEDULE_ASSIGNMENTS))
    );

    public static final Map<String, List<String>> CUSTOM_QUERY_PARAM_BY_ENTITY = Map.ofEntries(
            Map.entry(GustoEntityNames.CONTRACTOR_PAYMENTS, List.of("start_date", "end_date"))
    );

    private GustoConstants() {}
}

