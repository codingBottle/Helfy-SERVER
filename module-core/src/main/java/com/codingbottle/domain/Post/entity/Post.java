package com.codingbottle.domain.Post.entity;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.objectTIme.entity.ObjectTime;
import com.codingbottle.domain.post.dto.UpdatePostRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends ObjectTime {
    @Id
    @GeneratedValue
    @Column(name = "post_num")
    private Long id;

    @Column(name = "post_content")
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "post_image")
    private Image image;

    @Builder
    public Post(String content, User user, Image image) {
        this.content = content;
        this.user = user;
        this.image = image;
    }


    public void updatePost(UpdatePostRequest updatePostRequest, Image image) {

        this.content = updatePostRequest.getContent();
        this.image = image;
    }
}
