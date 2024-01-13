package com.codingbottle.domain.post.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.service.LikesRedisService;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.image.service.ImageService;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.repo.PostQueryRepository;
import com.codingbottle.domain.post.repo.PostSimpleJPARepository;
import com.codingbottle.domain.post.model.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostSimpleJPARepository postSimpleJPARepository;
    private final PostQueryRepository postQueryRepository;
    private final ImageService imageService;
    private final LikesRedisService likesRedisService;

    @Transactional
    @CacheEvict(value = "posts", allEntries = true)
    public Post save(PostRequest postRequest, User user) {
        Image image = imageService.findById(postRequest.imageId());

        Post post = Post.builder()
                .content(postRequest.content())
                .user(user)
                .image(image)
                .build();

        if(postRequest.hashtags() != null) {
            post.addHashtags(postRequest.hashtags());
        }

        return postSimpleJPARepository.save(post);
    }

    @Transactional
    @CacheEvict(value = "posts", allEntries = true)
    public Post update(PostRequest postRequest, Long id, User user) {
        Post post = findById(id);

        if (isNotSameWriter(post, user)) {
            throw new ApplicationErrorException(ApplicationErrorType.NO_AUTHENTICATION, String.format("해당 게시글(%s)에 접근 권한이 없습니다.", id));
        }

        if(!Objects.equals(post.getImage().getId(), postRequest.imageId())) {
            imageService.delete(post.getImage());

            Image image = imageService.findById(postRequest.imageId());
            return post.update(postRequest.content(), image, postRequest.hashtags());
        }

        return post.update(postRequest.content(), postRequest.hashtags());
    }

    @Cacheable(value = "posts", key = "#pageable.pageNumber", unless = "#result == null")
    public List<Post> findAll(Pageable pageable) {
        return postQueryRepository.finAll(pageable);
    }

    public Post findById(Long id) {
        return postSimpleJPARepository.findById(id)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.POST_NOT_FOUND, String.format("해당 게시글(%s)을 찾을 수 없습니다.", id)));
    }

    @Transactional
    @CacheEvict(value = "posts", allEntries = true)
    public void delete(Long id, User user) {
        Post post = findById(id);

        if (isNotSameWriter(post, user)) {
            throw new ApplicationErrorException(ApplicationErrorType.NO_AUTHENTICATION, String.format("해당 게시글(%s)에 접근 권한이 없습니다.", id));
        }

        imageService.delete(post.getImage());
        likesRedisService.deleteLikesPost(id);
        postSimpleJPARepository.delete(post);
    }

    public List<Post> searchByKeyword(String keyword) {
        return postQueryRepository.searchByKeyword(keyword);
    }

    private boolean isNotSameWriter(Post post, User user) {
        return !post.getUser().equals(user);
    }
}
