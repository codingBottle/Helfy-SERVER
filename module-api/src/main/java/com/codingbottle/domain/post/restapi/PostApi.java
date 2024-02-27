package com.codingbottle.domain.post.restapi;

import com.codingbottle.common.annotation.CustomPageableAsQueryParam;
import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.common.model.DefaultResponse;
import com.codingbottle.domain.post.model.PostRequest;
import com.codingbottle.domain.post.model.PostResponseWithLikeStatus;
import com.codingbottle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시판", description = "게시판 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1/posts")
public interface PostApi {
    @Operation(summary = "게시글 등록", description = "게시글을 등록합니다.")
    @PostMapping
    PostResponseWithLikeStatus create(
            @RequestBody @Validated PostRequest postRequest,
            User user
    );

    @Operation(summary = "게시글 모두 조회", description = "게시글을 모두 조회합니다.")
    @GetMapping
    @CustomPageableAsQueryParam
    SliceImpl<PostResponseWithLikeStatus> findAll(
            @PageableDefault Pageable pageable,
            User user
    );

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PatchMapping("/{id}")
    PostResponseWithLikeStatus update(
            @PathVariable(value = "id") @Parameter(description = "게시물 ID", example = "1") Long id,
            @RequestBody @Validated PostRequest postRequest,
            User user
    );

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    DefaultResponse delete(
            @PathVariable(value = "id") @Parameter(description = "게시물 ID", example = "1") Long id,
            User user
    );

    @Operation(summary = "게시글 좋아요 요청, 취소 요청", description = "게시글에 이미 좋아요가 되어있으면 취소하고, 좋아요가 되어있지 않으면 좋아요를 합니다. 좋아요시는 true, 취소시는 false를 반환합니다.")
    @PutMapping("/{id}/likes")
    boolean toggleLikeStatus(
            @PathVariable(value = "id") @Parameter(description = "게시글 ID", example = "1") Long postId,
            User user
    );

    @Operation(summary = "게시글 검색", description = "게시글을 검색합니다.")
    @GetMapping("/search")
    List<PostResponseWithLikeStatus> searchByKeyword(
            @RequestParam(value = "keyword") @Parameter(description = "검색어", example = "검색어") String keyword,
            User user
    );
}
