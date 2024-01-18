package com.codingbottle.domain.quiz.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "퀴즈", description = "퀴즈 API")
@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping
    public ResponseEntity<List<QuizResponse>> getQuizzes(@AuthenticationPrincipal User user,
                                                 @RequestParam(value = "type") Type type) {
        List<QuizResponse> quiz = quizService.findByType(user, type);

        return ResponseEntity.ok(quiz);
    }
}
