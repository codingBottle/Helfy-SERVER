package com.codingbottle.domain.post.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.auth.service.UserDetailService;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.Post.entity.Post;
import com.codingbottle.domain.post.dto.AddPostRequest;
import com.codingbottle.domain.post.dto.DeletePostRequest;
import com.codingbottle.domain.post.dto.UpdatePostRequest;
import com.codingbottle.domain.post.service.PostService;
import com.codingbottle.domain.region.entity.Region;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("PostController 테스트")
@ContextConfiguration(classes = PostController.class)
@WebMvcTest(value = PostControllerTest.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PostControllerTest extends RestDocsTest {
    @MockBean
    PostService postService;
    @MockBean
    UserDetailService userDetailService;

    @DisplayName("게시글 리스트 조회")
    @Test
    void find_all_posts() throws Exception{
        //given
        User user = User.builder()
                .username("test")
                .region(Region.SEOUL)
                .build();

        Image image = Image.builder()
                .id(1L)
                .build();

        postService.savePost(new AddPostRequest("content1", 1L), user);

        User user2 = User.builder()
                .username("test2")
                .region(Region.SEOUL)
                .build();

        Image image2 = Image.builder()
                .id(2L)
                .build();

        postService.savePost(new AddPostRequest("content2", 2L), user2);

        Page<Post> page = new PageImpl<>(Arrays.asList(
                Post.builder()
                        .content("content1")
                        .image(image)
                        .user(user)
                        .build(),
                Post.builder()
                        .content("content2")
                        .image(image2)
                        .user(user2)
                        .build()));

        given(postService.findAll(any(Pageable.class)))
                .willReturn(page);

        //when & then
        mvc.perform(get("/api/post")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.size").exists())
                .andDo(print());
    }

    @DisplayName("게시글 개별 조회")
    @Test
    void find_post() throws Exception{
        //given
        User user = User.builder()
                .username("test")
                .region(Region.SEOUL)
                .build();

        Image image = Image.builder()
                .id(1L)
                .build();

        postService.savePost(new AddPostRequest("content1", 1L), user);

        Post post = Post.builder()
                .content("content1")
                .image(image)
                .user(user)
                .build();

        given(postService.findById(1L))
                .willReturn(post);

        //when & then
        mvc.perform(get("/api/post/1")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.image").exists())
                .andDo(print());
    }

    @DisplayName("게시글 생성")
    @Test
    void create_post() throws Exception{
        //given
        User user = User.builder()
                .username("test")
                .region(Region.SEOUL)
                .build();

        Image image = Image.builder()
                .id(1L)
                .build();

        Post post = Post.builder()
                .content("content1")
                .image(image)
                .user(user)
                .build();

        given(postService.savePost(any(AddPostRequest.class), any(User.class)))
                .willReturn(post);

        //when & then
        mvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new AddPostRequest("content1",1L)))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.image").exists())
                .andDo(print());
    }

    @DisplayName("게시글 수정")
    @Test
    void update_post() throws Exception{
        //given
        User user = User.builder()
                .username("test")
                .region(Region.SEOUL)
                .build();

        Image image = Image.builder()
                .id(1L)
                .build();

        Post post = Post.builder()
                .content("content2")
                .image(image)
                .user(user)
                .build();

        postService.savePost(new AddPostRequest("content1", 1L), user);

        given(postService.updatePost(any(UpdatePostRequest.class), any(Long.class) ,any(User.class)))
                .willReturn(post);

        //when & then
        mvc.perform(patch("/api/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new UpdatePostRequest("content2",1L)))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.image").exists())
                .andDo(print());
    }

    @DisplayName("게시글 삭제")
    @Test
    void delete_post() throws Exception{
        //given
        User user = User.builder()
                .username("test")
                .region(Region.SEOUL)
                .build();

        Image image = Image.builder()
                .id(1L)
                .build();

        postService.savePost(new AddPostRequest("content1", 1L), user);

        postService.delete(any(Long.class), any(User.class));

        //when & then
        mvc.perform(delete("/api/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new DeletePostRequest(1L)))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
