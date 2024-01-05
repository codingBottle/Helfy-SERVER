package com.codingbottle.domain.information.model;

import com.codingbottle.domain.category.entity.Category;
import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.information.entity.Information;

public record InformationResponse(
        Long id,
        Category category,
        String content,
        String news_url,
        String youtube_url,
        ImageResponse image
) {
    public static InformationResponse of(Information information) {
        return new InformationResponse(
                information.getId(),
                information.getCategory(),
                information.getContent(),
                information.getNews(),
                information.getYoutube(),
                ImageResponse.from(information.getImage())
        );
    }
}
