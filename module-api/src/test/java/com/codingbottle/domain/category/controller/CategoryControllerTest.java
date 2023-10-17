package com.codingbottle.domain.category.controller;

import com.codingbottle.docs.config.RestDocsConfig;
import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CategoryController 테스트")
@ContextConfiguration(classes = CategoryController.class)
@WebMvcTest(value = CategoryController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
class CategoryControllerTest {
    MockMvc mvc;

    @MockBean
    CategoryService categoryService;

    @BeforeEach
    void setUp(WebApplicationContext context,
               RestDocumentationContextProvider provider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(provider))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("카테고리 목록 조회")
    void find_all_categories() throws Exception {
        //given
        List<Category> categoryList = Arrays.asList(Category.values());

        Map<String, String> categories = categoryList.stream()
                .collect(Collectors.toMap(Category::name, Category::getDetail));

        given(categoryService.findAll()).willReturn(categories);
        //when & then
        mvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andDo(document("categories",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }
}