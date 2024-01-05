package com.codingbottle.domain.image.model;

import com.codingbottle.domain.image.entity.Image;
import lombok.Builder;

@Builder
public record ImageResponse(
         Long id,
         String imageUrl
) {
    public static ImageResponse from(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
