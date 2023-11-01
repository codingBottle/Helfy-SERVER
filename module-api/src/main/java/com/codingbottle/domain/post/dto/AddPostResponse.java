package com.codingbottle.domain.post.dto;

import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.Post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddPostResponse {
    private Long PostId;
    private String content;
    private String username;
    private Image image;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static AddPostResponse createInstance(Post post) {
        return new AddPostResponse(post.getId(), post.getContent(), post.getUser().getName(),post.getImage(), post.getCreateTime(), post.getUpdateTime());
    }


}
