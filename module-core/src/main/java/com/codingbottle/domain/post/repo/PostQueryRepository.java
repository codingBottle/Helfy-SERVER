package com.codingbottle.domain.post.repo;

import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.entity.QPost;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QPost post = QPost.post;

    public List<Post> searchByKeyword(String keyword) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.hashtags.contains(keyword))
                .fetch();
    }

    public List<Post> finAll(Pageable pageable) {
        return jpaQueryFactory.selectFrom(post)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(new OrderSpecifier[]{new OrderSpecifier<>(Order.DESC, post.createdTime)})
                .fetch();
    }
}
