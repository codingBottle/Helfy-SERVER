package com.codingbottle.domain.quiz.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.repo.UserQuizQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQuizService {
    private final UserQuizQueryRepository userQuizQueryRepository;

    public List<Quiz> findRandomWrongQuizzesByUser(User user) {
        List<Quiz> randomWrongQuizzes = userQuizQueryRepository.findRandomWrongQuizzesByUser(user);

        if (randomWrongQuizzes.isEmpty()) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_EXIT_WRONG_ANSWER);
        }
        return randomWrongQuizzes;
    }
}
