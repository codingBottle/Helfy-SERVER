package com.codingbottle.domain.region.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.region.entity.Region;
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

    @Test
    @DisplayName("사용자 지역 수정")
    void update_user_regions() {
        // given
        var region = Region.SEOUL;

        User user = User.builder()
                .region(Region.NONE)
                .build();

        // when
        var updateUser = regionService.updateRegions(region, user);

        // then
        assertThat(updateUser.getRegion()).isEqualTo(region);
    }

}