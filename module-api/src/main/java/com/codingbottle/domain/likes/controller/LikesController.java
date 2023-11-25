package com.codingbottle.domain.likes.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{id}/likes")
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PutMapping
    public ResponseEntity<?> likesPut(@PathVariable(value = "id") Long id, @AuthenticationPrincipal User user) {
        if(likesService.likesPut(user, id).isPresent()){
            return (ResponseEntity<?>) ResponseEntity.ok();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Boolean> likeStatus(@PathVariable(value = "id") Long id, @AuthenticationPrincipal User user){
        Boolean likesStatus = likesService.getLikeStatus(user, id);

        return ResponseEntity.ok(likesStatus);
    }
}
