package com.codingbottle.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestPing {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
