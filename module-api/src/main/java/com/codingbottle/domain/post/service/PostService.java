package com.codingbottle.domain.post.service;

import com.codingbottle.domain.post.model.PostResponse;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.redis.service.LikesRedisService;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.image.service.ImageService;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.repo.PostQueryRepository;
import com.codingbottle.domain.post.repo.PostSimpleJPARepository;
import com.codingbottle.domain.post.model.PostRequest;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostSimpleJPARepository postSimpleJPARepository;
    private final PostQueryRepository postQueryRepository;
    private final ImageService imageService;
    private final LikesRedisService likesRedisService;
    private final UserPostLikesService userPostLikesService;

    @Transactional
    @CacheEvict(value = "posts", allEntries = true, cacheManager = "postCacheManager")
    public Post save(PostRequest postRequest, User user) {
        Image image = imageService.findById(postRequest.imageId());

        Post post = Post.builder()
                .content(postRequest.content())
                .user(user)
                .image(image)
                .build();

        if (postRequest.hashtags() != null) {
            post.addHashtags(postRequest.hashtags());
        }

        return postSimpleJPARepository.save(post);
    }

    @Transactional
    public boolean toggleLikeStatus(User user, Long postId) {
        Post post = findById(postId);
        if(userPostLikesService.isAlreadyLikes(user, post)){
            userPostLikesService.cancelLikes(user, post);
            return false;
        }
        userPostLikesService.likesPut(user, post);
        return true;
    }

    @Transactional
    @CacheEvict(value = "posts", allEntries = true, cacheManager = "postCacheManager")
    public PostResponse update(PostRequest postRequest, Long id, User user) {
        Post post = findById(id);

        validWriter(id, user, post);

        updateImageIfChanged(postRequest, post);

        post.update(postRequest.content(), postRequest.hashtags());

        return getPostResponse(post, user);
    }

    @Cacheable(value = "posts", key = "#pageable.pageNumber", unless = "#result == null", cacheManager = "postCacheManager")
    public List<PostResponse> findAll(Pageable pageable, User user) {
        List<Post> posts = postQueryRepository.finAll(pageable);

        return posts.stream()
                .map(post -> PostResponse.of(post, userPostLikesService.isAlreadyLikes(user, post)))
                .collect(Collectors.toList());
    }

    public Post findById(Long id) {
        return postSimpleJPARepository.findById(id)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.POST_NOT_FOUND, String.format("해당 게시글(%s)을 찾을 수 없습니다.", id)));
    }

    @Transactional
    @CacheEvict(value = "posts", allEntries = true, cacheManager = "postCacheManager")
    public void delete(Long id, User user) {
        Post post = findById(id);

        validWriter(id, user, post);

        imageService.delete(post.getImage());
        likesRedisService.deleteLikesPost(post);
        postSimpleJPARepository.delete(post);
    }

    public List<PostResponse> searchByKeyword(String keyword, User user) {
        List<Post> posts = postQueryRepository.searchByKeyword(keyword);

        return posts.stream()
                .map(post -> PostResponse.of(post, userPostLikesService.isAlreadyLikes(user, post)))
                .collect(Collectors.toList());
    }

    private void changeImage(PostRequest postRequest, Post post) {
        imageService.delete(post.getImage());

        Image image = imageService.findById(postRequest.imageId());
        post.update(postRequest.content(), image, postRequest.hashtags());
    }

    private void updateImageIfChanged(PostRequest postRequest, Post post) {
        if(!Objects.equals(post.getImage().getId(), postRequest.imageId())) {
            changeImage(postRequest, post);
        } else {
            post.update(postRequest.content(), postRequest.hashtags());
        }
    }

    private void validWriter(Long id, User user, Post post) {
        if (isNotSameWriter(post, user)) {
            throw new ApplicationErrorException(ApplicationErrorType.NO_AUTHENTICATION, String.format("해당 게시글(%s)에 접근 권한이 없습니다.", id));
        }
    }

    private PostResponse getPostResponse(Post post, User user) {
        return PostResponse.of(post, userPostLikesService.isAlreadyLikes(user, post));
    }

    private boolean isNotSameWriter(Post post, User user) {
        return !post.getUser().equals(user);
    }
}
