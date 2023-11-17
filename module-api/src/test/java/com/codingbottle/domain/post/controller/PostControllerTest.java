package com.codingbottle.domain.post.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.post.dto.PostRequest;
import com.codingbottle.domain.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static com.codingbottle.fixture.DomainFixture.*;
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

    private static final String REQUEST_URL = "/api/posts";

    @DisplayName("게시글 리스트 조회")
    @Test
    void find_all_posts() throws Exception{
        //given
        given(postService.findAll(any(PageRequest.class))).willReturn(new PageImpl<>(List.of(게시글1, 게시글2)));
        //when & then
        mvc.perform(get(REQUEST_URL)
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .queryParam("sort", "modifiedTime,desc")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.size").exists())
                .andDo(document("posts-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        queryParameters(
                                List.of(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 사이즈"),
                                        parameterWithName("sort").description("정렬 방식")
                                )),
                        responseFields(
                                fieldWithPath("content").description("게시물 리스트"),
                                fieldWithPath("content[].id").description("게시물 id").type("Number"),
                                fieldWithPath("content[].content").description("게시물 내용"),
                                fieldWithPath("content[].username").description("게시물 작성자"),
                                fieldWithPath("content[].image").description("게시물 이미지"),
                                fieldWithPath("content[].image.id").description("게시물 이미지 id"),
                                fieldWithPath("content[].image.imageUrl").description("게시물 이미지 url"),
                                fieldWithPath("content[].image.directory").description("게시물 이미지 디렉토리"),
                                fieldWithPath("content[].image.convertImageName").description("게시물 이미지 convertImageName"),
                                fieldWithPath("content[].createdTime").description("게시물 생성시간").type("LocalDateTime"),
                                fieldWithPath("content[].modifiedTime").description("게시물 수정시간").type("LocalDateTime"),
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
        given(postService.findById(any(Long.class))).willReturn(게시글1);
        //when & then
        mvc.perform(RestDocumentationRequestBuilders.get(REQUEST_URL + "/{id}", 1L)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.image").exists())
                .andDo(document("get-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        pathParameters(
                                parameterWithName("id").description("게시물 id")),
                        getPostResponseFields()));
    }

    @DisplayName("게시글 생성")
    @Test
    void create_post() throws Exception{
        //given
        given(postService.save(any(PostRequest.class), any(User.class))).willReturn(게시글1);
        //when & then
        mvc.perform(post(REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(게시글_생성_요청1))
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
                        getPostResponseFields()));
    }

    @DisplayName("게시글 수정")
    @Test
    void update_post() throws Exception{
        //given
        given(postService.update(any(PostRequest.class), any(Long.class), any(User.class))).willReturn(게시글2);
        //when & then
        mvc.perform(RestDocumentationRequestBuilders.patch(REQUEST_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(게시글_수정_요청1))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.image").exists())
                .andDo(document("update-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        pathParameters(
                                parameterWithName("id").description("게시물 id")),
                        getPostResponseFields()));
    }

    @DisplayName("게시글 삭제")
    @Test
    void delete_post() throws Exception{
        //when & then
        mvc.perform(RestDocumentationRequestBuilders.delete(REQUEST_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isNoContent())
                .andDo(document("delete-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        pathParameters(
                                parameterWithName("id").description("게시물 id"))));
    }

    private ResponseFieldsSnippet getPostResponseFields() {
        return responseFields(
                fieldWithPath("id").description("게시물 id").type("Number"),
                fieldWithPath("content").description("게시물 내용"),
                fieldWithPath("username").description("게시물 작성자 명"),
                fieldWithPath("image").description("게시물 이미지"),
                fieldWithPath("image.id").description("게시물 이미지 id").type("Number"),
                fieldWithPath("image.imageUrl").description("게시물 이미지 url"),
                fieldWithPath("image.directory").description("게시물 이미지 디렉토리"),
                fieldWithPath("image.convertImageName").description("게시물 이미지 convertImageName"),
                fieldWithPath("createdTime").description("게시물 생성시간").type("LocalDateTime"),
                fieldWithPath("modifiedTime").description("게시물 수정시간").type("LocalDateTime")
        );
    }
}
