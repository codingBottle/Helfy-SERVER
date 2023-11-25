package com.codingbottle.domain.information.controller;

import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.information.entity.Information;
import com.codingbottle.domain.information.service.InformationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "정보 제공", description = "정보 제공 API")
@RestController
@RequestMapping("/api/v1/information")
@RequiredArgsConstructor
public class InformationController {
    private final InformationService informationService;

    @GetMapping
    public ResponseEntity<Information> findByCategory(@RequestParam Category category) {
        Information information = informationService.findByCategory(category);

        return ResponseEntity.ok(information);
    }
}
