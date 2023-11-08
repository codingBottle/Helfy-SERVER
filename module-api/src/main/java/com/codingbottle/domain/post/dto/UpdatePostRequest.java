package com.codingbottle.domain.post.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequest (
        @NotBlank
        String content,
        @NotBlank
        Long imageId
){
}
