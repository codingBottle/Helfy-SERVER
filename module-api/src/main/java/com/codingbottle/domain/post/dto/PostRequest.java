package com.codingbottle.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
        @NotBlank
        String content,
        @NotNull
        Long imageId
){
}
