package com.codingbottle.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "image")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "directory", nullable = false)
    private Directory directory;

    @Column(name = "convert_image_name", nullable = false)
    private String convertImageName;


    public Image(String imageUrl, Directory directory, String convertImageName) {
        this.imageUrl = imageUrl;
        this.directory = directory;
        this.convertImageName = convertImageName;
    }
}