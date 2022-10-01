package com.fintech.accessfintechdemo.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fintech.accessfintechdemo.dto.PriceDTO;
import com.fintech.accessfintechdemo.runner.TempDirCreate;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
public class PriceScheduler {

    enum Extension {
        CSV,
        JSON
    }

    private final Map<String, Double> lowestPricesScheduled;

    private final CSVFormat csvFormat;

    private static final ObjectMapper objectMapper = JsonMapper
            .builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS)
    public void readSources() throws IOException {
        log.info("Doing work");
        try (Stream<Path> pathStream = Files.walk(TempDirCreate.TEMP_DIR_PATH, 1)) {
            pathStream
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(filePath -> {
                        log.info("Found file: {}", filePath.getFileName());
                        File file = filePath.toFile();
                        Extension extension = resolveExtension(file.getName());
                        switch (extension) {
                            case CSV -> resolvePricesFromCSV(file);
                            case JSON -> resolvePricesFromJson(file);
                        }
                    });
        }
        log.info("Count of computed entities: {}", lowestPricesScheduled.size());
    }

    private Extension resolveExtension(String filename) {
        if (!filename.contains(".")) {
            throw new IllegalArgumentException("File extension is absent");
        } else {
            return Extension.valueOf(filename.substring(filename.lastIndexOf(".") + 1).toUpperCase());
        }
    }

    @SneakyThrows
    private void resolvePricesFromCSV(File csv) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(csv))) {
            Map<String, Double> result = csvFormat
                    .parse(reader)
                    .stream()
                    .map(csvRecord -> PriceDTO.builder().name(csvRecord.get("Symbol")).price(Double.parseDouble(csvRecord.get("Price"))).build())
                    .collect(Collectors.toMap(PriceDTO::getName, PriceDTO::getPrice, BinaryOperator.minBy(Comparator.comparing(price -> price))));

            result.forEach((stock, newLowestPrice) -> lowestPricesScheduled.merge(stock, newLowestPrice, Math::min));
        }
    }

    @SneakyThrows
    private void resolvePricesFromJson(File json) {
        Map<String, Double> result = objectMapper.readValue(json, new TypeReference<List<PriceDTO>>() {
                }).stream()
                .collect(Collectors.toMap(PriceDTO::getName, PriceDTO::getPrice, BinaryOperator.minBy(Comparator.comparing(price -> price))));

        result.forEach((stock, newLowestPrice) -> lowestPricesScheduled.merge(stock, newLowestPrice, Math::min));
    }
}
