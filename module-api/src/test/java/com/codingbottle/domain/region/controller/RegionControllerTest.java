package com.codingbottle.domain.region.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.region.service.RegionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import java.util.stream.Collectors;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static com.codingbottle.fixture.DomainFixture.유저1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("RegionController 테스트")
@ContextConfiguration(classes = RegionController.class)
@WebMvcTest(value = RegionController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class RegionControllerTest extends RestDocsTest {
    @MockBean
    RegionService regionService;

    @Test
    @DisplayName("지역 목록 조회")
    void find_all_regions() throws Exception {
        //given
        List<Region> regions = Arrays.asList(Region.values());

        Map<String, String> regionList = regions.stream()
                .filter(region -> !region.equals(Region.NONE))
                .sorted(Comparator.comparing(Region::getCode))
                .collect(Collectors.toMap(Region::name, Region::getDetail, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        given(regionService.findAll()).willReturn(regionList);
        //when & from
        mvc.perform(get("/api/v1/regions")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("regions-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader()));
    }

    @Test
    @DisplayName("사용자 지역 수정")
    void update_user_region() throws Exception {
        //given
        given(regionService.updateRegions(any(Region.class), any(User.class))).willReturn(유저1);
        //when & then
        mvc.perform(patch("/api/v1/user/regions")
                        .queryParam("region", "BUSAN")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("update-user-region",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader()
                        , queryParameters(
                                parameterWithName("region")
                                        .description("변경 지역 (ex. SEOUL, GYEONGGI, INCHEON)")
                        ),
                        responseFields(
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("region").description("사용자 지역")
                        )));
    }

}