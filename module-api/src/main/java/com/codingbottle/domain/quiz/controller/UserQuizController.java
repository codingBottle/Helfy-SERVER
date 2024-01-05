package com.codingbottle.domain.quiz.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.UserQuizInfo;
import com.codingbottle.domain.quiz.service.UserQuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "사용자 퀴즈 정보", description = "퀴즈 사용자 관련 API")
@RestController
@RequestMapping("/api/v1/quiz/users")
@RequiredArgsConstructor
public class UserQuizController {
    private final UserQuizService userQuizService;

    @GetMapping("/wrong")
    public ResponseEntity<List<QuizResponse>> getWrongQuizzes(@AuthenticationPrincipal User user) {
        List<QuizResponse> wrongQuizzes = userQuizService.findRandomWrongQuizzesByUser(user).stream()
                .map(QuizResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(wrongQuizzes);
    }

    @GetMapping
    public ResponseEntity<UserQuizInfo> getUserQuizInfo(@AuthenticationPrincipal User user) {
        UserQuizInfo userQuizInfo = userQuizService.getUserQuizInfo(user);
        return ResponseEntity.ok(userQuizInfo);
    }
}
