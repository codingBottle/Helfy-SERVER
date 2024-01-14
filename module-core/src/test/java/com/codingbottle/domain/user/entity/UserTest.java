package com.codingbottle.domain.user.entity;

import com.codingbottle.domain.region.entity.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static final String FIREBASE_UID = "firebaseUid";
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email";

    User user;
    User user2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firebaseUid(FIREBASE_UID)
                .nickname(NICKNAME)
                .email(EMAIL)
                .role(Role.ROLE_USER)
                .region(Region.NONE)
                .build();

        user2 = User.builder()
                .firebaseUid(FIREBASE_UID)
                .nickname(NICKNAME)
                .email(EMAIL)
                .role(Role.ROLE_USER)
                .region(Region.NONE)
                .build();
    }
    @Test
    @DisplayName("User 생성 테스트")
    void createUser() {
        // then
        assertAll(
                () -> assertThat(user.getFirebaseUid()).isEqualTo(FIREBASE_UID),
                () -> assertThat(user.getNickname()).isEqualTo(NICKNAME),
                () -> assertThat(user.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(user.getRole()).isEqualTo(Role.ROLE_USER),
                () -> assertThat(user.getRegion()).isEqualTo(Region.NONE)
        );
    }

    @Test
    @DisplayName("토큰 업데이트 테스트")
    void updateToken() {
        // when
        user.updateByFirebaseToken("newFirebaseUid", "newEmail");
        // then
        assertAll(
                () -> assertThat(user.getFirebaseUid()).isEqualTo("newFirebaseUid"),
                () -> assertThat(user.getEmail()).isEqualTo("newEmail")
        );
    }

    @Test
    @DisplayName("닉네임 업데이트 테스트")
    void updateNickname() {
        // when
        user.updateNickname("newNickname");
        // then
        assertThat(user.getNickname()).isEqualTo("newNickname");
    }

    @Test
    @DisplayName("지역 업데이트 테스트")
    void updateRegion() {
        // when
        user.updateRegion(Region.SEOUL);
        // then
        assertThat(user.getRegion()).isEqualTo(Region.SEOUL);
    }

    @Test
    @DisplayName("User equals 테스트")
    void equals() {
        // when
        boolean result = user.equals(user2);
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("User toString 테스트")
    void toStringTest() {
        // when
        String result = user.toString();
        // then
        assertThat(result).isEqualTo("User{" +
                "id=" + "null" +
                ", firebaseUid='" + "firebaseUid" + '\'' +
                ", nickname='" + "nickname" + '\'' +
                ", role=" + "ROLE_USER" +
                ", email='" + "email" + '\'' +
                ", region=" + "NONE" +
                '}');
    }

    @Test
    @DisplayName("equals 서로 다른 객체 테스트")
    void equalsDifferentObject() {
        // when
        boolean result = user.equals(new Object());
        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("equals null 테스트")
    void equalsNull() {
        // when
        boolean result = user.equals(null);
        // then
        assertThat(result).isFalse();
    }
}