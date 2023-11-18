package com.codingbottle.domain.image.model;

import com.codingbottle.domain.image.entity.Image;
import lombok.Builder;

@Builder
public record ImageResponse(
         Long imageId,
         String imageUrl
) {
    public static ImageResponse of(Image image) {
        return ImageResponse.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
