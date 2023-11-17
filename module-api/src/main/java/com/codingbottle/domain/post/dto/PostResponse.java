package com.codingbottle.domain.post.dto;

import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.Post.entity.Post;

import java.time.LocalDateTime;

public record PostResponse (
        Long id,
        String content,
        String username,
        Image image,
        LocalDateTime createdTime,
        LocalDateTime modifiedTime
){
    public static PostResponse createInstance(Post post) {
        return new PostResponse(post.getId(), post.getContent(), post.getUser().getUsername(),post.getImage(), post.getCreatedTime(), post.getModifiedTime()
        );
    }
}
