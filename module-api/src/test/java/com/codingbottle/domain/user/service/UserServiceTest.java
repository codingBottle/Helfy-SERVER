package com.codingbottle.domain.user.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.codingbottle.fixture.DomainFixture.유저1;
import static com.codingbottle.fixture.DomainFixture.유저_정보_수정_요청1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 정보를 변경한다.")
    void updateInfo() {
        // given
        given(userRepository.save(유저1)).willReturn(유저1.updateInfo(유저_정보_수정_요청1.nickname(), 유저_정보_수정_요청1.region()));
        // when
        User user = userService.updateInfo(유저_정보_수정_요청1, 유저1);
        // then
        assertAll(
                () -> assertThat(user.getNickname()).isEqualTo(유저_정보_수정_요청1.nickname()),
                () -> assertThat(user.getRegion()).isEqualTo(유저_정보_수정_요청1.region())
        );
    }
}