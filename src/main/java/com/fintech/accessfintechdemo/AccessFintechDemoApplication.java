package com.fintech.accessfintechdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AccessFintechDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccessFintechDemoApplication.class, args);
    }

}
