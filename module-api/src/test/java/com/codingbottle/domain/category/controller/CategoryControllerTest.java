package com.codingbottle.domain.category.controller;

import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.category.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CategoryController 테스트")
@ContextConfiguration(classes = CategoryController.class)
@WebMvcTest(value = CategoryControllerTest.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CategoryControllerTest extends RestDocsTest {
    @MockBean
    CategoryService categoryService;

    @Test
    @DisplayName("카테고리 목록 조회")
    void find_all_categories() throws Exception {
        //given
        List<Category> categoryList = Arrays.asList(Category.values());

        Map<String, String> categories = categoryList.stream()
                .collect(Collectors.toMap(Category::name, Category::getDetail));

        given(categoryService.findAll()).willReturn(categories);
        //when & then
        mvc.perform(get("/api/v1/categories")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("categories-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader()));
    }
}