package com.codingbottle.domain.quiz.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.service.QuizService;
import com.codingbottle.domain.quiz.service.UserQuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "퀴즈", description = "퀴즈 API")
@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final UserQuizService userQuizService;

    @GetMapping
    public ResponseEntity<List<Quiz>> getQuizzes(@AuthenticationPrincipal User user,
                                                 @RequestParam(value = "type") Type type) {
        List<Quiz> quizzes = quizService.findByType(user, type);
        return ResponseEntity.ok(quizzes);
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<String> quizStatusPut(@PathVariable(value = "id") Long id,
                                                @RequestBody @Validated QuizStatusRequest quizStatusRequest,
                                                @AuthenticationPrincipal User user) {
        String userQuizStatus = userQuizService.updateQuizStatus(id, quizStatusRequest, user);
        return ResponseEntity.ok(userQuizStatus);
    }
}
