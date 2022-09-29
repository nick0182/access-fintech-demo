package com.fintech.accessfintechdemo.config;

import com.fintech.accessfintechdemo.service.InputService;
import com.fintech.accessfintechdemo.service.InputServiceImpl;
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

    @Bean(destroyMethod = "destroy")
    InputService inputService(ExecutorService priceJobsExecutor, Map<String, Double> lowestPrices) {
        return new InputServiceImpl(priceJobsExecutor, lowestPrices);
    }
}
