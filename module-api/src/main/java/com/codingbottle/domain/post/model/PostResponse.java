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
    public static PostResponse from(Post post, Boolean likeStatus) {
        return new PostResponse(post.getId(), post.getContent(), post.getUser().getFirebaseUid(),post.getImage(), post.getLikeCount(), likeStatus, post.getCreatedTime(), post.getModifiedTime()
        );
    }

    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getContent(), post.getUser().getFirebaseUid(),post.getImage(), post.getLikeCount(), false, post.getCreatedTime(), post.getModifiedTime()
        );
    }
}
