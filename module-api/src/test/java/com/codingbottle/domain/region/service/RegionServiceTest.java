package com.codingbottle.domain.region.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RegionService 테스트")
class RegionServiceTest {
    RegionService regionService;

    @BeforeEach
    void setUp() {
        regionService = new RegionService();
    }

    @Test
    @DisplayName("지역 목록 조회")
    void find_all_regions() {
        // given & when
        var regions = regionService.findAll();

        // then
        assertThat(regions).isNotEmpty();
    }

}