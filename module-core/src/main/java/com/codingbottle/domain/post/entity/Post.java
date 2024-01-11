package com.codingbottle.domain.post.entity;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.entity.BaseEntity;
import com.codingbottle.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Lob
    @Column(name = "post_content")
    private String content;

    @ElementCollection
    @CollectionTable(name = "post_hashtags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "hashtag")
    private List<String> hashtags = new ArrayList<>(5);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPostLikes> likes = new ArrayList<>();

    @Builder
    public Post(String content, User user, Image image) {
        this.content = content;
        this.user = user;
        this.image = image;
    }

    public Post update(String content, Image image, List<String> hashtags) {
        this.content = content;
        this.image = image;
        this.hashtags = hashtags;
        return this;
    }

    public Post update(String content, List<String> hashtags) {
        this.content = content;
        this.hashtags = hashtags;
        return this;
    }

    public void addLikes(UserPostLikes userPostLikes) {
        this.likes.add(userPostLikes);
    }

    public void removeLikes(User user) {
        this.likes.removeIf(likes -> likes.getUser().equals(user));
    }

    public void addHashtags(List<String> hashtags) {
        this.hashtags.addAll(hashtags);
    }
}
