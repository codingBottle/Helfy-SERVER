package com.codingbottle.domain.category.controller;

import com.codingbottle.domain.category.model.CategoryResponse;
import com.codingbottle.domain.category.restapi.CategoryApi;
import com.codingbottle.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {
    private final CategoryService categoryService;

    @Override
    public CategoryResponse findAll() {
        return CategoryResponse.from(categoryService.findAll());
    }
}
