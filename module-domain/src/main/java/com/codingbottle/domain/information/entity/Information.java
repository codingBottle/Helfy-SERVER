package com.codingbottle.domain.information.entity;

import com.codingbottle.common.entity.BaseEntity;
import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Information extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "news_url", nullable = false, columnDefinition = "VARCHAR(2048)")
    private String news;

    @Column(name = "youtube_url", nullable = false, columnDefinition = "VARCHAR(2048)")
    private String youtube;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Builder
    public Information(String content, String news, String youtube, Category category, Image image) {
        this.content = content;
        this.news = news;
        this.youtube = youtube;
        this.category = category;
        this.image = image;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Information information)) return false;

        if (id != null && information.getId() != null) {
            return Objects.equals(id, information.getId());
        }

        return Objects.equals(content, information.getContent()) &&
                Objects.equals(news, information.getNews()) &&
                Objects.equals(youtube, information.getYoutube()) &&
                Objects.equals(category, information.getCategory()) &&
                Objects.equals(image, information.getImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, news, youtube, category, image);
    }
}
