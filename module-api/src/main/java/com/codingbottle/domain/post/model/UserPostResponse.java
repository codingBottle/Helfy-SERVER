package com.codingbottle.domain.post.model;

import com.codingbottle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 게시글 응답")
public record UserPostResponse(
        @Schema(description = "닉네임", example = "홍길동")
        String nickname
) {
    public static UserPostResponse from(User user) {
        return new UserPostResponse(user.getNickname());
    }
}
