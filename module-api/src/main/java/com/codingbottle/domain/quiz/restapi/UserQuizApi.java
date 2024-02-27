package com.codingbottle.domain.quiz.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.quiz.model.UserQuizInfo;
import com.codingbottle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사용자 퀴즈 정보", description = "퀴즈 사용자 관련 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1/quiz/users")
public interface UserQuizApi {
    @Operation(summary = "오답 퀴즈 조회", description = "사용자의 오답 퀴즈를 조회합니다.")
    @GetMapping("/wrong")
    List<QuizResponse> getWrongQuizzes(
            User user
    );

    @Operation(summary = "퀴즈 결과 저장 요청", description = "사용자의 퀴즈 결과를 저장합니다.")
    @PutMapping("/{id}/result")
    String quizStatusPut(
            @PathVariable(value = "id") @Parameter(description = " 퀴즈 ID", example = "1") Long id,
            @RequestBody @Validated QuizStatusRequest quizStatusRequest,
            User user
    );

    @Operation(summary = "사용자 퀴즈 점수 조회", description = "사용자의 퀴즈 점수를 조회합니다.")
    @GetMapping
    UserQuizInfo getUserQuizInfo(
            User user
    );
}
