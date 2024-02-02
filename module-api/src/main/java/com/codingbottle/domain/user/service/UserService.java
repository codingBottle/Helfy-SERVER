package com.codingbottle.domain.user.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.event.UpdateUserInfoCacheEvent;
import com.codingbottle.domain.user.event.UpdateUserInfoRedisEvent;
import com.codingbottle.domain.user.repository.UserRepository;
import com.codingbottle.domain.user.model.UserInfoUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;

    @Transactional
    public User updateInfo(UserInfoUpdateRequest userInfoUpdateRequest, User user) {
        updateNicknameEvent(userInfoUpdateRequest, user);
        return userRepository.save(user);
    }

    private void updateNicknameEvent(UserInfoUpdateRequest userInfoUpdateRequest, User user) {
        applicationEventPublisher.publishEvent(new UpdateUserInfoRedisEvent(this, user, userInfoUpdateRequest.nickname(), userInfoUpdateRequest.region()));
        applicationEventPublisher.publishEvent(new UpdateUserInfoCacheEvent(this, user.updateInfo(userInfoUpdateRequest.nickname(), userInfoUpdateRequest.region())));
    }
}
