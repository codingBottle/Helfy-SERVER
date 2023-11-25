package com.codingbottle.domain.likes.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.RedisService;
import com.codingbottle.domain.likes.entity.Likes;
import com.codingbottle.domain.likes.repo.LikesRepository;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikesService {
    private final LikesRepository likesRepository;
    private final PostService postService;
    private final RedisService redisService;

    @Transactional
    public Optional<Boolean> likesPut(User user, Long postId) {
        Post findPost = postService.findById(postId);

        if(checkLikesAlready(user.getId(), postId))
        {
            Likes likes = findByUserIdAndPostId(user.getId(), postId);
            likesRepository.delete(likes);
            findPost.subLikeCount();
            try {
                redisService.setLikeStatus(user.getId(), postId, false);
            } catch (JsonProcessingException e) {
                throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "좋아요 상태를 변경 실패했습니다.");
            }
            return Optional.empty();
        }

        Likes likes = LikesToEntity(user, findPost);
        likesRepository.save(likes);
        findPost.addLikeCount();
        try {
            redisService.setLikeStatus(user.getId(), postId, true);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "좋아요 상태를 변경 실패했습니다.");
        }
        return Optional.of(true);
    }

    public Boolean getLikeStatus(User user, Long postId){
        try {
            return redisService.getLikeStatus(user.getId(), postId);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "좋아요 상태를 조회 실패했습니다.");
        }
    }

    public Likes findByUserIdAndPostId(Long userId, Long postId) {
        return likesRepository.findByUserIdAndPostId(userId, postId).orElseThrow(()->new ApplicationErrorException(ApplicationErrorType.LIKES_NOT_FOUND, String.format("해당 게시물(%s)에 대한 사용자(%s)의 좋아요를 찾을 수 없습니다.", postId, userId)));
    }

    private Boolean checkLikesAlready(Long userId, Long postId) {
        return likesRepository.findByUserIdAndPostId(userId, postId).isPresent();
    }

    private static Likes LikesToEntity(User user, Post post) {
        return Likes.builder().post(post).user(user).build();
    }
}
