package com.codingbottle.domain.quiz.controller;

import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.UserQuizInfo;
import com.codingbottle.domain.quiz.service.UserQuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사용자 퀴즈 정보", description = "퀴즈 사용자 관련 API")
@RestController
@RequestMapping("/api/v1/quiz/users")
@RequiredArgsConstructor
public class UserQuizController {
    private final UserQuizService userQuizService;

    @GetMapping("/wrong")
    public ResponseEntity<List<QuizResponse>> getWrongQuizzes(@AuthenticationPrincipal User user) {
        List<QuizResponse> wrongQuizzes = userQuizService.findRandomWrongQuizzesByUser(user);

        return ResponseEntity.ok(wrongQuizzes);
    }

    @PutMapping("/{id}/result")
    public ResponseEntity<String> quizStatusPut(@PathVariable(value = "id") Long id,
                                                @RequestBody @Validated QuizStatusRequest quizStatusRequest,
                                                @AuthenticationPrincipal User user) {
        String userQuizStatus = userQuizService.updateQuizStatus(id, quizStatusRequest, user);
        return ResponseEntity.ok(userQuizStatus);
    }

    @GetMapping
    public ResponseEntity<UserQuizInfo> getUserQuizInfo(@AuthenticationPrincipal User user) {
        UserQuizInfo userQuizInfo = userQuizService.getUserQuizInfo(user);
        return ResponseEntity.ok(userQuizInfo);
    }
}
