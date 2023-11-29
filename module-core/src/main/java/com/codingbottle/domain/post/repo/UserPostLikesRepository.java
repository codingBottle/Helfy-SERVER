package com.codingbottle.domain.post.repo;

import com.codingbottle.domain.post.entity.UserPostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPostLikesRepository extends JpaRepository<UserPostLikes, Long> {
    void deleteByUserAndPost(Long userId, Long postId);
}
