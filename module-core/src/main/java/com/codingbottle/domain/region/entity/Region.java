package com.codingbottle.domain.region.entity;

import lombok.Getter;

@Getter
public enum Region {
    SEOUL("서울", 1, 37.5665, 126.9780),
    GYEONGGI("경기", 2, 37.4138, 127.5183),
    INCHEON("인천", 3, 37.4563, 126.7052),
    BUSAN("부산", 4, 35.1796, 129.0756),
    JEJU("제주", 5, 33.4996, 126.5312),
    ULSAN("울산", 6, 35.5384, 129.3114),
    GYEONGSANGNAM("경남", 7, 35.4606, 128.2132),
    DAEGU("대구", 8, 35.8714, 128.6014),
    GYEONGSANGBUK("경북", 9, 36.4919, 128.8889),
    GANGWON("강원", 10, 37.8228, 128.1555),
    DAEJEON("대전", 11, 36.3504, 127.3845),
    CHUNGCHEONGNAM("충남", 12, 36.6588, 126.6728),
    CHUNGCHEONGBUK("충북", 13, 36.6357, 127.4912),
    SEJONG("세종", 14, 36.4801, 127.2892),
    GWANGJU("광주", 15, 35.1601, 126.8514),
    JEOLANAM("전남", 16, 34.8166, 126.4629),
    JEOLABUK("전북", 17, 35.7175, 127.1530),
    NONE("미정", 18, 37.5665, 126.9780);

    private final String detail;
    private final int code;
    private final double latitude;
    private final double longitude;

    Region(String detail, int code, double latitude, double longitude) {
        this.detail = detail;
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
