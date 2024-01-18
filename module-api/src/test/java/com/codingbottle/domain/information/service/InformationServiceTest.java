package com.codingbottle.domain.information.service;

import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.information.model.InformationResponse;
import com.codingbottle.domain.information.repo.InformationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.codingbottle.fixture.DomainFixture.정보1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InformationServiceTest {
    @InjectMocks
    private InformationService informationService;

    @Mock
    private InformationRepository informationRepository;

    @Test
    @DisplayName("카테고리에 해당하는 정보를 조회한다.")
    void findByCategory() {
        //given
        given(informationRepository.findByCategory(any(Category.class))).willReturn(정보1);
        //when
        InformationResponse information = informationService.findByCategory(Category.FLOOD);
        //then
        assertThat(information).isEqualTo(InformationResponse.from(정보1));
    }
}