package com.codingbottle.domain.information.controller;

import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.information.service.InformationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static com.codingbottle.fixture.DomainFixture.정보_응답1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("InformationController 테스트")
@ContextConfiguration(classes = InformationController.class)
@WebMvcTest(value = InformationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class InformationControllerTest extends RestDocsTest {
    @MockBean
    private InformationService informationService;

    private static final String REQUEST_URL = "/api/v1/information";

    @Test
    @DisplayName("카테고리에 해당하는 정보를 조회한다.")
    void findByCategory() throws Exception {
        //given
        given(informationService.findByCategory(any(Category.class))).willReturn(정보_응답1);
        //when
        mvc.perform(get(REQUEST_URL)
                        .queryParam("category", "FLOOD")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("get-information",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        queryParameters(
                                parameterWithName("category").description("카테고리")),
                        responseFields(
                                fieldWithPath("id").description("정보 ID").type("Number"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("content").description("내용"),
                                fieldWithPath("news_url").description("뉴스 url"),
                                fieldWithPath("youtube_url").description("유튜브 url"),
                                fieldWithPath("image").description("게시물 이미지"),
                                fieldWithPath("image.id").description("게시물 이미지 id").type("Number"),
                                fieldWithPath("image.imageUrl").description("게시물 이미지 url")
                )));
    }
}