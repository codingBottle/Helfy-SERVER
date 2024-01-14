package com.codingbottle.domain.user.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.model.UserNicknameRequest;
import com.codingbottle.domain.user.model.UserResponse;
import com.codingbottle.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 정보", description = "사용자 정보 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/user/nickname")
    public ResponseEntity<UserResponse> updateNickname(@AuthenticationPrincipal User user,
                                                       @RequestBody UserNicknameRequest nickname) {
        User updateUser = userService.updateNickname(nickname, user);

        return ResponseEntity.ok(UserResponse.of(updateUser));
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserResponse.of(user));
    }
}
