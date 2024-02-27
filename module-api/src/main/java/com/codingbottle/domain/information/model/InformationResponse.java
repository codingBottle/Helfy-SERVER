package com.codingbottle.domain.information.model;

import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.information.entity.Information;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "정보 응답")
public record InformationResponse(
        @Schema(description = "정보 ID", example = "1")
        Long id,
        @Schema(description = "카테고리", example = "홍수")
        String category,
        @Schema(description = "내용", example = "홍수 발생시 행동요령")
        String content,
        @Schema(description = "뉴스 URL", example = "https://www.naver.com/news/1")
        String news_url,
        @Schema(description = "유튜브 URL", example = "https://www.youtube.com/watch?v=1")
        String youtube_url,
        @Schema(description = "이미지 응답 데이터", implementation = ImageResponse.class)
        ImageResponse image
) {
    public static InformationResponse from(Information information) {
        return new InformationResponse(
                information.getId(),
                information.getCategory().getDetail(),
                information.getContent(),
                information.getNews(),
                information.getYoutube(),
                ImageResponse.from(information.getImage())
        );
    }
}
