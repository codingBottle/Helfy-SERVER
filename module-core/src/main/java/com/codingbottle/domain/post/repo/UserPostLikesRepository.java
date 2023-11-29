package com.codingbottle.domain.post.repo;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.entity.UserPostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostLikesRepository extends JpaRepository<UserPostLikes, Long> {
    void deleteByUserAndPost(User user, Post post);
}
