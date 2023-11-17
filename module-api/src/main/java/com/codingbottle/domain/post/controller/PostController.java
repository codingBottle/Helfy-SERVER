package com.codingbottle.domain.post.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.Post.entity.Post;
import com.codingbottle.domain.post.dto.PostRequest;
import com.codingbottle.domain.post.dto.PostResponse;
import com.codingbottle.domain.post.service.PostService;
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

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody @Validated PostRequest postRequest,
                                               @AuthenticationPrincipal User user) {
        Post post = postService.save(postRequest, user);
        return ResponseEntity.ok(PostResponse.createInstance(post));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable(value = "id") Long id) {
        Post findPost = postService.findById(id);
        return ResponseEntity.ok(PostResponse.createInstance(findPost));
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> page(@PageableDefault Pageable pageable) {
        Page<PostResponse> posts = postService.findAll(pageable).map(PostResponse::createInstance);
        return ResponseEntity.ok(posts);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable(value = "id") Long id,
                                               @RequestBody @Validated PostRequest postRequest,
                                               @AuthenticationPrincipal User user) {
        Post post = postService.update(postRequest, id, user);
        return ResponseEntity.ok(PostResponse.createInstance(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id,
                                    @AuthenticationPrincipal User user) {
        postService.delete(id,user);
        return ResponseEntity.noContent().build();
    }
}
