package io.datalakehouse.restconnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.datalakehouse.common.OpenTableConstants;
import io.datalakehouse.connectors.core.PaginationInfo;
import io.datalakehouse.connectors.impl.RestConnectionType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class OpenTableRestConnectionType extends RestConnectionType {

    Logger logger = Logger.getLogger(OpenTableRestConnectionType.class.getName());

    /**
     * Constructor for OpenTable with OAuth2 support (refresh token flow).
     */
    public OpenTableRestConnectionType(String baseUrl, String accessToken) {
        super(baseUrl, accessToken, null, null, null, null);
    }

    /**
     * Build pagination configuration for OpenTable.
     * OpenTable uses cursor-based pagination with offset/limit and hasNextPage.
     */
    public PaginationInfo buildPagination(Integer pageSize) {
        int size = (pageSize != null && pageSize > 0) ? pageSize : OpenTableConstants.MAX_PAGE_SIZE;
        // OpenTable uses offset-based pagination, continuePredicate is handled in fetchDataPaginated
        return new PaginationInfo(
                OpenTableConstants.OFFSET,  // pageParam is "offset"
                OpenTableConstants.LIMIT,   // sizeParam is "limit"
                0,                           // startPage is offset 0
                size,
                state -> true,  // We'll check hasNextPage in fetchDataPaginated instead
                null            // No metadata extraction needed
        );
    }


    @Override
    public void fetchDataPaginated(
            String entity, String apiPath, Map<String, String> queryParams, Map<String, String> customHeaders,
            List<String> entityIds, PaginationInfo paginationInfo, PaginationInfo.PageProcessor pageProcessor)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        int offset = 0; // Start from offset 0 for cursor-based pagination
        int totalFetched = 0;
        int pageNumber = 1; // Track page number for state reporting

        boolean hasNextPage = true;
        while (hasNextPage) {
            Map<String, String> pagedQuery = new HashMap<>();
            if (queryParams != null) {
                pagedQuery.putAll(queryParams);
            }

            // OpenTable uses offset and limit for pagination
            pagedQuery.put(paginationInfo.pageParam(), String.valueOf(offset));
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

            // Check if hasNextPage exists and is true
            hasNextPage = root.has(OpenTableConstants.HAS_NEXT_PAGE) &&
                    root.get(OpenTableConstants.HAS_NEXT_PAGE).asBoolean();


            int lastPageSize = checkLastPageSize(root);
            totalFetched += lastPageSize;

            // Create pagination state
            PaginationInfo.PaginationState state = new PaginationInfo.PaginationState(
                    pageNumber,
                    totalFetched,
                    lastPageSize,
                    null, // totalCount not provided by OpenTable
                    null  // maxPage not provided by OpenTable
            );

            // Process this page immediately
            boolean shouldContinue;
            try (InputStream pageInputStream = new ByteArrayInputStream(pageBytes)) {
                shouldContinue = pageProcessor.processPage(pageInputStream, state);
            }

            // Log pagination info if logging is enabled
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                logger.info(String.format("Processed page %d for entity %s: fetched %d items, total: %d, hasNextPage: %b",
                        pageNumber, entity, lastPageSize, totalFetched, hasNextPage));
            }

            // Check if we should continue
            if (!shouldContinue || lastPageSize == 0) {
                if (logger.isLoggable(java.util.logging.Level.INFO)) {
                    logger.info(String.format("Stopping pagination for entity %s. shouldContinue: %b, hasNextPage: %b, lastPageSize: %d",
                            entity, shouldContinue, hasNextPage, lastPageSize));
                }
                break;
            }

            // Move to next page by incrementing offset
            offset += paginationInfo.pageSize();
            pageNumber++;
        }
    }

    private int checkLastPageSize(JsonNode node) {
        if (node.isArray()) {
            return node.size();
        }
        if (node.isObject() && node.has(OpenTableConstants.ITEMS)) {
                JsonNode itemsNode = node.get(OpenTableConstants.ITEMS);
                if (itemsNode.isArray()) {
                    return itemsNode.size();
                }
            }

        return 0;
    }
}
