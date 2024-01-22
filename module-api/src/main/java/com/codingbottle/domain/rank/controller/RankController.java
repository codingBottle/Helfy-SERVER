package com.codingbottle.domain.rank.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.redis.service.QuizRankRedisService;
import com.codingbottle.domain.rank.model.UsersRankResponse;
import com.codingbottle.domain.rank.model.UserRankResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "랭킹", description = "랭킹 API")
@RestController
@RequestMapping("/api/v1/rank")
@RequiredArgsConstructor
public class RankController {
    private final QuizRankRedisService quizRankRedisService;

    @GetMapping
    public ResponseEntity<UsersRankResponse> getRank() {
        List<UserRankResponse> rank = quizRankRedisService.getUsersRank();
        return ResponseEntity.ok(UsersRankResponse.of(rank));
    }

    @GetMapping("/user")
    public ResponseEntity<UserRankResponse> getUserRank(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(quizRankRedisService.getUserRank(user));
    }
}
