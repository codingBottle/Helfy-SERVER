package com.codingbottle.domain.region.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.region.model.RegionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "지역", description = "지역 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1/regions")
public interface RegionApi {
    @Operation(summary = "지역 목록 조회", description = "지역 목록을 조회합니다.")
    @GetMapping
    RegionResponse findAll();
}
