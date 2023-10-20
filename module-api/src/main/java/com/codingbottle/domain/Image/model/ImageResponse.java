package com.codingbottle.domain.Image.model;

import com.codingbottle.domain.Image.entity.Image;
import lombok.Builder;
import lombok.Getter;

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
