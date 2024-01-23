package com.codingbottle.domain.user.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.event.UpdateUserInfoCacheEvent;
import com.codingbottle.domain.user.event.UpdateUserInfoRedisEvent;
import com.codingbottle.domain.user.repository.UserRepository;
import com.codingbottle.domain.user.model.UserNicknameRequest;
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
    public User updateNickname(UserNicknameRequest userNicknameRequest, User user) {
        updateNicknameEvent(userNicknameRequest, user);
        return userRepository.save(user);
    }

    private void updateNicknameEvent(UserNicknameRequest userNicknameRequest, User user) {
        applicationEventPublisher.publishEvent(new UpdateUserInfoRedisEvent(this, user, userNicknameRequest.nickname()));
        applicationEventPublisher.publishEvent(new UpdateUserInfoCacheEvent(this, user.updateNickname(userNicknameRequest.nickname())));
    }
}
