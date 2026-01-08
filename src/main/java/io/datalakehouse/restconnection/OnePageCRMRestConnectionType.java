package io.datalakehouse.restconnection;

import io.datalakehouse.common.OnePageCRMConstants;
import io.datalakehouse.connectors.core.AuthConfig;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;
import java.util.Map;

/**
 * OnePageCRM specific REST connection with authentication support.
 */
public class OnePageCRMRestConnectionType extends RestConnectionType {

    /**
     * Constructor for Basic authentication (username/password).
     * OnePageCRM typically uses Basic Auth with email+hash as username.
     *
     * @param baseUrl OnePageCRM API base URL
     * @param username OnePageCRM username (email with .RANDOM_HASH appended)
     * @param password OnePageCRM password
     */
    public OnePageCRMRestConnectionType(String baseUrl, String username, String password) {
        super(baseUrl, null, AuthConfig.Presets.basic(username, password));
    }

    /**
     * Build pagination configuration for OnePageCRM.
     * OnePageCRM uses page-based pagination with page and per_page parameters.
     */
    public PaginationInfo buildPagination(Integer pageSize) {
        int size = (pageSize != null && pageSize > 0) ? pageSize : OnePageCRMConstants.MAX_PAGE_SIZE;
        // stop when returned page has fewer than requested items
        return new PaginationInfo(
                OnePageCRMConstants.PAGE,
                OnePageCRMConstants.PER_PAGE,
                1,
                size,
                state -> {
                    if (state.maxPage() != null) {
                        return state.page() < state.maxPage();
                    }
                    return false;
                },
                Map.of(
                        PaginationInfo.PaginationKey.TOTAL_COUNT, OnePageCRMConstants.PAGINATION_META_TOTAL_COUNT,
                        PaginationInfo.PaginationKey.MAX_PAGE, OnePageCRMConstants.PAGINATION_META_MAX_PAGE
                      )
        );
    }
}
