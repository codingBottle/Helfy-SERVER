package com.codingbottle.domain.post.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.annotation.Nullable;
import java.util.List;

public record PostRequest(
        @NotBlank
        String content,
        @NotNull
        Long imageId,
        @Size(max = 10)
        @Nullable
        List<String> hashtags
){
}
