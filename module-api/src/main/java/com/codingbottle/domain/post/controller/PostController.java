package com.codingbottle.domain.post.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.common.annotation.CustomPageableAsQueryParam;
import com.codingbottle.common.util.PagingUtil;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.model.PostRequest;
import com.codingbottle.domain.post.model.PostResponse;
import com.codingbottle.domain.post.service.PostService;
import com.codingbottle.domain.post.service.UserPostLikesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시판", description = "게시판 API")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody @Validated PostRequest postRequest,
                                               @AuthenticationPrincipal User user) {
        Post post = postService.save(postRequest, user);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @GetMapping
    @CustomPageableAsQueryParam
    public ResponseEntity<SliceImpl<PostResponse>> findAll(@PageableDefault Pageable pageable,
                                                           @AuthenticationPrincipal User user) {
        List<PostResponse> posts = postService.findAll(pageable, user);

        return ResponseEntity.ok(PagingUtil.toSliceImpl(posts, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable(value = "id") Long id,
                                               @RequestBody @Validated PostRequest postRequest,
                                               @AuthenticationPrincipal User user) {
        PostResponse post = postService.update(postRequest, id, user);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id,
                                    @AuthenticationPrincipal User user) {
        postService.delete(id,user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/likes")
    public ResponseEntity<String> toggleLikeStatus(@PathVariable(value = "id") Long postId,
                                                   @AuthenticationPrincipal User user) {
        boolean isLiked = postService.toggleLikeStatus(user, postId);

        return ResponseEntity.ok().body(isLiked ? "Liked" : "Unliked");
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchByKeyword(@RequestParam(value = "keyword") String keyword,
                                                              @AuthenticationPrincipal User user) {
        List<PostResponse> posts = postService.searchByKeyword(keyword, user);
        return ResponseEntity.ok(posts);
    }
}
