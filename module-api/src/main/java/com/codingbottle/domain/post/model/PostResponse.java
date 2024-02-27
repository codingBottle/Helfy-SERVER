package com.codingbottle.domain.post.model;

import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "게시글 정보")
public record PostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,
        @Schema(description = "게시글 내용", example = "안녕하세요")
        String content,
        @Schema(description = "해시태그 목록", example = "[\"#안녕\", \"#하이\"]")
        List<String> hashtags,
        @Schema(description = "게시글 작성자 정보", implementation = UserPostResponse.class)
        UserPostResponse user,
        @Schema(description = "게시글 이미지 정보", implementation = ImageResponse.class)
        ImageResponse image,
        @Schema(description = "좋아요 수", example = "10")
        int likeCount,
        @Schema(description = "게시글 생성 시간", example = "2021-07-01T00:00:00")
        LocalDateTime createdTime,
        @Schema(description = "게시글 수정 시간", example = "2021-07-01T00:00:00")
        LocalDateTime modifiedTime
) {
    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getContent(), new ArrayList<>(post.getHashtags()), UserPostResponse.from(post.getUser()), ImageResponse.from(post.getImage()), post.getLikesCount(), post.getCreatedTime(), post.getModifiedTime());
    }
}
