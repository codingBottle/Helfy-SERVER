package com.codingbottle.domain.information.service;

import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.information.entity.Information;
import com.codingbottle.domain.information.repo.InformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InformationService {
    private final InformationRepository informationRepository;

    public Information findByCategory(Category category) {
        return informationRepository.findByCategory(category);
    }
}
