package com.codingbottle.domain.quiz.controller;

import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.quiz.restapi.UserQuizApi;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.UserQuizInfo;
import com.codingbottle.domain.quiz.service.UserQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserQuizController implements UserQuizApi {
    private final UserQuizService userQuizService;

    @Override
    public String quizStatusPut(Long id,
                                QuizStatusRequest quizStatusRequest,
                                @AuthenticationPrincipal User user) {
        return userQuizService.updateQuizStatus(id, quizStatusRequest, user);
    }

    @Override
    public UserQuizInfo getUserQuizInfo(@AuthenticationPrincipal User user) {
        return userQuizService.getUserQuizInfo(user);
    }
}
