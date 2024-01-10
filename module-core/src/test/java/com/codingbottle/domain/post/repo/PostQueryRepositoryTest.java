package com.codingbottle.domain.post.repo;

import com.codingbottle.auth.repository.UserRepository;
import com.codingbottle.common.annotation.RepositoryTest;
import com.codingbottle.domain.image.repo.ImageRepository;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.fixture.CoreDomainFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RepositoryTest
class PostQueryRepositoryTest {
    @Autowired
    private PostSimpleJPARepository postSimpleJPARepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostQueryRepository postQueryRepository;

    Post 게시글1;
    Post 게시글2;
    @BeforeEach
    void setUp() {
        userRepository.save(CoreDomainFixture.유저1);
        imageRepository.saveAll(List.of(CoreDomainFixture.게시글_이미지1, CoreDomainFixture.게시글_이미지2));
        게시글1 = postSimpleJPARepository.save(CoreDomainFixture.게시글1);
        게시글2 = postSimpleJPARepository.save(CoreDomainFixture.게시글2);

        게시글1.addHashtags(List.of("해시태그1", "해시태그2"));
        게시글2.addHashtags(List.of("해시태그3", "해시태그4"));
    }

    @Test
    @DisplayName("해시태그 검색어로 게시글을 검색한다.")
    void search_post_by_hashtag() {
        // when
        List<Post> post = postQueryRepository.searchByKeyword("해시태그1");
        // then
        Assertions.assertThat(post).contains(게시글1);
    }
}