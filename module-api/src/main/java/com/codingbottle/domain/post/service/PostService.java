package com.codingbottle.domain.post.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.LikesRedisService;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.image.service.ImageService;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.repo.PostRepository;
import com.codingbottle.domain.post.model.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;
    private final LikesRedisService likesRedisService;

    @Transactional
    public Post save(PostRequest postRequest, User user) {
        Image image = imageService.findById(postRequest.imageId());

        Post post = Post.builder()
                .content(postRequest.content())
                .user(user)
                .image(image)
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public Post update(PostRequest postRequest, Long id, User user) {
        Post post = findById(id);

        if (isNotSameWriter(post, user)) {
            throw new ApplicationErrorException(ApplicationErrorType.NO_AUTHENTICATION, String.format("해당 게시글(%s)에 접근 권한이 없습니다.", id));
        }

        Image image = imageService.findById(postRequest.imageId());
        post.update(postRequest.content(), image);

        return post;
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.POST_NOT_FOUND, String.format("해당 게시글(%s)을 찾을 수 없습니다.", id)));
    }

    @Transactional
    public void delete(Long id, User user) {
        Post post = findById(id);

        if (isNotSameWriter(post, user)) {
            throw new ApplicationErrorException(ApplicationErrorType.NO_AUTHENTICATION, String.format("해당 게시글(%s)에 접근 권한이 없습니다.", id));
        }
        postRepository.delete(post);
        likesRedisService.deleteLikesPost(post.getId());
    }

    private boolean isNotSameWriter(Post post, User user) {
        return !user.equals(post.getUser());
    }
}
