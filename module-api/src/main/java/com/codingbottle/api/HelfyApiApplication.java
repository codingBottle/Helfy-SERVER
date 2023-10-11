package com.codingbottle.api;

import com.codingbottle.core.HelfyCoreApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {HelfyCoreApplication.class})
public class HelfyApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelfyApiApplication.class, args);
    }
}
