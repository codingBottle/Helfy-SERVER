package com.codingbottle.fixture;

import com.codingbottle.auth.entity.Role;
import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.QuizStatus;
import com.codingbottle.domain.quiz.entity.QuizType;
import com.codingbottle.domain.quiz.entity.UserQuiz;
import com.codingbottle.domain.region.entity.Region;

import java.util.HashMap;

public class CoreDomainFixture {
    public static final User 유저1 = User.builder()
            .username("유저1")
            .email("helfy@gmail.com")
            .region(Region.SEOUL)
            .role(Role.ROLE_USER)
            .build();

    public static final Quiz 퀴즈1 = Quiz.builder()
            .answer("답")
            .question("질문")
            .choices(new HashMap<>(){
                {
                    put("1", "선택지1");
                    put("2", "선택지2");
                    put("3", "선택지3");
                    put("4", "선택지4");
                }
            })
            .quizType(QuizType.MULTIPLE_CHOICE)
            .image(null)
            .build();

    public static final Quiz 퀴즈2 = Quiz.builder()
            .answer("답")
            .question("질문")
            .choices(new HashMap<>(){
                {
                    put("1", "선택지1");
                    put("2", "선택지2");
                    put("3", "선택지3");
                    put("4", "선택지4");
                }
            })
            .quizType(QuizType.MULTIPLE_CHOICE)
            .image(null)
            .build();

    public static final UserQuiz 유저1_퀴즈1 = UserQuiz.builder()
            .user(유저1)
            .quiz(퀴즈1)
            .quizStatus(QuizStatus.CORRECT)
            .build();

    public static final UserQuiz 유저1_오답1 = UserQuiz.builder()
            .user(유저1)
            .quiz(퀴즈1)
            .quizStatus(QuizStatus.WRONG)
            .build();

    public static final UserQuiz 유저1_오답2 = UserQuiz.builder()
            .user(유저1)
            .quiz(퀴즈2)
            .quizStatus(QuizStatus.WRONG)
            .build();
}
