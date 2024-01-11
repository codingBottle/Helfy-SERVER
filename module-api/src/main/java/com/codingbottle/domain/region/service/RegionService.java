package com.codingbottle.domain.region.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.region.entity.Region;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegionService {
    public Map<String, String> findAll() {
        return Arrays.stream(Region.values())
                .filter(region -> !region.equals(Region.NONE))
                .sorted(Comparator.comparing(Region::getCode))
                .collect(Collectors.toMap(Region::name, Region::getDetail, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @Transactional
    public User updateRegions(Region region, User user) {
        return user.updateRegion(region);
    }
}
