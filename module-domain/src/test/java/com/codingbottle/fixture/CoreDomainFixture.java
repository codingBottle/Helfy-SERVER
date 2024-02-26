package com.codingbottle.fixture;

import com.codingbottle.domain.user.entity.Role;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.image.entity.Directory;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.QuizStatus;
import com.codingbottle.domain.quiz.entity.QuizType;
import com.codingbottle.domain.quiz.entity.UserQuiz;
import com.codingbottle.domain.region.entity.Region;

import java.util.HashMap;

public class CoreDomainFixture {
    public static final User 유저1 = User.builder()
            .nickname("유저1")
            .firebaseUid("firebaseUid")
            .email("helfy@gmail.com")
            .region(Region.SEOUL)
            .role(Role.ROLE_USER)
            .profileImage("https://d1csu9i9ktup9e.cloudfront.net/default.png")
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

    public static final Image 게시글_이미지1 = Image.builder()
            .id(1L)
            .directory(Directory.POST)
            .imageUrl("https://d1csu9i9ktup9e.cloudfront.net/default.png")
            .convertImageName("default.png")
            .build();

    public static final Image 게시글_이미지2 = Image.builder()
            .id(2L)
            .directory(Directory.POST)
            .imageUrl("https://d1csu9i9ktup9e.cloudfront.net/default.png")
            .convertImageName("default.png")
            .build();

    public static final Post 게시글1 = Post.builder()
            .content("게시글")
            .image(게시글_이미지1)
            .user(유저1)
            .build();

    public static final Post 게시글2 = Post.builder()
            .content("게시글")
            .image(게시글_이미지2)
            .user(유저1)
            .build();
}
