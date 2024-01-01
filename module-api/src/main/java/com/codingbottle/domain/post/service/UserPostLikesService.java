package com.codingbottle.domain.post.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.LikesRedisService;
import com.codingbottle.domain.post.entity.UserPostLikes;
import com.codingbottle.domain.post.repo.UserPostLikesRepository;
import com.codingbottle.domain.post.entity.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPostLikesService {
    private final UserPostLikesRepository userPostLikesRepository;
    private final PostService postService;
    private final LikesRedisService likesRedisService;

    @Transactional
    public void likesPut(User user, Long postId) {
        Post post = postService.findById(postId);

        setLikes(user, postId);
        UserPostLikes userPostLikes = UserPostLikes.builder()
                .post(post)
                .user(user)
                .build();

        post.addLikes(userPostLikes);
    }

    @Transactional
    public void cancelLikes(User user, Long postId) {
        Post post = postService.findById(postId);

        post.removeLikes(user);
        delete(user.getId(), postId);
    }

    public Boolean isAlreadyLikes(User user, Long postId) {
        return likesRedisService.isAlreadyLikes(user.getId(), postId);
    }

    @Transactional
    public boolean toggleLikeStatus(User user, Long postId) {
        if(isAlreadyLikes(user, postId)){
            cancelLikes(user, postId);
            return false;
        }
        likesPut(user, postId);
        return true;
    }


    public void delete(Long userId, Long postId) {
        likesRedisService.deleteLikes(userId, postId);
    }

    private void setLikes(User user, Long postId) {
        try {
            likesRedisService.setLikes(user.getId(), postId);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "좋아요 상태를 변경 실패했습니다.");
        }
    }
}
