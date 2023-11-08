package com.codingbottle.domain.post.dto;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.Post.entity.Post;
import jakarta.validation.constraints.NotBlank;

public record AddPostRequest(
        @NotBlank
        String content,
        @NotBlank
        Long imageId
){
    public Post toEntity(User user, Image image) {
        return Post.builder()
                .content(this.content)
                .user(user)
                .image(image)
                .build();
    }
}
