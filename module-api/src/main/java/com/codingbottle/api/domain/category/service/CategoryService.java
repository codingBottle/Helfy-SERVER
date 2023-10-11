package com.codingbottle.api.domain.category.service;

import com.codingbottle.core.domain.category.entity.Category;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    public Map<String, String> findAll() {
        List<Category> categoryList = Arrays.asList(Category.values());

        return categoryList.stream()
                .collect(Collectors.toMap(Category::name, Category::getDetail));
    }
}
