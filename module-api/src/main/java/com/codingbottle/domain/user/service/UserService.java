package com.codingbottle.domain.user.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.auth.repository.UserRepository;
import com.codingbottle.domain.rank.model.CacheUser;
import com.codingbottle.domain.user.model.UpdateUser;
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
        applicationEventPublisher.publishEvent(
                UpdateUser.of(
                        CacheUser.from(user),
                        CacheUser.from(user.getId(), userNicknameRequest.nickname())));

        return userRepository.save(user.updateNickname(userNicknameRequest.nickname()));
    }
}
