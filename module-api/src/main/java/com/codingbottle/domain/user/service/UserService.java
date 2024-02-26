package com.codingbottle.domain.user.service;

import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.image.service.ImageService;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.event.UpdateUserInfoAuthCacheEvent;
import com.codingbottle.domain.user.event.UpdateUserInfoRankCacheEvent;
import com.codingbottle.domain.user.model.UserProfileImageUpdateRequest;
import com.codingbottle.domain.user.repository.UserRepository;
import com.codingbottle.domain.user.model.UserInfoUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public User updateInfo(UserInfoUpdateRequest userInfoUpdateRequest, User user) {
        updateNicknameEvent(userInfoUpdateRequest, user);
        return userRepository.save(user);
    }

    public User updateProfileImage(User user, UserProfileImageUpdateRequest userProfileImageUpdateRequest) {
        imageService.deleteByImageUrl(user.getProfileImage());

        Image image = imageService.findById(userProfileImageUpdateRequest.id());
        User updateUser = user.updateProfileImage(image.getImageUrl());

        applicationEventPublisher.publishEvent(updateUser);

        return userRepository.save(updateUser);
    }

    private void updateNicknameEvent(UserInfoUpdateRequest userInfoUpdateRequest, User user) {
        applicationEventPublisher.publishEvent(new UpdateUserInfoRankCacheEvent(this, user, userInfoUpdateRequest.nickname(), userInfoUpdateRequest.region()));
        applicationEventPublisher.publishEvent(new UpdateUserInfoAuthCacheEvent(this, user.updateInfo(userInfoUpdateRequest.nickname(), userInfoUpdateRequest.region())));
    }
}
