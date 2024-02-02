package com.codingbottle.domain.quiz.service;

import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.event.UpdateUserInfoRedisEvent;
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
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQuizService {
    private final UserQuizQueryRepository userQuizQueryRepository;
    private final QuizService quizService;
    private final UserQuizSimpleJPARepository userQuizSimpleJPARepository;
    private final QuizRankRedisService quizRankRedisService;

    @EventListener
    public void handleUserUpdate(UpdateUserInfoRedisEvent event) {
        int score = quizRankRedisService.removeUserWithScore(UserInfo.of(event.getUser().getId(), event.getUser().getNickname()));
        quizRankRedisService.addScore(UserInfo.of(event.getUser().getId(), event.getUserUpdateInfo().nickname()), score);
    }

    public List<QuizResponse> findRandomWrongQuizzesByUser(User user) {
        List<Quiz> randomWrongQuizzes = userQuizQueryRepository.findRandomWrongQuizzesByUser(user);

        if (randomWrongQuizzes.isEmpty()) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_EXIT_WRONG_ANSWER);
        }
        return randomWrongQuizzes.stream()
                .map(QuizResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public String updateQuizStatus(Long quizId, QuizStatusRequest quizStatusRequest, User user) {
        if (quizStatusRequest.quizStatus() == QuizStatus.CORRECT) {
            handleCorrectAnswer(user, quizId, quizStatusRequest);
            return "CORRECT";
        } else {
            handleWrongAnswer(user, quizId, quizStatusRequest);
            return "WRONG";
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
        plusUserQuizScore(user);
    }

    private void validateQuizStatus(UserQuiz userQuiz) {
        if (userQuiz.getQuizStatus() != QuizStatus.WRONG) {
            throw new ApplicationErrorException(ApplicationErrorType.ALREADY_SOLVED_QUIZ);
        }
    }

    private void plusUserQuizScore(User user) {
        quizRankRedisService.updateScore(UserInfo.of(user.getId(), user.getNickname()), 10);
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
