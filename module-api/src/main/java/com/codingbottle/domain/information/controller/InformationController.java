package com.codingbottle.domain.information.controller;

import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.information.model.InformationResponse;
import com.codingbottle.domain.information.restapi.InformationApi;
import com.codingbottle.domain.information.service.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class InformationController implements InformationApi {
    private final InformationService informationService;

    @Override
    public InformationResponse findByCategory(Category category) {
        return informationService.findByCategory(category);
    }
}
