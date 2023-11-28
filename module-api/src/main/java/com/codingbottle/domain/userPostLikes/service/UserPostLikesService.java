package com.codingbottle.domain.userPostLikes.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.LikesRedisService;
import com.codingbottle.domain.userPostLikes.entity.UserPostLikes;
import com.codingbottle.domain.userPostLikes.repo.UserPostLikesRepository;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.service.PostService;
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
        Post findPost = postService.findById(postId);

        if (getLikeStatus(user, postId)) {
            setLikeStatus(user, postId, false);
            UserPostLikes userPostLikes = findByUserIdAndPostId(user.getId(), postId);
            userPostLikesRepository.delete(userPostLikes);
            findPost.subLikeCount();
            return;
        }
        setLikeStatus(user, postId, true);
        UserPostLikes userPostLikes = LikesToEntity(user, findPost);
        userPostLikesRepository.save(userPostLikes);
        findPost.addLikeCount();
    }

    private void setLikeStatus(User user, Long postId, boolean likeStatus) {
        try {
            likesRedisService.setLikeStatus(user.getId(), postId, likeStatus);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "좋아요 상태를 변경 실패했습니다.");
        }
    }

    public Boolean getLikeStatus(User user, Long postId){
        return likesRedisService.getLikeStatus(user.getId(), postId);
    }

    public UserPostLikes findByUserIdAndPostId(Long userId, Long postId) {
        return userPostLikesRepository.findByUserAndPost(userId, postId).orElseThrow(()->new ApplicationErrorException(ApplicationErrorType.LIKES_NOT_FOUND, String.format("해당 게시물(%s)에 대한 사용자(%s)의 좋아요를 찾을 수 없습니다.", postId, userId)));
    }

    private static UserPostLikes LikesToEntity(User user, Post post) {
        return UserPostLikes.builder().post(post).user(user).build();
    }
}
