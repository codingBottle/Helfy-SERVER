package com.codingbottle.domain.userPostLikes.repo;

import com.codingbottle.domain.userPostLikes.entity.UserPostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPostLikesRepository extends JpaRepository<UserPostLikes, Long> {
    Optional<UserPostLikes> findByUserAndPost(Long userId, Long postId);
}
