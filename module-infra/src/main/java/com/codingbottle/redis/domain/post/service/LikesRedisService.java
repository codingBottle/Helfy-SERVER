package com.codingbottle.redis.domain.post.service;

import com.codingbottle.redis.domain.post.model.PostCacheData;
import com.codingbottle.redis.domain.post.model.UserLikesCacheData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LikesRedisService {
    private final RedisTemplate<Object, Object> likesRedisTemplate;

    public void setLikesStatus(PostCacheData post, UserLikesCacheData userPostLikeStatus) {
        addSetMember(post, userPostLikeStatus);
    }

    public void setUpdateList(String update, PostCacheData post) {
        addSetMember(update, post);
    }

    public void deleteLikes(PostCacheData post, UserLikesCacheData userPostLikeStatus) {
        removeSetMember(post, userPostLikeStatus);
    }

    public Boolean isLikesExists(PostCacheData post, UserLikesCacheData userPostLikeStatus) {
        return isSetMember(post, userPostLikeStatus);
    }

    public Boolean deleteLikesPost(PostCacheData post) {
        return likesRedisTemplate.delete(post);
    }

    public void setExpiration(PostCacheData post) {
        likesRedisTemplate.expire(post, Duration.ofHours(2));
    }

    public Set<Object> getSetMembers(Object o) {
        return likesRedisTemplate.opsForSet().members(o);
    }

    public void deleteKey(Object o) {
        likesRedisTemplate.delete(o);
    }

    private void addSetMember(Object key, Object value) {
        likesRedisTemplate.opsForSet().add(key, value);
    }

    private void removeSetMember(Object key, Object value) {
        likesRedisTemplate.opsForSet().remove(key, value);
    }

    private Boolean isSetMember(Object key, Object value) {
        return likesRedisTemplate.opsForSet().isMember(key, value);
    }
}
