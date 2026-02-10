package io.datalakehouse.restconnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.HeartsLandPosConstants;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HeartsLandPOSConnectionType extends RestConnectionType {

    Logger logger = Logger.getLogger(HeartsLandPOSConnectionType.class.getName());

    public HeartsLandPOSConnectionType(String baseUrl, String accessToken) {
        super(baseUrl, accessToken);
    }

    /**
     * Build pagination configuration for Heartland POS (Global Payments).
     * Uses page-based pagination with page and page_size parameters.
     */
    public PaginationInfo buildPagination(Integer pageSize) {
        int size = (pageSize != null && pageSize > 0) ? pageSize : HeartsLandPosConstants.MAX_PAGE_SIZE;
        return new PaginationInfo(
                HeartsLandPosConstants.PAGE_PARAM,
                HeartsLandPosConstants.PAGE_SIZE_PARAM,
                1,
                size,
                state -> true,
                null
        );
    }

    @Override
    public void fetchDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, PaginationInfo.PageProcessor pageProcessor)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        int currentPage = 1; // Start from page 1
        int totalFetched = 0;
        boolean hasMoreData = true;

        while (hasMoreData) {
            Map<String, String> pagedQuery = new HashMap<>();
            if (queryParams != null) {
                pagedQuery.putAll(queryParams);
            }

            // Heartland POS uses page and page_size parameters
            pagedQuery.put(paginationInfo.pageParam(), String.valueOf(currentPage));
            pagedQuery.put(paginationInfo.sizeParam(), String.valueOf(paginationInfo.pageSize()));

            byte[] pageBytes;
            try (InputStream pageStream = fetchData(entity, apiPath, pagedQuery, customHeaders, entityIds)) {
                if (pageStream == null) {
                    logger.info(() -> "No more data returned for entity: " + entity);
                    break;
                }
                pageBytes = pageStream.readAllBytes();
            }

            // Parse response
            JsonNode root = mapper.readTree(pageBytes);

            // Check pagination info from response
            int currentPageSize = 0;
            int totalRecordCount = 0;
            int requestedPageSize = paginationInfo.pageSize();

            if (root.has(HeartsLandPosConstants.CURRENT_PAGE_SIZE)) {
                currentPageSize = root.get(HeartsLandPosConstants.CURRENT_PAGE_SIZE).asInt(0);
            }

            if (root.has(HeartsLandPosConstants.TOTAL_RECORD_COUNT)) {
                totalRecordCount = root.get(HeartsLandPosConstants.TOTAL_RECORD_COUNT).asInt(0);
            }

            if (root.has(HeartsLandPosConstants.PAGING)) {
                JsonNode pagingNode = root.get(HeartsLandPosConstants.PAGING);
                if (pagingNode.has(HeartsLandPosConstants.PAGE_SIZE_PARAM)) {
                    requestedPageSize = pagingNode.get(HeartsLandPosConstants.PAGE_SIZE_PARAM).asInt(paginationInfo.pageSize());
                }
            }

            totalFetched += currentPageSize;

            // Calculate if there are more pages based on page size logic
            // If current page has fewer records than requested page size, it's the last page
            hasMoreData = (currentPageSize >= requestedPageSize) && (currentPageSize > 0);

            // Create pagination state
            PaginationInfo.PaginationState state = new PaginationInfo.PaginationState(
                    currentPage,
                    totalFetched,
                    currentPageSize,
                    totalRecordCount > 0 ? totalRecordCount : null,
                    null // maxPage can be calculated as (totalRecordCount / pageSize) + 1
            );

            // Process this page immediately
            boolean shouldContinue;
            try (InputStream pageInputStream = new ByteArrayInputStream(pageBytes)) {
                shouldContinue = pageProcessor.processPage(pageInputStream, state);
            }

            // Log pagination info if logging is enabled
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                logger.info(String.format("Processed page %d for entity %s: fetched %d items, total: %d/%d",
                        currentPage, entity, currentPageSize, totalFetched, totalRecordCount));
            }

            // Check if we should continue
            if (!shouldContinue || currentPageSize == 0) {
                if (logger.isLoggable(java.util.logging.Level.INFO)) {
                    logger.info(String.format("Stopping pagination for entity %s. shouldContinue: %b, currentPageSize: %d",
                            entity, shouldContinue, currentPageSize));
                }
                break;
            }

            // Move to next page
            currentPage++;
        }
    }

    @Override
    protected void refreshOAuth2Token() throws IOException {
        // Heartland POS uses Bearer token authentications
        throw new UnsupportedOperationException("Token refresh not implemented for Heartland POS");
    }
}
