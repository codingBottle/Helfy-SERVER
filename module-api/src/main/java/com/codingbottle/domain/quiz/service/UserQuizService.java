package com.codingbottle.domain.quiz.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.RankRedisService;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.QuizStatus;
import com.codingbottle.domain.quiz.entity.UserQuiz;
import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.quiz.repo.UserQuizQueryRepository;
import com.codingbottle.domain.quiz.repo.UserQuizSimpleJPARepository;
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
    private final RankRedisService rankRedisService;

    public List<Quiz> findRandomWrongQuizzesByUser(User user) {
        List<Quiz> randomWrongQuizzes = userQuizQueryRepository.findRandomWrongQuizzesByUser(user);

        if (randomWrongQuizzes.isEmpty()) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_EXIT_WRONG_ANSWER);
        }
        return randomWrongQuizzes;
    }

    public UserQuiz findByUserAndQuiz(User user, Long id){
        Quiz quiz = quizService.findById(id);
        return userQuizSimpleJPARepository.findByUserAndQuiz(user, quiz).orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.USER_QUIZ_NOT_FOUND, String.format("해당 사용자(%s)의 퀴즈(%s)를 찾을 수 없습니다.", user.getId(), id)));
    }

    @Transactional
    public void createUserQuiz(User user, Long quizId, QuizStatusRequest quizStatusRequest){
        updateScore(user, quizStatusRequest);

        Quiz quiz = quizService.findById(quizId);

        UserQuiz userQuiz = UserQuiz.builder()
                .quiz(quiz)
                .user(user)
                .quizStatus(quizStatusRequest.quizStatus())
                .build();
        userQuizSimpleJPARepository.save(userQuiz);
    }

    @Transactional
    public void updateUserQuiz(User user, Long quizId, QuizStatusRequest quizStatusRequest){
        updateScore(user, quizStatusRequest);

        Quiz quiz = quizService.findById(quizId);

        UserQuiz userQuiz = findByUserAndQuiz(user, quizId);
        userQuiz.updateQuizStatus(quizStatusRequest.quizStatus());
    }

    public Boolean isAlreadyUserQuiz(User user, Long quizId){
        try{
            findByUserAndQuiz(user, quizId);
        }catch (ApplicationErrorException e){
            return false;
        }
        return true;
    }

    private void updateScore(User user, QuizStatusRequest quizStatusRequest) {
        if(quizStatusRequest.quizStatus() == QuizStatus.CORRECT){
            rankRedisService.ZSetAddOrUpdate("user_score", user.getId().toString(), 1.0);
        }
    }
}
