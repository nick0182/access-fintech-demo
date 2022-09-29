package com.fintech.accessfintechdemo.job;

import com.fintech.accessfintechdemo.dto.PriceDTO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class CSVPriceJob implements Runnable {

    private final InputStream fileInputStream;

    private static final CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .setHeader(
                    "Symbol",
                    "Date",
                    "Price",
                    "Volume")
            .setSkipHeaderRecord(true)
            .build();

    private final Map<String, Double> lowestPrices;

    @SneakyThrows
    @Override
    public void run() {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(fileInputStream))) {
            Map<String, Double> result = csvFormat
                    .parse(reader)
                    .stream()
                    .map(csvRecord -> PriceDTO.builder().name(csvRecord.get("Symbol")).price(Double.parseDouble(csvRecord.get("Price"))).build())
                    .collect(Collectors.toMap(PriceDTO::getName, PriceDTO::getPrice, BinaryOperator.minBy(Comparator.comparing(price -> price))));

            result.forEach((stock, newLowestPrice) -> {
                log.info("Storing lowest price {} for stock {}", newLowestPrice, stock);
                lowestPrices.merge(stock, newLowestPrice, Math::min);
            });
        }
    }
}
