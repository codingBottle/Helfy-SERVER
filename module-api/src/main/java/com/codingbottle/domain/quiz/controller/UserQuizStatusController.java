package com.codingbottle.domain.quiz.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.quiz.service.UserQuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "퀴즈 정답 유무", description = "퀴즈 정답 유무 API")
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class UserQuizStatusController {
    private final UserQuizService userQuizService;

    @PutMapping("/{id}")
    public ResponseEntity<String> quizStatusPut(@PathVariable(value = "id") Long id,
                                                @RequestBody @Validated QuizStatusRequest quizStatusRequest,
                                                @AuthenticationPrincipal User user){
        if (userQuizService.isAlreadyUserQuiz(user, id)) {
            userQuizService.updateUserQuiz(user, id, quizStatusRequest);
        }
        userQuizService.createUserQuiz(user, id, quizStatusRequest);

        return ResponseEntity.ok("QuizStatus update completed successfully.");
    }
}
