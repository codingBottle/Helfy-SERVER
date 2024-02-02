package com.codingbottle.domain.user.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.model.UserInfoResponse;
import com.codingbottle.domain.user.model.UserInfoUpdateRequest;
import com.codingbottle.domain.user.model.UserResponse;
import com.codingbottle.domain.user.service.UserService;
import com.codingbottle.redis.domain.quiz.model.QuizRankUserData;
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

        return ResponseEntity.ok(UserResponse.of(updateUser));
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUser(@AuthenticationPrincipal User user) {
        QuizRankUserData quizRankUserData = QuizRankUserData.of(user.getId(), user.getNickname());

        Long rank = quizRankRedisService.getRank(quizRankUserData);
        int score = quizRankRedisService.getScore(quizRankUserData);

        return ResponseEntity.ok(UserInfoResponse.of(user, rank, score));
    }
}
