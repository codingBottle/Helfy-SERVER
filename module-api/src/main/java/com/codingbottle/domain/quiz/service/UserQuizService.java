package com.codingbottle.domain.quiz.service;

import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.event.UpdateUserInfoRankCacheEvent;
import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.QuizStatus;
import com.codingbottle.domain.quiz.entity.UserQuiz;
import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.quiz.model.UserQuizInfo;
import com.codingbottle.domain.quiz.repo.UserQuizQueryRepository;
import com.codingbottle.domain.quiz.repo.UserQuizSimpleJPARepository;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import com.codingbottle.redis.domain.quiz.service.TodayQuizRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQuizService {
    private final UserQuizQueryRepository userQuizQueryRepository;
    private final QuizService quizService;
    private final UserQuizSimpleJPARepository userQuizSimpleJPARepository;
    private final QuizRankRedisService quizRankRedisService;
    private final TodayQuizRedisService todayQuizRedisService;

    @EventListener
    public void handleUserUpdate(UpdateUserInfoRankCacheEvent event) {
        int score = quizRankRedisService.removeUserWithScore(UserInfo.of(event.getUser().getId(), event.getUser().getNickname()));
        quizRankRedisService.addScore(UserInfo.of(event.getUser().getId(), event.getUserUpdateInfo().nickname()), score);
    }

    @Transactional
    public String updateQuizStatus(Long quizId, QuizStatusRequest quizStatusRequest, User user) {
        setTodayQuizSolvedIfNotAlreadySolved(quizStatusRequest, user.getId());

        if (quizStatusRequest.quizStatus() == QuizStatus.CORRECT) {
            handleCorrectAnswer(user, quizId, quizStatusRequest);
            return "CORRECT";
        } else {
            handleWrongAnswer(user, quizId, quizStatusRequest);
            return "WRONG";
        }
    }

    private void setTodayQuizSolvedIfNotAlreadySolved(QuizStatusRequest quizStatusRequest, Long userId) {
        if (!todayQuizRedisService.isAlreadySolved(userId) && quizStatusRequest.type().equals(Type.TODAY)) {
            todayQuizRedisService.setSolved(userId);
        }
    }

    private UserQuiz saveUserQuiz(User user, Long quizId, QuizStatusRequest quizStatusRequest){
        Quiz quiz = quizService.findById(quizId);
        UserQuiz userQuiz = UserQuiz.builder()
                .quiz(quiz)
                .user(user)
                .quizStatus(quizStatusRequest.quizStatus())
                .build();
        return userQuizSimpleJPARepository.save(userQuiz);
    }

    private Boolean existsUserQuiz(User user, Long quizId){
        return userQuizSimpleJPARepository.existsByUserIdAndQuizId(user.getId(), quizId);
    }

    private UserQuiz findUserQuiz(Long quizId, User user) {
        return userQuizSimpleJPARepository.findByUserIdAndQuizId(user.getId(), quizId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.USER_QUIZ_NOT_FOUND));
    }

    private void handleCorrectAnswer(User user, Long quizId, QuizStatusRequest quizStatusRequest) {
        UserQuiz userQuiz;
        if (existsUserQuiz(user, quizId)) {
            userQuiz = findUserQuiz(quizId, user);
            validateQuizStatus(userQuiz);
        } else {
            userQuiz = saveUserQuiz(user, quizId, quizStatusRequest);
        }
        userQuiz.updateQuizStatus(quizStatusRequest.quizStatus());
        plusUserQuizScore(user, quizStatusRequest.type());
    }

    private void validateQuizStatus(UserQuiz userQuiz) {
        if (userQuiz.getQuizStatus() != QuizStatus.WRONG) {
            throw new ApplicationErrorException(ApplicationErrorType.ALREADY_SOLVED_QUIZ);
        }
    }

    private void plusUserQuizScore(User user, Type type) {
        quizRankRedisService.updateScore(UserInfo.of(user.getId(), user.getNickname()), type.getScore());
    }

    private void handleWrongAnswer(User user, Long quizId, QuizStatusRequest quizStatusRequest) {
        if(!existsUserQuiz(user, quizId)) {
            saveUserQuiz(user, quizId, quizStatusRequest);
        }
    }

    public UserQuizInfo getUserQuizInfo(User user) {
        int score = quizRankRedisService.getScore(UserInfo.of(user.getId(), user.getNickname()));
        return UserQuizInfo.from(user.getNickname(), score);
    }
}
