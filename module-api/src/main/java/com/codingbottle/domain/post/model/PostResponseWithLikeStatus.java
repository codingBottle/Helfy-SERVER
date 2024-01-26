package com.codingbottle.domain.post.model;

public record PostResponseWithLikeStatus(
        PostResponse post,
        boolean likeStatus
) {
    public static PostResponseWithLikeStatus of(PostResponse post, boolean likeStatus) {
        return new PostResponseWithLikeStatus(post, likeStatus);
    }
}
