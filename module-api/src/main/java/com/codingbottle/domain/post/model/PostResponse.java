package com.codingbottle.domain.post.model;

import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostResponse (
        Long id,
        String content,
        String username,
        Image image,
        int likeCount,
        boolean likeStatus,
        LocalDateTime createdTime,
        LocalDateTime modifiedTime
){
    public static PostResponse createInstance(Post post, Boolean likeStatus) {
        return new PostResponse(post.getId(), post.getContent(), post.getUser().getUsername(),post.getImage(), post.getLikeCount(), likeStatus, post.getCreatedTime(), post.getModifiedTime()
        );
    }
}
