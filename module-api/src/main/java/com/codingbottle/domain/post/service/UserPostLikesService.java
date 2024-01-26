package com.codingbottle.domain.post.service;

import com.codingbottle.domain.post.repo.UserPostLikesRepository;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.redis.domain.post.model.PostCacheData;
import com.codingbottle.redis.domain.post.model.UserLikesCacheData;
import com.codingbottle.redis.domain.post.service.LikesRedisService;
import com.codingbottle.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPostLikesService {
    private final LikesRedisService likesRedisService;
    private final UserPostLikesRepository userPostLikesRepository;

    @Transactional
    public boolean isLikes(User user, Long postId) {
        Boolean likesExists = likesRedisService.isLikesExists(PostCacheData.from(postId), UserLikesCacheData.of(user.getId(), true));
        if (likesExists) {
            return true;
        }

        userPostLikesRepository.findByPostIdAndUserId(postId, user.getId()).ifPresent(userPostLikes -> {
            likesRedisService.setLikesStatus(PostCacheData.from(postId), UserLikesCacheData.of(user.getId(), true));
            userPostLikesRepository.delete(userPostLikes);
        });

        likesRedisService.setLikesStatus(PostCacheData.from(postId), UserLikesCacheData.of(user.getId(), false));
        return false;
    }


    @Transactional
    public boolean toggleLikeStatus(User user, Post post) {
        Boolean likesExists = likesRedisService.isLikesExists(PostCacheData.from(post.getId()), UserLikesCacheData.of(user.getId(), true));
        if (likesExists) {
            likesRedisService.deleteLikes(PostCacheData.from(post.getId()), UserLikesCacheData.of(user.getId(), true));
            userPostLikesRepository.deleteByUserIdAndPostId(user.getId(), post.getId());
            updateOncePost(post);
            return false;
        }

        likesRedisService.setLikesStatus(PostCacheData.from(post.getId()), UserLikesCacheData.of(user.getId(), true));
        likesRedisService.setExpiration(PostCacheData.from(post.getId()));
        updateOncePost(post);
        return true;
    }

    private void updateOncePost(Post post) {
        likesRedisService.setUpdateList("UPDATELIST", PostCacheData.from(post.getId()));
        likesRedisService.setExpiration(PostCacheData.from(post.getId()));
    }
}
