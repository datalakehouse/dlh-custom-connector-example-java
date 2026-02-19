package io.datalakehouse.utils;

import io.datalakehouse.helper.impl.DownloadHelper;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvDataBuffer is responsible for buffering CSV rows in memory and writing them to disk in fixed-size chunks.
 *
 * It is used during asynchronous processing of dependent entity responses.
 * For example, multiple dependent API calls such as:
 * - /employee/1/home_address
 * - /employee/2/home_address
 * may be processed in parallel and their results buffered before being written.
 *
 * The class maintains an internal row buffer, tracks the total number of data rows processed,
 * and writes CSV files incrementally once the configured chunk size is reached.
 */
public class CsvDataBuffer {

    private final String entity;
    private final int chunkSize;
    private final Instant entityProcessingStartTime = Instant.now();
    private volatile int lineCount = 0;
    private final String connectorType;
    private volatile boolean containsHeaderRow = false;
    private final List<String[]> rowValues = new ArrayList<>();

    public CsvDataBuffer(String[] headers, int chunkSize, String entity, String connectorType) {
        rowValues.add(headers);
        containsHeaderRow = true;
        this.connectorType = connectorType;
        this.chunkSize = chunkSize;
        this.entity = entity;
    }

    public synchronized void addRowValues(String[] values, DownloadHelper downloadHelper) throws IOException {
        rowValues.add(values);
        lineCount++;
        int rowValuesCount = containsHeaderRow ? rowValues.size() - 1 : rowValues.size();
        // if rowValuesCount is reached csv chunk size then csv will be processed
        if (rowValuesCount % chunkSize == 0) {
            containsHeaderRow = false;
            downloadHelper.writeChunkToCsv(entity, rowValues, lineCount, connectorType);
            rowValues.clear();
        }
    }

    public synchronized void flushRemaining(DownloadHelper downloadHelper) throws IOException {
        downloadHelper.writeChunkToCsv(entity, rowValues, lineCount, connectorType);
        downloadHelper.logEndHistory(entity, connectorType, lineCount);
        rowValues.clear();
    }

    public synchronized Integer getTotalRecordsCount() {
        return lineCount;
    }

    public Instant getEntityProcessingStartTime() {
        return entityProcessingStartTime;
    }

}