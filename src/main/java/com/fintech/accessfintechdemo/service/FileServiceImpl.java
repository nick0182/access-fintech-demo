package com.fintech.accessfintechdemo.service;

import com.fintech.accessfintechdemo.runner.TempDirCreate;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final Map<String, Double> lowestPricesScheduled;

    @Override
    public void storeFile(MultipartFile file) throws IOException {
        Path tempFilePath = Files.createFile(TempDirCreate.TEMP_DIR_PATH.resolve(Paths.get(Objects.requireNonNull(file.getOriginalFilename()))));
        File tempFile = tempFilePath.toFile();
        tempFile.deleteOnExit();
        file.transferTo(tempFilePath);
    }

    @Override
    public double getLowestStockPrice(String stock) {
        return lowestPricesScheduled.getOrDefault(stock, -1.0);
    }

    @Override
    public Map<String, Double> getAllLowestPrices() {
        return lowestPricesScheduled;
    }
}
