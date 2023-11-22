package com.codingbottle.domain.quiz.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.service.UserQuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "오답 퀴즈", description = "오답 퀴즈 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserQuizController {
    private final UserQuizService userQuizService;

    @GetMapping("/wrong-quizzes")
    public ResponseEntity<List<Quiz>> getWrongQuizzes(@AuthenticationPrincipal User user) {
        List<Quiz> wrongQuizzes = userQuizService.findRandomWrongQuizzesByUser(user);

        return ResponseEntity.ok(wrongQuizzes);
    }
}
