package com.codingbottle.common.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

public class PagingUtil {
    public static <T> SliceImpl<T> toSliceImpl(List<T> items, Pageable pageable) {
        boolean hasNext = false;

        if (items.size() > pageable.getPageSize()) {
            items.remove(items.size() - 1);
            hasNext = true;
        }

        if(items.isEmpty()) {
            items = new ArrayList<>();
        }
        return new SliceImpl<>(items, pageable, hasNext);
    }
}

