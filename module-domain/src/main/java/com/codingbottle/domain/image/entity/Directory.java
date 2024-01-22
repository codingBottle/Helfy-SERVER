package com.codingbottle.domain.image.entity;

import lombok.Getter;

@Getter
public enum Directory {
    POST("posts"),INFORMATION("information"), QUIZ("quiz");

    private final String name;

    Directory(String name) {
        this.name = name;
    }
}
