package com.codingbottle.domain.post.model;

import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PostResponse(
        Long id,
        String content,
        List<String> hashtags,
        UserPostResponse user,
        ImageResponse image,
        int likeCount,
        boolean likeStatus,
        LocalDateTime createdTime,
        LocalDateTime modifiedTime
) {
    public static PostResponse of(Post post, Boolean likeStatus) {
        return new PostResponse(post.getId(), post.getContent(), new ArrayList<>(post.getHashtags()), UserPostResponse.from(post.getUser()), ImageResponse.from(post.getImage()), post.getLikesCount(), likeStatus, post.getCreatedTime(), post.getModifiedTime());
    }

    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getContent(), new ArrayList<>(post.getHashtags()), UserPostResponse.from(post.getUser()), ImageResponse.from(post.getImage()), post.getLikes().size(), false, post.getCreatedTime(), post.getModifiedTime());
    }
}
