package com.codingbottle.domain.region.entity;

import lombok.Getter;

@Getter
public enum Region {
    SEOUL("서울", 1),
    GYEONGGI("경기", 2),
    INCHEON("인천", 3),
    BUSAN("부산", 4),
    JEJU("제주", 5),
    ULSAN("울산", 6),
    GYEONGSANGNAM("경남", 7),
    DAEGU("대구", 8),
    GYEONGSANGBUK("경북", 9),
    GANGWON("강원", 10),
    DAEJEON("대전", 11),
    CHUNGCHEONGNAM("충남", 12),
    CHUNGCHEONGBUK("충북", 13),
    SEJONG("세종", 14),
    GWANGJU("광주", 15),
    JEOLANAM("전남", 16),
    JEOLABUK("전북", 17),
    NONE("미정", 18);

    private final String detail;
    private final int code;

    Region(String detail, int code) {
        this.detail = detail;
        this.code = code;
    }
}
