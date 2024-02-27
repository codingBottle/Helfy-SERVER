package com.codingbottle.domain.rank.controller;

import com.codingbottle.domain.rank.model.UserInfoWithRankInfo;
import com.codingbottle.domain.rank.restapi.RankApi;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.quiz.model.RankInfo;
import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
import com.codingbottle.domain.rank.model.UsersRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class RankController implements RankApi {
    private final QuizRankRedisService quizRankRedisService;

    @Override
    public UsersRankResponse getRankResponse() {
        List<UserInfo> usersData = quizRankRedisService.getUsersRank();

        List<UserInfoWithRankInfo> rank = usersData.stream()
                .map(userInfo -> UserInfoWithRankInfo.of(userInfo, quizRankRedisService.getRankInfo(userInfo)))
                .filter(userInfoWithRankInfo -> userInfoWithRankInfo.rankInfo().rank() != 0)
                .collect(Collectors.toList());

        return UsersRankResponse.of(rank);
    }

    @Override
    public UserInfoWithRankInfo getUserRank(@AuthenticationPrincipal User user) {
        UserInfo userInfo = UserInfo.of(user.getId(), user.getNickname());
        RankInfo rankInfo = quizRankRedisService.getRankInfo(userInfo);

        return UserInfoWithRankInfo.of(userInfo, rankInfo);
    }
}
