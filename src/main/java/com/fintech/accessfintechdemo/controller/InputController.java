package com.fintech.accessfintechdemo.controller;

import com.fintech.accessfintechdemo.service.InputService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/input")
@AllArgsConstructor
public class InputController {

    private final InputService inputService;

    @PostMapping("/csv")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {
        inputService.createCSVJob(file.getInputStream());
    }

    @PostMapping("/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadJson(@RequestParam("file") MultipartFile file) throws IOException {
        inputService.createJsonJob(file.getInputStream());
    }

    @GetMapping("/lowest/{stock}")
    public double getLowestStockPrice(@PathVariable String stock) {
        return inputService.getLowestStockPrice(stock);
    }

    @GetMapping("/lowest")
    public Map<String, Double> getAllLowestPrices() {
        return inputService.getAllLowestPrices();
    }
}
