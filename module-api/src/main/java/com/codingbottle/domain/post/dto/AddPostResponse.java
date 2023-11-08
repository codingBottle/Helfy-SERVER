package com.codingbottle.domain.post.dto;

import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.Post.entity.Post;

import java.time.LocalDateTime;

public record AddPostResponse (
        Long postId,
        String content,
        String username,
        Image image,
        LocalDateTime createTime,
        LocalDateTime updateTime
){
    public static AddPostResponse createInstance(Post post) {
        return new AddPostResponse(post.getId(), post.getContent(), post.getUser().getName(),post.getImage(), post.getCreateTime(), post.getUpdateTime());
    }
}
