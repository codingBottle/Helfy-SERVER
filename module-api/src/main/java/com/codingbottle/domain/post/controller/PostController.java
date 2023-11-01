package com.codingbottle.domain.post.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.Post.entity.Post;
import com.codingbottle.domain.post.dto.AddPostRequest;
import com.codingbottle.domain.post.dto.AddPostResponse;
import com.codingbottle.domain.post.dto.PostResponse;
import com.codingbottle.domain.post.dto.UpdatePostRequest;
import com.codingbottle.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public Object createPost(@RequestBody @Validated AddPostRequest addPostRequest,@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(AddPostResponse.createInstance(postService.savePost(addPostRequest,user)), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public PostResponse findByIdLeftJoin(@PathVariable Long postId) {
        return PostResponse.createInstance(postService.findById(postId));
    }

    @GetMapping
    public Page<Post> findByIdPage(Pageable pageable) {
        return postService.findAll(pageable);
    }

    @PatchMapping("/{postId}")
    public Object postUpdate(@PathVariable(name = "postId") Long postId, @RequestBody @Validated UpdatePostRequest updatePostRequest,@AuthenticationPrincipal User user) {
        postService.updatePost(updatePostRequest,postId,user);
        return PostResponse.createInstance(postService.findById(postId));
    }

    @DeleteMapping("/{postId}")
    public Object postDelete(@PathVariable(name = "postId") Long postId, @AuthenticationPrincipal User user) {
        postService.delete(postId,user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
