package com.codingbottle.domain.category.controller;

import com.codingbottle.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "카테고리", description = "카테고리 API")
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    @Operation(summary = "카테고리 목록 조회")
    public ResponseEntity<Map<String, String>> findAll() {
        Map<String, String> categories = categoryService.findAll();

        return ResponseEntity.ok(categories);
    }
}
