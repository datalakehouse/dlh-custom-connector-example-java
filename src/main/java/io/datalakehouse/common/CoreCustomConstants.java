package io.datalakehouse.common;

import java.util.Set;

public class CoreCustomConstants {
    public enum HISTORY_ENTITY_TYPE {
        LIGHTSPEED_RETAIL_X_ENTITY, GUSTO_ENTITY, ONE_PAGE_CRM_ENTITY, FRESHDESK_ENTITY, FRESHSERVICE_ENTITY,
        OPEN_TABLE_ENTITY, SERVICE_NOW_ENTITY, BAMBOO_HR_ENTITY, HEARTS_LAND_POS_ENTITY
    }

    public static final Set<String> DLH_TS_COLUMNS = Set.of(
            "__DLH_SYNC_TS",
            "__DLH_START_TS",
            "__DLH_FINISH_TS");
}
