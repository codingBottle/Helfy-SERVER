package com.codingbottle.common.annotation;

import com.codingbottle.model.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = "{\"errorCode\": \"401\", \"reason\": \"인증에 실패했습니다.\"}"))),
        @ApiResponse(responseCode = "403", description = "잘못된 요청", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = "{\"errorCode\": \"403\", \"reason\": \"접근이 거부되었습니다.\"}"))),
        @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = "{\"errorCode\": \"404\", \"reason\": \"요청하신 리소스를 찾을 수 없습니다.\"}"))),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = "{\"errorCode\": \"500\", \"reason\": \"서버 내부 오류가 발생했습니다.\"}")))
})
public @interface DefaultApiResponse {
}
