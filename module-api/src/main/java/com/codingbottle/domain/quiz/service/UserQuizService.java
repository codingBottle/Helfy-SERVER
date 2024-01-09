package com.codingbottle.domain.quiz.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.service.QuizRankRedisService;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.QuizStatus;
import com.codingbottle.domain.quiz.entity.UserQuiz;
import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.quiz.model.UserQuizInfo;
import com.codingbottle.domain.quiz.repo.UserQuizQueryRepository;
import com.codingbottle.domain.quiz.repo.UserQuizSimpleJPARepository;
import com.codingbottle.domain.rank.model.CacheUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQuizService {
    private final UserQuizQueryRepository userQuizQueryRepository;
    private final QuizService quizService;
    private final UserQuizSimpleJPARepository userQuizSimpleJPARepository;
    private final QuizRankRedisService quizRankRedisService;

    public List<Quiz> findRandomWrongQuizzesByUser(User user) {
        List<Quiz> randomWrongQuizzes = userQuizQueryRepository.findRandomWrongQuizzesByUser(user);

        if (randomWrongQuizzes.isEmpty()) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_EXIT_WRONG_ANSWER);
        }
        return randomWrongQuizzes;
    }

    @Transactional
    public String updateQuizStatus(Long quizId, QuizStatusRequest quizStatusRequest, User user) {
        if(quizStatusRequest.quizStatus() == QuizStatus.CORRECT){
            updateScoreAndQuizStatus(user, quizId, quizStatusRequest);
            return "CORRECT";
        } else {
            saveWrongAnswer(user, quizId, quizStatusRequest);
            return "WRONG";
        }
    }

    private void saveUserQuiz(User user, Long quizId, QuizStatusRequest quizStatusRequest){
        Quiz quiz = quizService.findById(quizId);
        UserQuiz userQuiz = UserQuiz.builder()
                .quiz(quiz)
                .user(user)
                .quizStatus(quizStatusRequest.quizStatus())
                .build();
        userQuizSimpleJPARepository.save(userQuiz);
    }

    private Boolean existsUserQuiz(User user, Long quizId){
        return userQuizSimpleJPARepository.existsByUserIdAndQuizId(user.getId(), quizId);
    }

    private UserQuiz findUserQuiz(Long quizId, User user) {
        return userQuizSimpleJPARepository.findByUserIdAndQuizId(user.getId(), quizId).orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.USER_QUIZ_NOT_FOUND));
    }

    private void updateScoreAndQuizStatus(User user, Long quizId, QuizStatusRequest quizStatusRequest) {
        quizRankRedisService.updateScore(user, 10);

        if(existsUserQuiz(user, quizId)) {
            UserQuiz userQuiz = findUserQuiz(quizId, user);
            userQuiz.updateQuizStatus(quizStatusRequest.quizStatus());
        } else {
            saveUserQuiz(user, quizId, quizStatusRequest);
        }
    }

    private void saveWrongAnswer(User user, Long quizId, QuizStatusRequest quizStatusRequest) {
        if(!existsUserQuiz(user, quizId)) {
            saveUserQuiz(user, quizId, quizStatusRequest);
        }
    }

    public UserQuizInfo getUserQuizInfo(User user) {
        int score = quizRankRedisService.getScore(CacheUser.from(user));
        return UserQuizInfo.from(user.getNickname(), score);
    }
}
