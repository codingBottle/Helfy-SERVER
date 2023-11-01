package com.codingbottle.domain.post.dto;

import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.Post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long PostId;
    private String content;
    private String username;
    private Image image;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static PostResponse createInstance(Post post) {
        return new PostResponse(post.getId(), post.getContent(), post.getUser().getUsername(),post.getImage(), post.getCreateTime(), post.getUpdateTime()
        );
    }
}
