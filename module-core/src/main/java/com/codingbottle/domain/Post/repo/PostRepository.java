package com.codingbottle.domain.Post.repo;

import com.codingbottle.domain.Post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
