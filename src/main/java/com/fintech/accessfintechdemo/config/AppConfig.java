package com.fintech.accessfintechdemo.config;

import com.fintech.accessfintechdemo.runner.TempDirCreate;
import com.fintech.accessfintechdemo.scheduler.PriceScheduler;
import com.fintech.accessfintechdemo.service.FileService;
import com.fintech.accessfintechdemo.service.FileServiceImpl;
import com.fintech.accessfintechdemo.service.InputService;
import com.fintech.accessfintechdemo.service.InputServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    ExecutorService priceJobsExecutor() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean
    Map<String, Double> lowestPrices() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    Map<String, Double> lowestPricesScheduled() {
        return new ConcurrentHashMap<>();
    }

    @Bean(destroyMethod = "destroy")
    InputService inputService(ExecutorService priceJobsExecutor, Map<String, Double> lowestPrices, CSVFormat csvFormat) {
        return new InputServiceImpl(priceJobsExecutor, lowestPrices, csvFormat);
    }

    @Bean
    FileService fileService(Map<String, Double> lowestPricesScheduled) {
        return new FileServiceImpl(lowestPricesScheduled);
    }

    @Bean
    PriceScheduler priceScheduler(Map<String, Double> lowestPricesScheduled, CSVFormat csvFormat) {
        return new PriceScheduler(lowestPricesScheduled, csvFormat);
    }

    @Bean
    CommandLineRunner tempDirCreate() {
        return new TempDirCreate();
    }

    @Bean
    CSVFormat csvFormat() {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(
                        "Symbol",
                        "Date",
                        "Price",
                        "Volume")
                .setSkipHeaderRecord(true)
                .build();
    }
}
