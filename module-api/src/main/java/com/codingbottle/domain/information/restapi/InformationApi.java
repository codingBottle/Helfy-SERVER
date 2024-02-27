package com.codingbottle.domain.information.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.information.model.InformationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "정보 제공", description = "정보 제공 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1/information")
public interface InformationApi {
    @Operation(summary = "정보 제공", description = "정보를 제공합니다.")
    @GetMapping
    InformationResponse findByCategory(
            @RequestParam(name = "category") @Parameter(description = "카테고리를 url 파라미터로 지정해서 요청하세요.", example = "FLOODING") Category category
    );
}
