package com.codingbottle.domain.user.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.model.UserInfoUpdateRequest;
import com.codingbottle.domain.user.model.UserProfileImageUpdateRequest;
import com.codingbottle.domain.user.model.UserResponse;
import com.codingbottle.domain.user.model.UserResponseWithRankInfo;
import com.codingbottle.domain.user.restapi.UserApi;
import com.codingbottle.domain.user.service.UserService;
import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.quiz.model.RankInfo;
import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    private final QuizRankRedisService quizRankRedisService;

    @Override
    public UserResponse updateUserInfo(@AuthenticationPrincipal User user,
                                                         UserInfoUpdateRequest userInfoUpdateRequest) {
        User updateUser = userService.updateInfo(userInfoUpdateRequest, user);

        return UserResponse.from(updateUser);
    }

    @Override
    public UserResponse updateProfileImage(@AuthenticationPrincipal User user,
                                           UserProfileImageUpdateRequest userProfileImageUpdateRequest) {
        User updateUser = userService.updateProfileImage(user, userProfileImageUpdateRequest);

        return UserResponse.from(updateUser);
    }

    @Override
    public UserResponseWithRankInfo getUser(@AuthenticationPrincipal User user) {
        UserInfo userInfo = UserInfo.of(user.getId(), user.getNickname());
        RankInfo rankInfo = quizRankRedisService.getRankInfo(userInfo);

        return UserResponseWithRankInfo.of(UserResponse.from(user), rankInfo);
    }
}
