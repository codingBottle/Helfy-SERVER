package com.codingbottle.domain.post.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.Post.entity.Post;
import com.codingbottle.domain.post.dto.AddPostRequest;
import com.codingbottle.domain.post.dto.UpdatePostRequest;
import com.codingbottle.domain.post.service.PostService;
import com.codingbottle.domain.region.entity.Region;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("PostController 테스트")
@ContextConfiguration(classes = PostController.class)
@WebMvcTest(value = PostControllerTest.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class PostControllerTest extends RestDocsTest {
    @MockBean
    PostService postService;

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
        mvc.perform(get("/api/post/list")
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.size").exists())
                .andDo(document("posts-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader()
                        ,queryParameters(
                                List.of(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 사이즈")
                                ))
                        ,responseFields(
                                fieldWithPath("content").description("게시물 리스트"),
                                fieldWithPath("content[].postId").description("게시물 id"),
                                fieldWithPath("content[].content").description("게시물 내용"),
                                fieldWithPath("content[].username").description("게시물 작성자"),
                                fieldWithPath("content[].image").description("게시물 이미지"),
                                fieldWithPath("content[].image.id").description("게시물 이미지 id"),
                                fieldWithPath("content[].image.imageUrl").description("게시물 이미지 url"),
                                fieldWithPath("content[].image.directory").description("게시물 이미지 디렉토리"),
                                fieldWithPath("content[].image.convertImageName").description("게시물 이미지 convertImageName"),
                                fieldWithPath("content[].createTime").description("게시물 생성시간"),
                                fieldWithPath("content[].updateTime").description("게시물 수정시간"),

                                fieldWithPath("pageable").description("페이지 정보"),
                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("totalElements").description("전체 요소 수"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("size").description("페이지 크기"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("sort.empty").description("정렬되지 않은 경우"),
                                fieldWithPath("sort.sorted").description("정렬된 경우"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않은 경우"),
                                fieldWithPath("first").description("첫 페이지 여부"),
                                fieldWithPath("numberOfElements").description("현재 페이지의 요소 수"),
                                fieldWithPath("empty").description("데이터가 비어 있는지 여부")
                                )));
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
        mvc.perform(get("/api/post")
                        .queryParam("postId", "1")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.image").exists())
                .andDo(document("get-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader()
                        , queryParameters(
                                parameterWithName("postId").description("게시물 id")
                        )
                , responseFields(
                                fieldWithPath("postId").description("게시물 id"),
                                fieldWithPath("content").description("게시물 내용"),
                                fieldWithPath("username").description("게시물 작성자"),
                                fieldWithPath("image").description("게시물 이미지"),
                                fieldWithPath("image.id").description("게시물 이미지 id"),
                                fieldWithPath("image.imageUrl").description("게시물 이미지 url"),
                                fieldWithPath("image.directory").description("게시물 이미지 디렉토리"),
                                fieldWithPath("image.convertImageName").description("게시물 이미지 convertImageName"),
                                fieldWithPath("createTime").description("게시물 생성시간"),
                                fieldWithPath("updateTime").description("게시물 수정시간")
                                )));
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
                .andDo(document("create-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        requestFields(
                                fieldWithPath("content").description("게시물 내용"),
                                fieldWithPath("imageId").description("게시물 이미지 id")
                        ),
                        responseFields(
                                fieldWithPath("postId").description("게시물 id"),
                                fieldWithPath("content").description("게시물 내용"),
                                fieldWithPath("username").description("게시물 작성자"),
                                fieldWithPath("image").description("게시물 이미지"),
                                fieldWithPath("image.id").description("게시물 이미지 id"),
                                fieldWithPath("image.imageUrl").description("게시물 이미지 url"),
                                fieldWithPath("image.directory").description("게시물 이미지 디렉토리"),
                                fieldWithPath("image.convertImageName").description("게시물 이미지 convertImageName"),
                                fieldWithPath("createTime").description("게시물 생성시간"),
                                fieldWithPath("updateTime").description("게시물 수정시간"))));
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
        mvc.perform(patch("/api/post")
                        .queryParam("postId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new UpdatePostRequest("content2",1L)))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.image").exists())
                .andDo(document("update-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader()
                        , queryParameters(
                                parameterWithName("postId").description("게시물 id")
                        ),
                        responseFields(
                                fieldWithPath("postId").description("게시물 id"),
                                fieldWithPath("content").description("게시물 내용"),
                                fieldWithPath("username").description("게시물 작성자"),
                                fieldWithPath("image").description("게시물 이미지"),
                                fieldWithPath("image.id").description("게시물 이미지 id"),
                                fieldWithPath("image.imageUrl").description("게시물 이미지 url"),
                                fieldWithPath("image.directory").description("게시물 이미지 디렉토리"),
                                fieldWithPath("image.convertImageName").description("게시물 이미지 convertImageName"),
                                fieldWithPath("createTime").description("게시물 생성시간"),
                                fieldWithPath("updateTime").description("게시물 수정시간"))));
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
        mvc.perform(delete("/api/post")
                        .queryParam("postId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isNoContent())
                .andDo(document("delete-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader()
                        , queryParameters(
                                parameterWithName("postId").description("게시물 id")
                        )));
    }
}
