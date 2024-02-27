package com.codingbottle.domain.rank.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.rank.model.UserInfoWithRankInfo;
import com.codingbottle.domain.rank.model.UsersRankResponse;
import com.codingbottle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "랭킹", description = "랭킹 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1/rank")
public interface RankApi {
    @Operation(summary = "랭킹 조회", description = "랭킹을 조회합니다.(1~10위)")
    @GetMapping
    UsersRankResponse getRankResponse();

    @Operation(summary = "사용자 랭킹 조회", description = "사용자의 현재 랭킹을 조회합니다. 만약 점수가 0이면 0등이 나타납니다.")
    @GetMapping("/user")
    UserInfoWithRankInfo getUserRank(User user);
}
