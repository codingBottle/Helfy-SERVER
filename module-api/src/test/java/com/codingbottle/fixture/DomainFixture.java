package com.codingbottle.fixture;

import com.codingbottle.auth.entity.Role;
import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.image.entity.Directory;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.dto.PostRequest;
import com.codingbottle.domain.region.entity.Region;


public class DomainFixture {
    public static final Image 게시글1_이미지 = Image.builder()
            .directory(Directory.POST)
            .imageUrl("https://d1csu9i9ktup9e.cloudfront.net/default.png")
            .convertImageName("default.png")
            .build();

    public static final Image 게시글2_이미지 = Image.builder()
            .directory(Directory.POST)
            .imageUrl("https://d1csu9i9ktup9e.cloudfront.net/default.png")
            .convertImageName("default.png")
            .build();

    public static final User 유저1 = User.builder()
            .username("유저1")
            .email("helfy@gmail.com")
            .region(Region.SEOUL)
            .picture("https://d1csu9i9ktup9e.cloudfront.net/default.png")
            .role(Role.ROLE_USER)
            .build();

    public static final User 유저2 = User.builder()
            .username("유저2")
            .email("helfy@gmail.com")
            .region(Region.SEOUL)
            .picture("https://d1csu9i9ktup9e.cloudfront.net/default.png")
            .role(Role.ROLE_USER)
            .build();

    public static final Post 게시글1 = Post.builder()
            .content("게시글")
            .image(게시글1_이미지)
            .user(유저1)
            .build();

    public static final Post 게시글2 = Post.builder()
            .content("게시글")
            .image(게시글2_이미지)
            .user(유저1)
            .build();

    public static final PostRequest 게시글_생성_요청1 = new PostRequest("게시글", 1L);

    public static final PostRequest 게시글_수정_요청1 = new PostRequest("게시글 수정", 1L);

    public static final Image 게시글_수정_이미지1 = Image.builder()
            .directory(Directory.POST)
            .imageUrl("https://d1csu9i9ktup9e.cloudfront.net/default.png")
            .convertImageName("default.png")
            .build();
}

