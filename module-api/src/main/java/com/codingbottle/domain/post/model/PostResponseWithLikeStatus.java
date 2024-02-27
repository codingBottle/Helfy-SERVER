package com.codingbottle.domain.post.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 응답 데이터 모델")
public record PostResponseWithLikeStatus(
        @Schema(description = "게시글 정보", implementation = PostResponse.class)
        PostResponse post,
        @Schema(description = "좋아요 상태 유무", example = "true")
        boolean likeStatus
) {
    public static PostResponseWithLikeStatus of(PostResponse post, boolean likeStatus) {
        return new PostResponseWithLikeStatus(post, likeStatus);
    }
}
