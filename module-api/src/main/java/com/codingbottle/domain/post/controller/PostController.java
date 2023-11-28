package com.codingbottle.domain.post.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.model.PostRequest;
import com.codingbottle.domain.post.model.PostResponse;
import com.codingbottle.domain.post.service.PostService;
import com.codingbottle.domain.userPostLikes.service.UserPostLikesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판", description = "게시판 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserPostLikesService userPostLikesService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody @Validated PostRequest postRequest,
                                               @AuthenticationPrincipal User user) {
        Post post = postService.save(postRequest, user);
        Boolean likeStatus = userPostLikesService.getLikeStatus(user, post.getId());
        return ResponseEntity.ok(PostResponse.createInstance(post, likeStatus));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable(value = "id") Long id, @AuthenticationPrincipal User user) {
        Post findPost = postService.findById(id);
        Boolean likeStatus = userPostLikesService.getLikeStatus(user, id);
        return ResponseEntity.ok(PostResponse.createInstance(findPost, likeStatus));
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> page(@PageableDefault Pageable pageable, @AuthenticationPrincipal User user) {
        Page<PostResponse> posts = postService.findAll(pageable)
                .map(post -> PostResponse.createInstance(post, userPostLikesService.getLikeStatus(user, post.getId())));
        return ResponseEntity.ok(posts);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable(value = "id") Long id,
                                               @RequestBody @Validated PostRequest postRequest,
                                               @AuthenticationPrincipal User user) {
        Post post = postService.update(postRequest, id, user);
        Boolean likeStatus = userPostLikesService.getLikeStatus(user, id);
        return ResponseEntity.ok(PostResponse.createInstance(post, likeStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id,
                                    @AuthenticationPrincipal User user) {
        postService.delete(id,user);
        return ResponseEntity.noContent().build();
    }
}
