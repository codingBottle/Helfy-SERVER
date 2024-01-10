package com.codingbottle.domain.rank.model;

import java.util.List;

public record UsersRankResponse(
        List<UserRankResponse> userRankResponses
) {
    public static UsersRankResponse of(List<UserRankResponse> userRankResponses) {
        return new UsersRankResponse(userRankResponses);
    }
}
