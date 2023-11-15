package com.codingbottle.domain.post.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.Post.entity.Post;
import com.codingbottle.domain.post.dto.AddPostRequest;
import com.codingbottle.domain.post.dto.PostResponse;
import com.codingbottle.domain.post.dto.UpdatePostRequest;
import com.codingbottle.domain.post.service.PostService;
import com.codingbottle.domain.region.entity.Region;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판", description = "게시판 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Validated AddPostRequest addPostRequest,@AuthenticationPrincipal User user) {
        Post createPost = postService.savePost(addPostRequest,user);
        return ResponseEntity.ok(PostResponse.createInstance(createPost));
    }

    @GetMapping
    public ResponseEntity<PostResponse> findById(@RequestParam(value = "postId") Long postId) {
        Post findPost = postService.findById(postId);
        return ResponseEntity.ok(PostResponse.createInstance(findPost));
    }

    @GetMapping("/list")
    public ResponseEntity<Page> page(Pageable pageable) {
        Page<PostResponse> map = postService.findAll(pageable).map(PostResponse::createInstance);
        return ResponseEntity.ok(map);
    }

    @PatchMapping
    public ResponseEntity<PostResponse> postUpdate(@RequestParam(value = "postId") Long postId, @RequestBody @Validated UpdatePostRequest updatePostRequest,@AuthenticationPrincipal User user) {
        Post updatedPost = postService.updatePost(updatePostRequest,postId,user);
        return ResponseEntity.ok(PostResponse.createInstance(updatedPost));
    }

    @DeleteMapping
    public ResponseEntity<?> postDelete(@RequestParam(value = "postId") Long postId, @AuthenticationPrincipal User user) {
        postService.delete(postId,user);
        return ResponseEntity.noContent().build();
    }
}
