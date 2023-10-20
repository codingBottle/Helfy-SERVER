package com.codingbottle.domain.Image.controller;

import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.Image.entity.Directory;
import com.codingbottle.domain.Image.entity.Image;
import com.codingbottle.domain.Image.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import java.nio.charset.StandardCharsets;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ImageController 테스트")
@ContextConfiguration(classes = ImageController.class)
@WebMvcTest(value = ImageController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ImageControllerTest extends RestDocsTest {
    @MockBean
    ImageService imageService;

    @Test
    @DisplayName("이미지 업로드")
    void image_upload() throws Exception {
        //given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "image.png",
                "multipart/form-data",
                "image.png".getBytes(StandardCharsets.UTF_8));

        given(imageService.upload(mockMultipartFile, Directory.POST)).willReturn(Image.builder()
                .id(1L)
                .imageUrl("https://d2zp5u7z0buhfu.cloudfront.net/swagger.png")
                .build());
        //when & then
        mvc.perform(multipart("/api/v1/images")
                        .file(mockMultipartFile)
                        .header("Authorization", "Bearer FirebaseToken")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("directory", "POST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("imageId").value(1L))
                .andExpect(jsonPath("imageUrl").value("https://d2zp5u7z0buhfu.cloudfront.net/swagger.png"))
                .andDo(document("images-upload",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        queryParameters(
                                parameterWithName("directory")
                                        .description("이미지 디렉토리(POST, INFORMATION, QUIZ)")
                        ),
                        requestParts(
                                partWithName("image").description("이미지 파일 (png, jpg, jpeg)")
                        ),
                        responseFields(
                                fieldWithPath("imageId").description("이미지 식별자"),
                                fieldWithPath("imageUrl").description("이미지 URL"))));
    }
}