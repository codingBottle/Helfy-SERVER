package com.codingbottle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.codingbottle")
public class HelfyApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelfyApiApplication.class, args);
    }
}
