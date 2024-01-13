package com.codingbottle.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PagingUtilTest {
    @Test
    @DisplayName("페이징 처리 시 items의 size가 pageSize보다 작으면 hasNext는 false이다.")
    void if_items_size_is_less_than_pageSize_then_hasNext_is_false() {
        // given
        List<String> items = List.of("1", "2", "3");
        PageRequest pageable = PageRequest.of(0, 10);
        // when
        SliceImpl<String> slice = PagingUtil.toSliceImpl(items, pageable);
        // then
        assertThat(slice.hasNext()).isFalse();
    }

    @Test
    @DisplayName("페이징 처리 시 items의 size가 pageSize보다 크면 hasNext는 true이이고 items의 마지막 요소는 제거된다.")
    void if_items_size_is_greater_than_pageSize_then_hasNext_is_true_and_last_item_is_removed() {
        // given
        List<String> items = new ArrayList<>(List.of("1", "2", "3", "4", "5", "6"));
        PageRequest pageable = PageRequest.of(0, 5);
        // when
        SliceImpl<String> slice = PagingUtil.toSliceImpl(items, pageable);
        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isTrue(),
                () -> assertThat(slice.getContent()).hasSize(5),
                () -> assertThat(slice.getContent().get(4)).isEqualTo("5")
        );
    }

    @Test
    @DisplayName("페이징 처리 시 items가 비어있으면 hasNext는 false이다.")
    void if_items_is_empty_then_hasNext_is_false() {
        // given
        List<String> items = new ArrayList<>();
        PageRequest pageable = PageRequest.of(0, 5);
        // when
        SliceImpl<String> slice = PagingUtil.toSliceImpl(items, pageable);
        // then
        assertThat(slice.hasNext()).isFalse();
    }
}