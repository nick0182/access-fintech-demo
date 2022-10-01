package com.fintech.accessfintechdemo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface FileService {

    void storeFile(MultipartFile file) throws IOException;

    double getLowestStockPrice(String stock);

    Map<String, Double> getAllLowestPrices();
}
