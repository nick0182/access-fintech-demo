package com.fintech.accessfintechdemo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface InputService {

    void createCSVJob(InputStream fileInputStream) throws IOException;

    double getLowestStockPrice(String stock);

    Map<String, Double> getAllLowestPrices();

    void destroy();

    void createJsonJob(InputStream fileInputStream);
}
