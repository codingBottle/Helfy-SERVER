package com.codingbottle.domain.category.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CategoryService 테스트")
class CategoryServiceTest {
    CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService();
    }

    @Test
    @DisplayName("카테고리 목록 조회")
    void find_all_categories() {
        // given & when
        var categories = categoryService.findAll();

        // then
        assertThat(categories).isNotEmpty();
    }
}