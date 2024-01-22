package com.codingbottle.domain.user.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.event.UpdateNicknameCacheEvent;
import com.codingbottle.domain.user.event.UpdateNicknameRedisEvent;
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
        applicationEventPublisher.publishEvent(new UpdateNicknameRedisEvent(this, user, userNicknameRequest.nickname()));
        applicationEventPublisher.publishEvent(new UpdateNicknameCacheEvent(this, user.updateNickname(userNicknameRequest.nickname())));
    }
}
