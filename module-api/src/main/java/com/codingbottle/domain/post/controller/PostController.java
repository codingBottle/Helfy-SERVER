package com.codingbottle.domain.post.controller;

import com.codingbottle.auth.entity.User;
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
import java.util.stream.Collectors;

@Tag(name = "게시판", description = "게시판 API")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserPostLikesService userPostLikesService;

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
        List<Post> posts = postService.findAll(pageable);

        List<PostResponse> postResponses = posts.stream()
                .map(post -> PostResponse.of(post, userPostLikesService.isAlreadyLikes(user, post.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingUtil.toSliceImpl(postResponses, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable(value = "id") Long id,
                                               @RequestBody @Validated PostRequest postRequest,
                                               @AuthenticationPrincipal User user) {
        Post post = postService.update(postRequest, id, user);
        Boolean likeStatus = userPostLikesService.isAlreadyLikes(user, id);
        return ResponseEntity.ok(PostResponse.of(post, likeStatus));
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
        boolean isLiked = userPostLikesService.toggleLikeStatus(user, postId);
        return ResponseEntity.ok().body(isLiked ? "Liked" : "Unliked");
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchByKeyword(@RequestParam(value = "keyword") String keyword) {
        List<PostResponse> posts = postService.searchByKeyword(keyword).stream()
                .map(PostResponse::from)
                .toList();
        return ResponseEntity.ok(posts);
    }
}
