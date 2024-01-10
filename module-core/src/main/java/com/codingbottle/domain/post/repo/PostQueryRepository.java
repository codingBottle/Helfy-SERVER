package com.codingbottle.domain.post.repo;

import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QPost qPost = QPost.post;

    public List<Post> searchByKeyword(String keyword) {
        return jpaQueryFactory.selectFrom(qPost)
                .where(qPost.hashtags.contains(keyword))
                .fetch();
    }
}
