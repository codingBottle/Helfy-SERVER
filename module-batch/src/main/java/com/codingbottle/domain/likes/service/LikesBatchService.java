package com.codingbottle.domain.likes.service;

import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.entity.UserPostLikes;
import com.codingbottle.domain.post.repo.PostSimpleJPARepository;
import com.codingbottle.domain.post.repo.UserPostLikesRepository;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.repository.UserRepository;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import com.codingbottle.redis.domain.post.model.PostCacheData;
import com.codingbottle.redis.domain.post.model.UserLikesCacheData;
import com.codingbottle.redis.domain.post.service.LikesRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikesBatchService {
    private final LikesRedisService likesRedisService;
    private final UserRepository userRepository;
    private final UserPostLikesRepository userPostLikesRepository;
    private final PostSimpleJPARepository postRepository;

    @Transactional
    public void cacheToDb() {

        Set<PostCacheData> posts = likesRedisService.getSetMembers("UPDATELIST").stream()
                .map(o -> (PostCacheData) o)
                .collect(Collectors.toSet());

        log.info("캐시에서 DB로 저장 시작");
        likesRedisService.deleteKey("UPDATELIST");
        for(PostCacheData post : posts){
            List<User> users = userRepository.findAllById(likesRedisService.getSetMembers(post).stream()
                    .map(o -> (UserLikesCacheData) o)
                    .filter(UserLikesCacheData::status)
                    .map(UserLikesCacheData::id)
                    .collect(Collectors.toList()));

            try {
                Post findPost = postRepository.findById(post.id())
                        .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.POST_NOT_FOUND));

                userPostLikesRepository.saveAll(users.stream()
                        .map(user -> UserPostLikes.builder()
                                .user(user)
                                .post(findPost)
                                .build())
                        .toList());
                log.info("캐시에서 DB로 저장 완료");
            } catch (Exception e) {
                likesRedisService.setUpdateList("UPDATELIST", post);
            }
        }
    }
}
