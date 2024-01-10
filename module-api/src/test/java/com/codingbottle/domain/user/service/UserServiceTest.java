package com.codingbottle.domain.user.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.auth.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.codingbottle.fixture.DomainFixture.유저1;
import static com.codingbottle.fixture.DomainFixture.유저_닉네임_변경_요청1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("사용자의 닉네임을 변경한다.")
    void update_nickname() {
        //given
        given(userRepository.save(any())).willReturn(유저1.updateNickname(유저_닉네임_변경_요청1.nickname()));
        //when
        User user = userService.updateNickname(유저_닉네임_변경_요청1, 유저1);
        //then
        assertThat(user.getNickname()).isEqualTo(유저_닉네임_변경_요청1.nickname());
    }
}