package com.codingbottle.domain.quiz.controller;

import com.codingbottle.domain.quiz.restapi.QuizApi;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuizController implements QuizApi {
    private final QuizService quizService;

    @Override
    public List<QuizResponse> getQuizList(@AuthenticationPrincipal User user,
                                          Type type) {
        return quizService.findByType(user, type);
    }
}
