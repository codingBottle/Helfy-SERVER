package com.codingbottle.domain.region.service;

import com.codingbottle.domain.region.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {
    public Map<String, String> findAll() {
        return Arrays.stream(Region.values())
                .filter(region -> !region.equals(Region.NONE))
                .sorted(Comparator.comparing(Region::getCode))
                .collect(Collectors.toMap(Region::name, Region::getDetail, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
