package com.codingbottle.domain.quiz.service;

import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.repo.QuizQueryRepository;
import com.codingbottle.domain.quiz.repo.QuizSimpleJPARepository;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {
    private final QuizQueryRepository quizRepository;
    private final QuizSimpleJPARepository quizSimpleJPARepository;

    public List<QuizResponse> findByType(User user, Type type) {
        return type.getQuizzes(user, quizRepository).stream()
                .map(QuizResponse::from)
                .collect(Collectors.toList());
    }

    public Quiz findById(Long id){
        return quizSimpleJPARepository.findById(id).orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.QUIZ_NOT_FOUND, String.format("해당 퀴즈(%s)를 찾을 수 없습니다.", id)));
    }
}
