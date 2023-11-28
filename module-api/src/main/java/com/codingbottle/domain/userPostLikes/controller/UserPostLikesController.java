package com.codingbottle.domain.userPostLikes.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.redis.LikesRedisService;
import com.codingbottle.domain.userPostLikes.service.UserPostLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{id}/likes")
@RequiredArgsConstructor
public class UserPostLikesController {
    private final UserPostLikesService userPostLikesService;
    private final LikesRedisService likesRedisService;

    @PutMapping
    public ResponseEntity<String> likesPut(@PathVariable(value = "id") Long id, @AuthenticationPrincipal User user) {
        userPostLikesService.likesPut(user, id);
        return ResponseEntity.ok("Likes operation completed successfully.");
    }

    @GetMapping
    public ResponseEntity<Boolean> likeStatus(@PathVariable(value = "id") Long id, @AuthenticationPrincipal User user){
        Boolean likesStatus = likesRedisService.getLikeStatus(user.getId(), id);

        return ResponseEntity.ok(likesStatus);
    }
}
