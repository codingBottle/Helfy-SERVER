package com.codingbottle.domain.post.controller;

import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.post.model.PostResponse;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.post.model.PostRequest;
import com.codingbottle.domain.post.service.PostService;
import com.codingbottle.domain.post.service.UserPostLikesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    @MockBean
    UserPostLikesService userPostLikesService;

    private static final String REQUEST_URL = "/api/v1/posts";

    @DisplayName("게시글 리스트 조회")
    @Test
    void find_all_posts() throws Exception{
        //given
        given(postService.findAll(any(PageRequest.class))).willReturn(List.of(PostResponse.from(게시글1), PostResponse.from(게시글2)));
        given(userPostLikesService.isLikes(any(User.class), any())).willReturn(false);
        //when & then
        mvc.perform(get(REQUEST_URL)
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .queryParam("sort", "modifiedTime,desc")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
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
                                fieldWithPath("content[].post.id").description("게시물 id").type("Number"),
                                fieldWithPath("content[].post.content").description("게시물 내용"),
                                fieldWithPath("content[].post.hashtags").description("게시물 해시태그"),
                                fieldWithPath("content[].post.user").description("게시물 작성자"),
                                fieldWithPath("content[].post.user.nickname").description("게시물 작성자 닉네임"),
                                fieldWithPath("content[].post.likeCount").description("게시물 좋아요 수"),
                                fieldWithPath("content[].post.image").description("게시물 이미지"),
                                fieldWithPath("content[].post.image.id").description("게시물 이미지 id"),
                                fieldWithPath("content[].post.image.imageUrl").description("게시물 이미지 url"),
                                fieldWithPath("content[].post.createdTime").description("게시물 생성시간").type("LocalDateTime"),
                                fieldWithPath("content[].post.modifiedTime").description("게시물 수정시간").type("LocalDateTime"),
                                fieldWithPath("content[].likeStatus").description("게시물 좋아요 여부"),
                                fieldWithPath("pageable").description("페이지 정보"),
                                fieldWithPath("pageable.pageNumber").description("페이지 번호"),
                                fieldWithPath("pageable.pageSize").description("페이지 사이즈"),
                                fieldWithPath("pageable.sort").description("정렬 방식"),
                                fieldWithPath("pageable.sort.empty").description("페이징 정렬 방식 (빈 값)"),
                                fieldWithPath("pageable.sort.sorted").description("페이징 정렬 유무"),
                                fieldWithPath("pageable.sort.unsorted").description("페이징 정렬 유무"),
                                fieldWithPath("pageable.offset").description("페이지 offset"),
                                fieldWithPath("pageable.paged").description("페이지 여부"),
                                fieldWithPath("pageable.unpaged").description("페이지 여부"),
                                fieldWithPath("first").description("첫번째 페이지 여부"),
                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("size").description("페이지 사이즈"),
                                fieldWithPath("number").description("페이지 번호"),
                                fieldWithPath("sort").description("정렬 방식"),
                                fieldWithPath("sort.empty").description("정렬 여부"),
                                fieldWithPath("sort.sorted").description("정렬 여부"),
                                fieldWithPath("sort.unsorted").description("정렬 여부"),
                                fieldWithPath("numberOfElements").description("페이지 요소 수"),
                                fieldWithPath("empty").description("페이지 여부"))));
    }

    @DisplayName("게시글 생성")
    @Test
    void create_post() throws Exception{
        //given
        given(postService.save(any(PostRequest.class), any(User.class))).willReturn(PostResponse.from(게시글1));
        given(userPostLikesService.isLikes(any(User.class), any())).willReturn(false);
        //when & then
        mvc.perform(post(REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(게시글_생성_요청1))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("create-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        requestFields(
                                fieldWithPath("content").description("게시물 내용 (필수)"),
                                fieldWithPath("imageId").description("게시물 이미지 id (필수)"),
                                fieldWithPath("hashtags").description("게시물 해시태그 (선택)")
                        ),
                        getPostResponseFields()));
    }

    @DisplayName("게시글 수정")
    @Test
    void update_post() throws Exception{
        //given
        given(postService.update(any(PostRequest.class), any(Long.class), any(User.class))).willReturn(PostResponse.from(게시글2));
        //when & then
        mvc.perform(RestDocumentationRequestBuilders.patch(REQUEST_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(게시글_수정_요청1))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
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
                .andExpect(status().isOk())
                .andDo(document("delete-post",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        pathParameters(
                                parameterWithName("id").description("게시물 id"))));
    }

    @Test
    @DisplayName("게시글 좋아요 요청")
    void likes_put() throws Exception{
        //given
        given(postService.findById(any(Long.class))).willReturn(게시글1);
        given(userPostLikesService.toggleLikeStatus(any(User.class), any(Post.class))).willReturn(true);
        //when & then
        mvc.perform(RestDocumentationRequestBuilders.put(REQUEST_URL + "/{id}/likes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("put-likes",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        pathParameters(
                                parameterWithName("id").description("게시물 id"))));
    }

    @Test
    @DisplayName("게시글 좋아요 취소 요청")
    void likes_cancel() throws Exception{
        //given
        given(userPostLikesService.isLikes(any(User.class),any())).willReturn(false);
        //when & then
        mvc.perform(RestDocumentationRequestBuilders.put(REQUEST_URL + "/{id}/likes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("put-likes",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        pathParameters(
                                parameterWithName("id").description("게시물 id"))));
    }

    @Test
    @DisplayName("게시글 검색")
    void search_keyword() throws Exception{
        //given
        given(postService.searchByKeyword(any(String.class))).willReturn(List.of(PostResponse.from(게시글1), PostResponse.from(게시글2)));
        given(userPostLikesService.isLikes(any(User.class), any())).willReturn(false);
        //when & then
        mvc.perform(get(REQUEST_URL + "/search")
                        .queryParam("keyword", "검색어")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("search-keyword",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        queryParameters(
                                parameterWithName("keyword").description("검색어")),
                        responseFields(
                                fieldWithPath("[].post").description("게시물 리스트"),
                                fieldWithPath("[].post.id").description("게시물 id").type("Number"),
                                fieldWithPath("[].post.content").description("게시물 내용"),
                                fieldWithPath("[].post.hashtags").description("게시물 해시태그"),
                                fieldWithPath("[].post.user").description("게시물 작성자"),
                                fieldWithPath("[].post.user.nickname").description("게시물 작성자 닉네임"),
                                fieldWithPath("[].post.likeCount").description("게시물 좋아요 수"),
                                fieldWithPath("[].post.image").description("게시물 이미지"),
                                fieldWithPath("[].post.image.id").description("게시물 이미지 id"),
                                fieldWithPath("[].post.image.imageUrl").description("게시물 이미지 url"),
                                fieldWithPath("[].post.createdTime").description("게시물 생성시간").type("LocalDateTime"),
                                fieldWithPath("[].post.modifiedTime").description("게시물 수정시간").type("LocalDateTime"),
                                fieldWithPath("[].likeStatus").description("게시물 좋아요 여부")
                        )));
    }

    private ResponseFieldsSnippet getPostResponseFields() {
        return responseFields(
                fieldWithPath("post").description("게시물"),
                fieldWithPath("post.id").description("게시물 id").type("Number"),
                fieldWithPath("post.content").description("게시물 내용"),
                fieldWithPath("post.hashtags").description("게시물 해시태그"),
                fieldWithPath("post.user.nickname").description("게시물 작성자 닉네임"),
                fieldWithPath("post.likeCount").description("게시물 좋아요 수"),
                fieldWithPath("post.image").description("게시물 이미지"),
                fieldWithPath("post.image.id").description("게시물 이미지 id").type("Number"),
                fieldWithPath("post.image.imageUrl").description("게시물 이미지 url"),
                fieldWithPath("post.createdTime").description("게시물 생성시간").type("LocalDateTime"),
                fieldWithPath("post.modifiedTime").description("게시물 수정시간").type("LocalDateTime"),
                fieldWithPath("likeStatus").description("게시물 좋아요 여부")
        );
    }
}
