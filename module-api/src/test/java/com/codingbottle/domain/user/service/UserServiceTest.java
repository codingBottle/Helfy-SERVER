package com.codingbottle.domain.user.service;

import com.codingbottle.auth.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.codingbottle.fixture.DomainFixture.유저1;
import static com.codingbottle.fixture.DomainFixture.유저_닉네임_변경_요청1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자의 닉네임을 변경한다.")
    void update_nickname() {
        //when
        User user = userService.updateNickname(유저_닉네임_변경_요청1, 유저1);
        //then
        assertThat(user.getUsername()).isEqualTo(유저_닉네임_변경_요청1.nickname());
    }
}