package com.codingbottle.domain.image.entity;

import com.codingbottle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "directory", nullable = false)
    @Enumerated(EnumType.STRING)
    private Directory directory;

    @Column(name = "convert_image_name", nullable = false)
    private String convertImageName;

    public Image(String imageUrl, Directory directory, String convertImageName) {
        this.imageUrl = imageUrl;
        this.directory = directory;
        this.convertImageName = convertImageName;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", directory=" + directory +
                ", convertImageName='" + convertImageName + '\'' +
                '}';
    }
}