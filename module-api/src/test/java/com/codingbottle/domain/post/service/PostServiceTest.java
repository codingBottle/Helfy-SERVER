package com.codingbottle.domain.post.service;

import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.redis.service.LikesRedisService;
import com.codingbottle.domain.image.service.ImageService;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.repo.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.codingbottle.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;
    @Mock
    private LikesRedisService likesRedisService;

    @Mock
    private ImageService imageService;

    @Test
    @DisplayName("ID에 해당하는 게시글을 조회한다.")
    void find_post_by_id() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(게시글1));
        // when
        Post post = postService.findById(anyLong());
        // then
        assertThat(post).isEqualTo(게시글1);
    }

    @Test
    @DisplayName("ID에 해당하는 게시글이 없으면 예외를 발생시킨다.")
    void find_post_by_id_not_found() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> postService.findById(anyLong()))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("게시글을 저장한다.")
    void save_post() {
        // given
        given(imageService.findById(anyLong())).willReturn(게시글1.getImage());
        given(postRepository.save(any(Post.class))).willReturn(게시글1);
        // when
        Post post = postService.save(게시글_생성_요청1, 게시글1.getUser());
        // then
        assertThat(post).isEqualTo(게시글1);
    }

    @Test
    @DisplayName("게시글을 수정한다.")
    void update_post() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(게시글1));
        given(imageService.findById(anyLong())).willReturn(게시글_수정_이미지1);
        // when
        Post post = postService.update(게시글_수정_요청1, 게시글1.getId(), 게시글1.getUser());
        // then
        assertAll(() -> {
            assertThat(post.getContent()).isEqualTo(게시글_수정_요청1.content());
            assertThat(post.getImage()).isEqualTo(게시글_수정_이미지1);
        });
    }

    @Test
    @DisplayName("게시글 수정시 게시글 작성자와 수정자가 같은 사용자가 아닌 경우 예외를 발생시킨다.")
    void update_post_not_same_user() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(게시글1));
        // when & then
        assertThatThrownBy(() -> postService.update(게시글_수정_요청1, 게시글1.getId(), 유저2))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("게시글 삭제시 게시글 작성자와 삭제자가 같은 사용자가 아닌 경우 예외를 발생시킨다.")
    void delete_post_not_same_user() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(게시글1));
        // when & then
        assertThatThrownBy(() -> postService.delete(게시글1.getId(), 유저2))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("게시글을 삭제한다.")
    void delete_post() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(게시글1));
        given(likesRedisService.deleteLikesPost(any())).willReturn(true);
        // when
        postService.delete(게시글1.getId(), 게시글1.getUser());
        // then
        verify(postRepository).delete(any(Post.class));
        verify(likesRedisService).deleteLikesPost(any());
    }

    @Test
    @DisplayName("게시글을 모두 조회한다.")
    void find_all_post() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);

        given(postRepository.findAll(any(PageRequest.class))).willReturn(new PageImpl<>(List.of(게시글1)));
        // when
        Page<Post> posts = postService.findAll(pageRequest);
        // then
        assertThat(posts).containsExactly(게시글1);
    }
}