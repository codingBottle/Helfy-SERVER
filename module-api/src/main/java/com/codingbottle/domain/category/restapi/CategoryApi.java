package com.codingbottle.domain.category.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.category.model.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "카테고리", description = "카테고리 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1")
public interface CategoryApi {
    @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회합니다.")
    @GetMapping("/categories")
    CategoryResponse findAll();
}
