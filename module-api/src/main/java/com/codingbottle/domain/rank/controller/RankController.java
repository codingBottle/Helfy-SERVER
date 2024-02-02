package com.codingbottle.domain.rank.controller;

import com.codingbottle.domain.rank.model.UserInfoWithRankInfo;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.quiz.model.RankInfo;
import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
import com.codingbottle.domain.rank.model.UsersRankResponse;
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
        List<UserInfo> usersData = quizRankRedisService.getUsersRank();

        List<UserInfoWithRankInfo> rank = usersData.stream()
                .map(userInfo -> UserInfoWithRankInfo.of(userInfo, quizRankRedisService.getRankInfo(userInfo)))
                .filter(userInfoWithRankInfo -> userInfoWithRankInfo.rankInfo().rank() != 0)
                .collect(Collectors.toList());

        return ResponseEntity.ok(UsersRankResponse.of(rank));
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoWithRankInfo> getUserRank(@AuthenticationPrincipal User user) {
        UserInfo userInfo = UserInfo.of(user.getId(), user.getNickname());
        RankInfo rankInfo = quizRankRedisService.getRankInfo(userInfo);

        return ResponseEntity.ok(UserInfoWithRankInfo.of(userInfo, rankInfo));
    }
}
