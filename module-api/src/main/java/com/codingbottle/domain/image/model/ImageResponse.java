package com.codingbottle.domain.image.model;

import com.codingbottle.domain.image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 응답")
public record ImageResponse(
        @Schema(description = "이미지 ID", example = "1")
        Long id,
        @Schema(description = "이미지 URL", example = "https://hellobf.co.kr/wp-content/uploads/2016/02/smile-puppy-01-1280x720.jpg")
        String imageUrl
) {
    public static ImageResponse from(Image image) {
        return new ImageResponse(image.getId(), image.getImageUrl());
    }
}
