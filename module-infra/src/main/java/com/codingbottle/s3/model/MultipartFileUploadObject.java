package com.codingbottle.s3.model;

import java.io.InputStream;

public record MultipartFileUploadObject(
        InputStream inputStream,
        long size,
        String contentType
) {
    public static MultipartFileUploadObject of(InputStream inputStream, long size, String contentType) {
        return new MultipartFileUploadObject(inputStream, size, contentType);
    }
}
