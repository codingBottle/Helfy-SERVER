package com.codingbottle.domain.quiz.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "퀴즈", description = "퀴즈 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1/quiz")
public interface QuizApi {
    @Operation(summary = "퀴즈 목록 조회", description = "Type에 맞는 퀴즈 목록을 조회합니다. (오늘의 퀴즈를 이미 푼 사용자는 에러 메시지가 반환됩니다.")
    @GetMapping
    List<QuizResponse> getQuizList(
            User user,
            @RequestParam(value = "type") @Parameter(description = "퀴즈 타입을 URL 파라미터로 지정해서 요청하세요. (TODAY, NORMAL)") Type type
    );
}
