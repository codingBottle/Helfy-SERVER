package com.codingbottle.domain.post.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codingbottle.fixture.CoreDomainFixture.*;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    private static final String CONTENT = "게시글 내용";

    @Test
    @DisplayName("게시글 객체를 빌더 패턴을 이용하여 생성한다.")
    void create_post_with_builder() {
        // when
        Post post = Post.builder()
                .content(CONTENT)
                .user(유저1)
                .image(게시글_이미지1)
                .build();
        // then
        assertAll(
                () -> assertEquals(CONTENT, post.getContent()),
                () -> assertEquals(유저1, post.getUser()),
                () -> assertEquals(게시글_이미지1, post.getImage())
        );
    }

    @Test
    @DisplayName("게시글의 내용과 해시태그를 수정한다.")
    void update_post() {
        // given
        Post post = Post.builder()
                .content(CONTENT)
                .user(유저1)
                .image(게시글_이미지1)
                .build();
        // when
        post.update("수정된 게시글 내용", List.of("해시태그1", "해시태그2"));
        // then
        assertAll(
                () -> assertEquals("수정된 게시글 내용", post.getContent()),
                () -> assertEquals(2, post.getHashtags().size())
        );
    }

    @Test
    @DisplayName("게시글의 내용과 해시태그, 이미지를 수정한다.")
    void update_post_with_image() {
        // given
        Post post = Post.builder()
                .content(CONTENT)
                .user(유저1)
                .image(게시글_이미지1)
                .build();
        // when
        post.update("수정된 게시글 내용", 게시글_이미지2, List.of("해시태그1", "해시태그2"));
        // then
        assertAll(
                () -> assertEquals("수정된 게시글 내용", post.getContent()),
                () -> assertEquals(게시글_이미지2, post.getImage()),
                () -> assertEquals(2, post.getHashtags().size())
        );
    }

    @Test
    @DisplayName("게시글의 해시태그를 추가한다.")
    void add_hashtags() {
        // given
        Post post = Post.builder()
                .content(CONTENT)
                .user(유저1)
                .image(게시글_이미지1)
                .build();
        // when
        post.addHashtags(List.of("해시태그1", "해시태그2"));
        // then
        assertAll(
                () -> assertEquals(2, post.getHashtags().size())
        );
    }

    @Test
    @DisplayName("게시글의 좋아요를 추가한다.")
    void add_likes() {
        // given
        Post post = Post.builder()
                .content(CONTENT)
                .user(유저1)
                .image(게시글_이미지1)
                .build();
        // when
        post.addLikes(UserPostLikes.builder()
                .user(유저1)
                .post(post)
                .build());
        // then
        assertAll(
                () -> assertEquals(1, post.getLikes().size())
        );
    }

    @Test
    @DisplayName("게시글의 좋아요를 삭제한다.")
    void remove_likes() {
        // given
        Post post = Post.builder()
                .content(CONTENT)
                .user(유저1)
                .image(게시글_이미지1)
                .build();
        post.addLikes(UserPostLikes.builder()
                .user(유저1)
                .post(post)
                .build());
        // when
        post.removeLikes(유저1);
        // then
        assertAll(
                () -> assertEquals(0, post.getLikes().size())
        );
    }
}