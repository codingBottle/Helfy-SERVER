package com.codingbottle.domain.post.model;

import com.codingbottle.domain.user.entity.User;

public record UserPostResponse(
        String nickname
) {
    public static UserPostResponse from(User user) {
        return new UserPostResponse(user.getNickname());
    }
}
