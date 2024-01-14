package com.codingbottle.domain.region.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.region.service.RegionService;
import com.codingbottle.domain.user.model.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PatchMapping("/user/regions")
    public ResponseEntity<UserResponse> updateRegions(@AuthenticationPrincipal User user,
                                                      @RequestParam(value = "region") Region region) {
        User updateUser = regionService.updateRegions(region, user);

        return ResponseEntity.ok(UserResponse.of(updateUser));
    }
}
