package com.codingbottle.domain.user.model;

import com.codingbottle.domain.rank.model.CacheUser;

public record UpdateUser(
        CacheUser before,
        CacheUser after
) {
    public static UpdateUser of(CacheUser before, CacheUser after) {
        return new UpdateUser(before, after);
    }
}
