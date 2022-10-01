package com.fintech.accessfintechdemo.controller;

import com.fintech.accessfintechdemo.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.storeFile(file);
    }

    @GetMapping("/lowest/{stock}")
    public double getLowestStockPrice(@PathVariable String stock) {
        return fileService.getLowestStockPrice(stock);
    }

    @GetMapping("/lowest")
    public Map<String, Double> getAllLowestPrices() {
        return fileService.getAllLowestPrices();
    }
}
