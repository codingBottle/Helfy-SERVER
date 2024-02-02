package com.codingbottle.domain.region.controller;

import com.codingbottle.domain.region.service.RegionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "지역", description = "지역 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/regions")
    public ResponseEntity<Map<String, String>> findAll() {
        Map<String, String> regions = regionService.findAll();

        return ResponseEntity.ok(regions);
    }
}
