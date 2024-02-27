package com.codingbottle.domain.post.controller;

import com.codingbottle.common.model.DefaultResponse;
import com.codingbottle.domain.post.model.PostResponseWithLikeStatus;
import com.codingbottle.domain.post.restapi.PostApi;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.common.util.PagingUtil;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.model.PostRequest;
import com.codingbottle.domain.post.model.PostResponse;
import com.codingbottle.domain.post.service.PostService;
import com.codingbottle.domain.post.service.UserPostLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController implements PostApi {
    private final PostService postService;
    private final UserPostLikesService userPostLikesService;

    @Override
    public PostResponseWithLikeStatus create(PostRequest postRequest,
                                             @AuthenticationPrincipal User user) {
        PostResponse post = postService.save(postRequest, user);

        return PostResponseWithLikeStatus.of(post, userPostLikesService.isLikes(user, post.id()));
    }

    @Override
    public SliceImpl<PostResponseWithLikeStatus> findAll( Pageable pageable,
                                                           @AuthenticationPrincipal User user) {
        List<PostResponse> posts = postService.findAll(pageable);

        List<PostResponseWithLikeStatus> postResponses = posts.stream()
                .map(post -> PostResponseWithLikeStatus.of(post, userPostLikesService.isLikes(user, post.id())))
                .toList();

        return PagingUtil.toSliceImpl(postResponses, pageable);
    }

    @Override
    public PostResponseWithLikeStatus update(Long id,
                                             PostRequest postRequest,
                                             @AuthenticationPrincipal User user) {
        PostResponse post = postService.update(postRequest, id, user);

        return PostResponseWithLikeStatus.of(post, userPostLikesService.isLikes(user, post.id()));
    }

    @Override
    public DefaultResponse delete(@PathVariable(value = "id") Long id,
                                  @AuthenticationPrincipal User user) {
        postService.delete(id, user);

        return DefaultResponse.ok();
    }

    @Override
    public boolean toggleLikeStatus(Long postId,
                                    @AuthenticationPrincipal User user) {
        Post post = postService.findById(postId);
        return userPostLikesService.toggleLikeStatus(user, post);
    }

    @Override
    public List<PostResponseWithLikeStatus> searchByKeyword(String keyword,
                                                                            @AuthenticationPrincipal User user) {
        List<PostResponse> posts = postService.searchByKeyword(keyword);

        return posts.stream()
                .map(post -> PostResponseWithLikeStatus.of(post, userPostLikesService.isLikes(user, post.id())))
                .toList();
    }
}
