package com.codingbottle.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApplicationErrorType {
    //400
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청값이 올바르지 않습니다."),
    INVALID_HEADER(HttpStatus.BAD_REQUEST, "Header 값이 올바르지 않습니다."),
    INVALID_FIREBASE_TOKEN(HttpStatus.BAD_REQUEST, "Firebase 토큰이 올바르지 않습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "파일 타입이 올바르지 않습니다."),
    NOT_EXIT_WRONG_ANSWER(HttpStatus.BAD_REQUEST, "틀린 문제가 없습니다."),
    INVALID_DATA_ACCESS(HttpStatus.BAD_REQUEST, "데이터 접근이 올바르지 않습니다."),
    //401
    NO_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    //403
    EXPIRED_FIREBASE_TOKEN(HttpStatus.FORBIDDEN, "Firebase 토큰이 만료되었습니다."),
    ALREADY_SOLVED_QUIZ(HttpStatus.FORBIDDEN, "이미 푼 문제입니다."),
    ALREADY_SOLVED_TODAY_QUIZ(HttpStatus.BAD_REQUEST, "이미 오늘의 퀴즈를 푸셨습니다."),
    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    LIKES_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "퀴즈를 찾을 수 없습니다."),
    USER_QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 퀴즈를 찾을 수 없습니다."),
    //500
    WEATHER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "날씨 정보를 가져오는데 실패했습니다."),
    WEB_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "WebClient 에러가 발생했습니다."),
    FAIL_TO_UPLOAD_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패하였습니다"),
    REDIS_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 삭제에 실패하였습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다. 잠시만 기달려주세요."),
    JSON_PROCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 처리 중 에러가 발생했습니다."),
    ;


    private final HttpStatus httpStatus;

    private final String message;

    public int getStatusCode() {
        return httpStatus.value();
    }
}
