package com.codingbottle.domain.rank.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.redis.domain.quiz.model.QuizRankUserData;
import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
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
import java.util.stream.Collectors;

@Tag(name = "랭킹", description = "랭킹 API")
@RestController
@RequestMapping("/api/v1/rank")
@RequiredArgsConstructor
public class RankController {
    private final QuizRankRedisService quizRankRedisService;

    @GetMapping
    public ResponseEntity<UsersRankResponse> getRankResponse() {
        List<QuizRankUserData> usersData = quizRankRedisService.getUsersRank();

        List<UserRankResponse> rank = usersData.stream()
                .map(user -> UserRankResponse.of(quizRankRedisService.getRank(user) + 1, user, quizRankRedisService.getScore(user)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(UsersRankResponse.of(rank));
    }

    @GetMapping("/user")
    public ResponseEntity<UserRankResponse> getUserRank(@AuthenticationPrincipal User user) {
        QuizRankUserData quizRankUserData = QuizRankUserData.of(user.getId(), user.getNickname());
        Long rank = quizRankRedisService.getRank(quizRankUserData);
        int score = quizRankRedisService.getScore(quizRankUserData);
        return ResponseEntity.ok(UserRankResponse.of(rank + 1, quizRankUserData, score));
    }

}
