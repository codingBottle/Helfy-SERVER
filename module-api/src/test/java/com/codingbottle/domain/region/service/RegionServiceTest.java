package com.codingbottle.domain.region.service;

import com.codingbottle.domain.region.entity.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RegionService 테스트")
class RegionServiceTest {
    private RegionService regionService;

    @BeforeEach
    void setUp() {
        regionService = new RegionService();
    }

    @Test
    @DisplayName("지역 목록 조회")
    void find_all_regions() {
        //when
        var regions = regionService.findAll();

        LinkedHashMap<String, String> expected  = Arrays.stream(Region.values())
                .filter(region -> !region.equals(Region.NONE))
                .sorted(Comparator.comparing(Region::getCode))
                .collect(Collectors.toMap(Region::name, Region::getDetail, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        // then
        assertThat(regions).isEqualTo(expected);
    }
}