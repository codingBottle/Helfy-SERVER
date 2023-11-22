package com.codingbottle.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ApplicationErrorType {
    //400
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청값이 올바르지 않습니다."),
    INVALID_HEADER(HttpStatus.BAD_REQUEST, "Header 값이 올바르지 않습니다."),
    INVALID_FIREBASE_TOKEN(HttpStatus.BAD_REQUEST, "Firebase 토큰이 올바르지 않습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "파일 타입이 올바르지 않습니다."),
    NOT_EXIT_WRONG_ANSWER(HttpStatus.BAD_REQUEST, "틀린 문제가 없습니다."),
    //401
    NO_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
    //500
    FAIL_TO_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패하였습니다"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다. 잠시만 기달려주세요.");

    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String message;

    public int getStatusCode() {
        return httpStatus.value();
    }
}
