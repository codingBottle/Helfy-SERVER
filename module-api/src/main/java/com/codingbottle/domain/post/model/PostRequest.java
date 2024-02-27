package com.codingbottle.domain.post.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.annotation.Nullable;
import java.util.List;

@Schema(description = "게시글 생성 요청")
public record PostRequest(
        @NotBlank
        @Schema(description = "게시글 내용", example = "홍수가 발생했습니다.")
        String content,
        @NotNull
        @Schema(description = "이미지 ID", example = "1")
        Long imageId,
        @Size(max = 10)
        @Nullable
        @Schema(description = "해시태그 목록", example = "[\"홍수\", \"태풍\"]")
        List<String> hashtags
){
}
