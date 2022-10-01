package com.fintech.accessfintechdemo.service;

import com.fintech.accessfintechdemo.job.CSVPriceJob;
import com.fintech.accessfintechdemo.job.JsonPriceJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Slf4j
@AllArgsConstructor
public class InputServiceImpl implements InputService {

    private final ExecutorService priceJobsExecutor;

    private final Map<String, Double> lowestPrices;

    private final CSVFormat csvFormat;

    @Override
    public void createCSVJob(InputStream fileInputStream) {
        priceJobsExecutor.submit(new CSVPriceJob(fileInputStream, lowestPrices, csvFormat));
    }

    @Override
    public double getLowestStockPrice(String stock) {
        return lowestPrices.getOrDefault(stock, -1.0);
    }

    @Override
    public Map<String, Double> getAllLowestPrices() {
        return lowestPrices;
    }

    @Override
    public void destroy() {
        log.info("Destroying executor");
        priceJobsExecutor.shutdownNow();
    }

    @Override
    public void createJsonJob(InputStream fileInputStream) {
        priceJobsExecutor.submit(new JsonPriceJob(fileInputStream, lowestPrices));
    }
}
