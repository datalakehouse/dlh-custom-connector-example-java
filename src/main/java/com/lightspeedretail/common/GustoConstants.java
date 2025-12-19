package com.lightspeedretail.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GustoConstants {

    public static final String CONNECTOR_NAME = "GUSTO_CONNECTOR";

    public static final class GustoAPI {
        // TODO : Need to switch to Prod API URL when actual integration is done.
        // Demo API URL: https://api.gusto-demo.com
        // Prod API URL: https://api.gusto.com
        public static final String API_URL = "https://api.gusto-demo.com";

        private GustoAPI() {}
    }

    public static final class GustoEntityNames {
        public static final String COMPANY = "COMPANY";
        public static final String COMPANY_ADMINS = "COMPANY_ADMINS";
        public static final String CUSTOM_FIELDS = "CUSTOM_FIELDS";
        public static final String COMPANY_LOCATIONS = "COMPANY_LOCATIONS";
        public static final String LOCATIONS_MINIMUM_WAGES= "LOCATIONS_MINIMUM_WAGES";
        public static final String PAY_SCHEDULES = "PAY_SCHEDULES";
        public static final String PAY_SCHEDULES_ASSIGNMENTS = "PAY_SCHEDULES_ASSIGNMENTS";
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

        private GustoEntityNames() {}
    }

    public static final class GustoHeaders {

        public static final String[] COMPANY = {"UUID","EIN","ENTITY_TYPE","TIER","CONTRACTOR_ONLY","IS_SUSPENDED","COMPANY_STATUS","NAME","TRADE_NAME","SLUG","IS_PARTNER_MANAGED","PAY_SCHEDULE_TYPE","JOIN_DATE","FUNDING_TYPE","COMPENSATIONS","PRIMARY_SIGNATORY","PRIMARY_PAYROLL_ADMIN","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_ADMINS = {"UUID","FIRST_NAME","LAST_NAME","EMAIL","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CUSTOM_FIELDS = {"UUID","NAME","DESCRIPTION","TYPE","SELECTION_OPTIONS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_LOCATIONS = {"UUID","CREATED_AT","UPDATED_AT","COMPANY_UUID","VERSION","STREET_1","STREET_2","CITY","STATE","ZIP","COUNTRY","ACTIVE","PHONE_NUMBER","FILING_ADDRESS","MAILING_ADDRESS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] LOCATIONS_MINIMUM_WAGES = {"UUID","AUTHORITY","WAGE","WAGE_TYPE","EFFECTIVE_DATE","NOTES","LOCATION_UUID","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PAY_SCHEDULES = {"UUID","VERSION", "FREQUENCY", "ANCHOR_PAY_DATE", "ANCHOR_END_OF_PAY_PERIOD", "DAY_1", "DAY_2", "NAME", "CUSTOM_NAME", "AUTOPILOT", "ACTIVE", "__ROW_MD5", "__DLH_IS_DELETED", "__DLH_SYNC_TS", "__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PAY_SCHEDULES_ASSIGNMENTS = {"TYPE","EMPLOYEES","DEPARTMENTS","HOURLY_PAY_SCHEDULE_UUID","SALARIED_PAY_SCHEDULE_UUID","DEFAULT_PAY_SCHEDULE_UUID","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PAY_PERIODS = {"START_DATE","END_DATE","PAY_SCHEDULE_UUID","PAYROLL_UUID","PAYROLL_CHECK_DATE","PAYROLL_PROCESSED","PAYROLL_DEADLINE","PAYROLL_TYPE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EARNING_TYPES = {"UUID","NAME","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] PAYROLLS = {"UUID","EMPLOYEE_COMPENSATIONS","SUBMISSION_BLOCKERS","CREDIT_BLOCKERS","PAYROLL_UUID","COMPANY_UUID","OFF_CYCLE","AUTO_PILOT","PROCESSED","PROCESSED_DATE","CALCULATED_AT","PAY_PERIOD_START_DATE","PAY_PERIOD_END_DATE","PAY_PERIOD_PAY_SCHEDULE_UUID","PAY_PERIOD_ID","PAYROLL_STATUS_META_CANCELLABLE","PAYROLL_STATUS_META_EXPECTED_CHECK_DATE","PAYROLL_STATUS_META_INITIAL_CHECK_DATE","PAYROLL_STATUS_META_EXPECTED_DEBIT_TIME","PAYROLL_STATUS_META_PAYROLL_LATE","PAYROLL_STATUS_META_INITIAL_DEBIT_CUTOFF_TIME","CHECK_DATE","EXTERNAL","PAYROLL_DEADLINE","PROCESSING_REQUEST_ID","COMPANY_TAXES","CREATED_AT","PARTNER_OWNED_DISBURSEMENT","TOTAL_EMPLOYEE_BONUSES","TOTAL_EMPLOYEE_COMMISSIONS","TOTAL_EMPLOYEE_CASH_TIPS","TOTAL_EMPLOYEE_PAYCHECK_TIPS","TOTAL_ADDITIONAL_EARNINGS","TOTAL_OWNERS_DRAW","TOTAL_BENEFITS","TOTAL_CHECK_AMOUNT","TOTAL_CHILD_SUPPORT_DEBIT","TOTAL_COMPANY_DEBIT","TOTAL_DEFERRED_PAYROLL_TAXES","TOTAL_EMPLOYEE_BENEFITS_DEDUCTIONS","TOTAL_EMPLOYEE_TAXES","TOTAL_EMPLOYER_TAXES","TOTAL_GROSS_PAY","TOTAL_IMPUTED_PAY","TOTAL_NET_PAY","TOTAL_NET_PAY_DEBIT","TOTAL_OTHER_DEDUCTIONS","TOTAL_REIMBURSEMENT_DEBIT","TOTAL_REIMBURSEMENTS","TOTAL_TAX_DEBIT","PROCESSING_REQUEST_STATUS","PROCESSING_REQUEST_ERRORS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] COMPANY_BENEFITS = {"UUID","VERSION","COMPANY_UUID","BENEFIT_TYPE","ACTIVE","DESCRIPTION","SOURCE","PARTNER_NAME","DELETABLE","SUPPORTS_PERCENTAGE_AMOUNTS","RESPONSIBLE_FOR_EMPLOYER_TAXES","RESPONSIBLE_FOR_EMPLOYEE_W2","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] BENEFITS = {"BENEFIT_TYPE","NAME","DESCRIPTION","PRETAX","POSTTAX","IMPUTED","HEALTHCARE","RETIREMENT","YEARLY_LIMIT","CATEGORY","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] DEPARTMENTS = {"UUID","COMPANY_UUID","TITLE","VERSION","EMPLOYEES","CONTRACTORS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TIMESHEETS = {"UUID","COMPANY_UUID","STATUS","TIME_ZONE","ENTITY_TYPE","VERSION","JOB_UUID","ENTITY_UUID","SHIFT_STARTED_AT","SHIFT_ENDED_AT","CREATED_AT","UPDATED_AT","METADATA","ENTRIES","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEES = {"UUID","FIRST_NAME","MIDDLE_INITIAL","LAST_NAME","EMAIL","COMPANY_UUID","MANAGER_UUID","VERSION","CURRENT_EMPLOYMENT_STATUS","ONBOARDING_STATUS","PREFERRED_FIRST_NAME","DEPARTMENT_UUID","EMPLOYEE_CODE","PAYMENT_METHOD","DEPARTMENT","TERMINATED","TWO_PERCENT_SHAREHOLDER","ONBOARDED","HISTORICAL","HAS_SSN","ONBOARDING_DOCUMENTS_CONFIG","CURRENT_HOME_ADDRESS","ELIGIBLE_PAID_TIME_OFF","TERMINATIONS","GARNISHMENTS","DATE_OF_BIRTH","SSN","PHONE","WORK_EMAIL","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_HOME_ADDRESSES = {"UUID","VERSION","EMPLOYEE_UUID","STREET_1","STREET_2","CITY","STATE","ZIP","COUNTRY","ACTIVE","EFFECTIVE_DATE","COURTESY_WITHHOLDING","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_WORK_ADDRESSES = {"UUID","EMPLOYEE_UUID","LOCATION_UUID","EFFECTIVE_DATE","ACTIVE","VERSION","STREET_1","STREET_2","CITY","STATE","ZIP","COUNTRY","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_CUSTOM_FIELDS = {"ID","COMPANY_CUSTOM_FIELD_ID","NAME","DESCRIPTION","TYPE","VALUE","SELECTION_OPTIONS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_TIME_OFF_ACTIVITIES = {"POLICY_UUID","TIME_OFF_TYPE","POLICY_NAME","EVENT_TYPE","EVENT_DESCRIPTION","EFFECTIVE_TIME","BALANCE","BALANCE_CHANGE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_TERMINATIONS = {"UUID","EMPLOYEE_UUID","VERSION","ACTIVE","CANCELABLE","EFFECTIVE_DATE","RUN_TERMINATION_PAYROLL","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_REHIRE = {"VERSION","EMPLOYEE_UUID","ACTIVE","EFFECTIVE_DATE","FILE_NEW_HIRE_REPORT","WORK_LOCATION_UUID","TWO_PERCENT_SHAREHOLDER","EMPLOYMENT_STATUS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] JOBS = {"UUID","VERSION","EMPLOYEE_UUID","CURRENT_COMPENSATION_UUID","PAYMENT_UNIT","PRIMARY","TITLE","STATE_WC_COVERED","STATE_WC_CLASS_CODE","RATE","HIRE_DATE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] JOBS_COMPENSATIONS = {"UUID","JOBS_ID","VERSION","PAYMENT_UNIT","FLSA_STATUS","JOB_UUID","EFFECTIVE_DATE","RATE","ADJUST_FOR_MINIMUM_WAGE","MINIMUM_WAGES","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] EMPLOYEE_BENEFITS = {"UUID","VERSION","EMPLOYEE_UUID","COMPANY_BENEFIT_UUID","ACTIVE","EMPLOYEE_DEDUCTION","EMPLOYEE_DEDUCTION_ANNUAL_MAXIMUM","COMPANY_CONTRIBUTION_ANNUAL_MAXIMUM","LIMIT_OPTION","DEDUCT_AS_PERCENTAGE","CATCH_UP","COVERAGE_AMOUNT","DEDUCTION_REDUCES_TAXABLE_INCOME","COVERAGE_SALARY_MULTIPLIER","CONTRIBUTION_TYPE","CONTRIBUTION_VALUE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CONTRIBUTION_EXCLUSIONS = {"CONTRIBUTION_UUID","CONTRIBUTION_TYPE","EXCLUDED","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] GARNISHMENTS = {"UUID","VERSION","EMPLOYEE_UUID","ACTIVE","AMOUNT","DESCRIPTION","COURT_ORDERED","TIMES","RECURRING","ANNUAL_MAXIMUM","TOTAL_AMOUNT","PAY_PERIOD_MAXIMUM","DEDUCT_AS_PERCENTAGE","GARNISHMENT_TYPE","CHILD_SUPPORT_STATE","CHILD_SUPPORT_PAYMENT_PERIOD","CHILD_SUPPORT_CASE_NUMBER","CHILD_SUPPORT_ORDER_NUMBER","CHILD_SUPPORT_REMITTANCE_NUMBER","CHILD_SUPPORT_FIPS_CODE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CONTRACTORS = {"ID","UUID","COMPANY_UUID","COMPANY_ID","WAGE_TYPE","IS_ACTIVE","VERSION","TYPE","FIRST_NAME","LAST_NAME","MIDDLE_INITIAL","BUSINESS_NAME","EIN","HAS_EIN","HAS_SSN","EMAIL","FILE_NEW_HIRE_REPORT","WORK_STATE","ONBOARDED","ONBOARDING_STATUS","CONTRACTOR_ADDRESS_STREET_1","CONTRACTOR_ADDRESS_STREET_2","CONTRACTOR_ADDRESS_CITY","CONTRACTOR_ADDRESS_STATE","CONTRACTOR_ADDRESS_ZIP","CONTRACTOR_ADDRESS_COUNTRY","HOURLY_RATE","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] CONTRACTOR_PAYMENTS = {"CONTRACTOR_ID","REIMBURSEMENT_TOTAL","WAGE_TOTAL","PAYMENTS","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] RECURRING_REIMBURSEMENTS = {"UUID","EMPLOYEE_UUID","VERSION","DESCRIPTION","CREATED_AT","UPDATED_AT","AMOUNT","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};
        public static final String[] TOKEN_INFO = {"SCOPE","RESOURCE_TYPE","RESOURCE_UUID","RESOURCE_OWNER_TYPE","RESOURCE_OWNER_UUID","__ROW_MD5","__DLH_IS_DELETED","__DLH_SYNC_TS","__DLH_START_TS","__DLH_FINISH_TS","__DLH_IS_ACTIVE"};

        private GustoHeaders() {}
    }

    public static final String[] dlhCommonColumns = new String[]{};


    public static final Map<String, String> ENTITY_VERSION_MAP = Map.<String, String>ofEntries(
            Map.entry(GustoEntityNames.TOKEN_INFO, "v1"),
            Map.entry(GustoEntityNames.COMPANY, "v1"),
            Map.entry(GustoEntityNames.COMPANY_ADMINS, "v1"),
            Map.entry(GustoEntityNames.CUSTOM_FIELDS, "v1"),
            Map.entry(GustoEntityNames.COMPANY_LOCATIONS, "v1"),
            Map.entry(GustoEntityNames.PAY_SCHEDULES, "v1"),
            Map.entry(GustoEntityNames.EARNING_TYPES, "v1"),
            Map.entry(GustoEntityNames.PAYROLLS, "v1"),
            Map.entry(GustoEntityNames.COMPANY_BENEFITS, "v1"),
            Map.entry(GustoEntityNames.BENEFITS, "v1"),
            Map.entry(GustoEntityNames.DEPARTMENTS, "v1"),
            Map.entry(GustoEntityNames.TIMESHEETS, "v1"),
            Map.entry(GustoEntityNames.EMPLOYEES, "v1"),
            Map.entry(GustoEntityNames.EMPLOYEE_HOME_ADDRESSES, "v1"),
            Map.entry(GustoEntityNames.EMPLOYEE_WORK_ADDRESSES, "v1"),
            Map.entry(GustoEntityNames.EMPLOYEE_CUSTOM_FIELDS, "v1"),
            Map.entry(GustoEntityNames.EMPLOYEE_TIME_OFF_ACTIVITIES, "v1"),
            Map.entry(GustoEntityNames.EMPLOYEE_TERMINATIONS, "v1"),
            Map.entry(GustoEntityNames.EMPLOYEE_REHIRE, "v1"),
            Map.entry(GustoEntityNames.JOBS, "v1"),
            Map.entry(GustoEntityNames.JOBS_COMPENSATIONS, "v1"),
            Map.entry(GustoEntityNames.EMPLOYEE_BENEFITS, "v1"),
            Map.entry(GustoEntityNames.CONTRIBUTION_EXCLUSIONS, "v1"),
            Map.entry(GustoEntityNames.GARNISHMENTS, "v1"),
            Map.entry(GustoEntityNames.CONTRACTORS, "v1"),
            Map.entry(GustoEntityNames.PAY_PERIODS, "v1"),
            Map.entry(GustoEntityNames.PAY_SCHEDULES_ASSIGNMENTS, "v1"),
            Map.entry(GustoEntityNames.LOCATIONS_MINIMUM_WAGES, "v1"),
            Map.entry(GustoEntityNames.CONTRACTOR_PAYMENTS, "v1"),
            Map.entry(GustoEntityNames.RECURRING_REIMBURSEMENTS, "v1"));

    public static List<String> excludedEntities = List.of();
    public static String getUrl(String entityName){
        return ENTITY_VERSION_MAP.get(entityName) + "/" + entityName.toLowerCase();
    }


    public static Map<String, String> ENTITY_API_PATH_MAP = new HashMap<>();

    // Define entity dependencies
    public static final Map<String, List<String>> ENTITY_DEPENDENCY_MAP = Map.ofEntries(Map.entry(
            GustoEntityNames.EMPLOYEES, List.of(
                    GustoEntityNames.EMPLOYEE_HOME_ADDRESSES, GustoEntityNames.EMPLOYEE_WORK_ADDRESSES, GustoEntityNames.EMPLOYEE_TERMINATIONS,
                    GustoEntityNames.EMPLOYEE_REHIRE, GustoEntityNames.JOBS, GustoEntityNames.GARNISHMENTS,
                            GustoEntityNames.EMPLOYEE_BENEFITS, GustoEntityNames.RECURRING_REIMBURSEMENTS)),
            Map.entry(GustoEntityNames.COMPANY_LOCATIONS, List.of(GustoEntityNames.LOCATIONS_MINIMUM_WAGES)),
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
            Map.entry(GustoEntityNames.COMPANY_BENEFITS, GustoHeaders.COMPANY_BENEFITS)
            //            Map.entry(GustoEntityNames.EARNING_TYPES, GustoHeaders.EARNING_TYPES),
            //             Map.entry(GustoEntityNames.CUSTOM_FIELDS, GustoHeaders.CUSTOM_FIELDS),
            //            Map.entry(GustoEntityNames.CONTRACTOR_PAYMENTS, GustoHeaders.CONTRACTOR_PAYMENTS),
            //            Map.entry(GustoEntityNames.EMPLOYEE_TIME_OFF_ACTIVITIES, GustoHeaders
            //            .EMPLOYEE_TIME_OFF_ACTIVITIES),
            //            Map.entry(GustoEntityNames.EMPLOYEE_REHIRE, GustoHeaders.EMPLOYEE_REHIRE),
            );


    public static Map<String, String> getEntityApiPathMap(String companyId){
        // Generate API path map

        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.COMPANY, ENTITY_VERSION_MAP.get(GustoEntityNames.COMPANY) + "/companies/" + companyId);
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.COMPANY_ADMINS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.COMPANY_ADMINS) + "/companies/" + companyId + "/admins");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.COMPANY_LOCATIONS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.COMPANY_LOCATIONS) + "/companies/" + companyId + "/locations");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.PAY_SCHEDULES, ENTITY_VERSION_MAP.get(
                GustoEntityNames.PAY_SCHEDULES) + "/companies/" + companyId + "/pay_schedules");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.COMPANY_BENEFITS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.COMPANY_BENEFITS) + "/companies/" + companyId + "/company_benefits");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.DEPARTMENTS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.DEPARTMENTS) + "/companies/" + companyId + "/departments");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.EMPLOYEES, ENTITY_VERSION_MAP.get(
                GustoEntityNames.EMPLOYEES) + "/companies/" + companyId + "/employees");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.PAYROLLS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.PAYROLLS) + "/companies/" + companyId + "/payrolls");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.BENEFITS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.BENEFITS) + "/benefits");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.TIMESHEETS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.TIMESHEETS) +"/companies/" + companyId + "/time_tracking/time_sheets");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.CONTRACTORS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.CONTRACTORS) + "/companies/" + companyId + "/contractors");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.EMPLOYEE_HOME_ADDRESSES, ENTITY_VERSION_MAP.get(
                GustoEntityNames.EMPLOYEE_HOME_ADDRESSES) + "/employees/{employee_id}/home_addresses");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.EMPLOYEE_WORK_ADDRESSES, ENTITY_VERSION_MAP.get(
                GustoEntityNames.EMPLOYEE_WORK_ADDRESSES) + "/employees/{employee_id}/work_addresses");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.EMPLOYEE_TERMINATIONS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.EMPLOYEE_TERMINATIONS) + "/employees/{employee_id}/terminations");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.EMPLOYEE_REHIRE, ENTITY_VERSION_MAP.get(
                GustoEntityNames.EMPLOYEE_REHIRE) + "/employees/{employee_id}/rehire");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.JOBS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.JOBS) + "/employees/{employee_id}/jobs");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.GARNISHMENTS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.GARNISHMENTS) + "/employees/{employee_id}/garnishments");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.EMPLOYEE_BENEFITS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.EMPLOYEE_BENEFITS) + "/employees/{employee_id}/employee_benefits");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.RECURRING_REIMBURSEMENTS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.RECURRING_REIMBURSEMENTS) + "/employees/{employee_id}/recurring_reimbursements");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.JOBS_COMPENSATIONS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.JOBS_COMPENSATIONS) + "/jobs/{job_id}/compensations");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.PAY_PERIODS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.PAY_PERIODS) + "/companies/" + companyId + "/pay_periods");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.PAY_SCHEDULES_ASSIGNMENTS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.PAY_SCHEDULES_ASSIGNMENTS) + "/companies/" + companyId + "/pay_schedules/assignments");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.EMPLOYEE_TIME_OFF_ACTIVITIES, ENTITY_VERSION_MAP.get(
                GustoEntityNames.EMPLOYEE_TIME_OFF_ACTIVITIES) + "/employees/{employee_id}/time_off_activities");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.CONTRACTOR_PAYMENTS, ENTITY_VERSION_MAP.get(
                GustoEntityNames.CONTRACTORS) + "/companies/" + companyId + "/contractor_payments");

        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.LOCATIONS_MINIMUM_WAGES, ENTITY_VERSION_MAP.get(
                        GustoEntityNames.LOCATIONS_MINIMUM_WAGES) + "/locations/{company_location_id}/minimum_wages");
        ENTITY_API_PATH_MAP.put(
                GustoEntityNames.EARNING_TYPES, ENTITY_VERSION_MAP.get(
                GustoEntityNames.EARNING_TYPES) + "/companies/" + companyId + "/earning_types");

        HEADERS_BY_ENTITY.keySet().forEach(entity -> ENTITY_API_PATH_MAP.putIfAbsent(entity, getUrl(entity)));

        return ENTITY_API_PATH_MAP;
    }

    private GustoConstants() {}
}

