package com.codingbottle.domain.region.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.user.event.UpdateUserInfoCacheEvent;
import com.codingbottle.domain.user.event.UpdateUserInfoRedisEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final ApplicationEventPublisher applicationEventPublisher;

    public Map<String, String> findAll() {
        return Arrays.stream(Region.values())
                .filter(region -> !region.equals(Region.NONE))
                .sorted(Comparator.comparing(Region::getCode))
                .collect(Collectors.toMap(Region::name, Region::getDetail, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @Transactional
    public User updateRegions(Region region, User user) {
        updateRegionEvent(region, user);
        return user.updateRegion(region);
    }

    private void updateRegionEvent(Region region, User user) {
        applicationEventPublisher.publishEvent(new UpdateUserInfoRedisEvent(this, user, region));
        applicationEventPublisher.publishEvent(new UpdateUserInfoCacheEvent(this, user.updateRegion(region)));
    }
}
