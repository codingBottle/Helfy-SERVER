package com.codingbottle.domain.user.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.model.UserInfoUpdateRequest;
import com.codingbottle.domain.user.model.UserResponse;
import com.codingbottle.domain.user.model.UserResponseWithRankInfo;
import com.codingbottle.domain.user.service.UserService;
import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.quiz.model.RankInfo;
import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 정보", description = "사용자 정보 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final QuizRankRedisService quizRankRedisService;

    @PatchMapping("/user")
    public ResponseEntity<UserResponse> updateNickname(@AuthenticationPrincipal User user,
                                                       @RequestBody UserInfoUpdateRequest userInfoUpdateRequest) {
        User updateUser = userService.updateInfo(userInfoUpdateRequest, user);

        return ResponseEntity.ok(UserResponse.from(updateUser));
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseWithRankInfo> getUser(@AuthenticationPrincipal User user) {
        UserInfo userInfo = UserInfo.of(user.getId(), user.getNickname());
        RankInfo rankInfo = quizRankRedisService.getRankInfo(userInfo);

        return ResponseEntity.ok(UserResponseWithRankInfo.of(UserResponse.from(user), rankInfo));
    }
}
