package com.codingbottle.domain.post.model;

import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String content,
        List<String> hashtags,
        String writerNickname,
        ImageResponse image,
        int likeCount,
        boolean likeStatus,
        LocalDateTime createdTime,
        LocalDateTime modifiedTime
) {
    public static PostResponse of(Post post, Boolean likeStatus) {
        return new PostResponse(post.getId(), post.getContent(), post.getHashtags(), post.getUser().getNickname(), ImageResponse.from(post.getImage()), post.getLikes().size(), likeStatus, post.getCreatedTime(), post.getModifiedTime());
    }

    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getContent(), post.getHashtags(), post.getUser().getNickname(), ImageResponse.from(post.getImage()), post.getLikes().size(), false, post.getCreatedTime(), post.getModifiedTime());
    }
}
