package com.codingbottle.domain.post.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.redis.service.LikesRedisService;
import com.codingbottle.domain.post.entity.UserPostLikes;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPostLikesService {
    private final LikesRedisService likesRedisService;

    @Transactional
    public void likesPut(User user, Post post) {
        setLikes(user, post);
        UserPostLikes userPostLikes = UserPostLikes.builder()
                .post(post)
                .user(user)
                .build();

        post.addLikes(userPostLikes);
    }

    @Transactional
    public void cancelLikes(User user, Post post) {
        post.removeLikes(user);
        delete(user, post);
    }

    public Boolean isAlreadyLikes(User user, Post post) {
        return likesRedisService.isAlreadyLikes(user, post);
    }

    public void delete(User user, Post post) {
        likesRedisService.deleteLikes(user, post);
    }

    private void setLikes(User user, Post post) {
        try {
            likesRedisService.setLikes(user, post);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "좋아요 상태를 변경 실패했습니다.");
        }
    }
}
