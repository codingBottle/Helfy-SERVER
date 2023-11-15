package com.codingbottle.domain.Post.entity;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.common.entity.ObjectTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_num")
    private Long id;

    @Column(name = "post_content")
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @Builder
    public Post(String content, User user, Image image) {
        this.content = content;
        this.user = user;
        this.image = image;
    }

    public void updatePost(String content, Image image) {
        this.content = content;
        this.image = image;
    }
}
