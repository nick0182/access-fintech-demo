package com.fintech.accessfintechdemo.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fintech.accessfintechdemo.dto.PriceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class JsonPriceJob implements Runnable {

    private final InputStream fileInputStream;

    private final Map<String, Double> lowestPrices;

    private static final ObjectMapper objectMapper = JsonMapper
            .builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    @Override
    public void run() {
        try {
            Map<String, Double> result = objectMapper.readValue(fileInputStream, new TypeReference<List<PriceDTO>>() {
                    }).stream()
                    .collect(Collectors.toMap(PriceDTO::getName, PriceDTO::getPrice, BinaryOperator.minBy(Comparator.comparing(price -> price))));

            result.forEach((stock, newLowestPrice) -> {
                log.info("Storing lowest price {} for stock {}", newLowestPrice, stock);
                lowestPrices.merge(stock, newLowestPrice, Math::min);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
