package com.codingbottle.api.domain.category.controller;

import com.codingbottle.api.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<Map<String, String>> findAll() {
        Map<String, String> categories = categoryService.findAll();

        return ResponseEntity.ok(categories);
    }
}
