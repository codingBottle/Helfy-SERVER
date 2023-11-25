package com.codingbottle.domain.information.repo;

import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.information.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationRepository extends JpaRepository<Information, Long> {
    Information findByCategory(Category category);
}
