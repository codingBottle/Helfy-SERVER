package com.codingbottle.domain.post.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.Image.service.ImageService;
import com.codingbottle.domain.Post.entity.Post;
import com.codingbottle.domain.Post.repo.PostRepository;
import com.codingbottle.domain.post.dto.AddPostRequest;
import com.codingbottle.domain.post.dto.UpdatePostRequest;
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

    @Transactional
    public Post savePost(AddPostRequest addPostRequest, User user) {
        Image image = imageService.findById(addPostRequest.imageId());
        return postRepository.save(addPostRequest.toEntity(user, image));
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.POST_NOT_FOUND, String.format("해당 게시글(%s)을 찾을 수 없습니다.", postId)));
    }

    @Transactional
    public Post updatePost(UpdatePostRequest upPost, Long postId, User user) {
        Post findPost = findById(postId);
        Image image = imageService.findById(upPost.imageId());

        if (!postUserValidation(findPost, user)) {
            throw new ApplicationErrorException(ApplicationErrorType.NO_AUTHENTICATION, String.format("해당 게시글(%s)에 접근 권한이 없습니다.", postId));
        }

        findPost.updatePost(upPost.content(), image);
        return findPost;
    }

    @Transactional
    public void delete (Long postId, User user){
        Post findPost = findById(postId);

        if (postUserValidation(findPost, user)){
            throw new ApplicationErrorException(ApplicationErrorType.NO_AUTHENTICATION, String.format("해당 게시글(%s)에 접근 권한이 없습니다.", postId));
        }
        postRepository.delete(findPost);
    }

    private boolean postUserValidation(Post post, User user) {
        return user == post.getUser();
    }
}
