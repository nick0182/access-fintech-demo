package com.fintech.accessfintechdemo.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class TempDirCreate implements CommandLineRunner {

    public static final Path TEMP_DIR_PATH = Paths.get(System.getProperty("user.home"), "price-files");

    @Override
    public void run(String... args) throws IOException {
        log.info("Home path: {}", TEMP_DIR_PATH);
        Files.createDirectory(TEMP_DIR_PATH);
        File tempDir = TEMP_DIR_PATH.toFile();
        tempDir.deleteOnExit();
    }
}
