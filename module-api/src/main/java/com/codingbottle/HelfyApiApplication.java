package com.codingbottle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HelfyApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelfyApiApplication.class, args);
    }
}
