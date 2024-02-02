package com.codingbottle.security.service;

import com.codingbottle.domain.user.entity.Role;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.event.UpdateUserInfoCacheEvent;
import com.codingbottle.domain.user.repository.UserRepository;
import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @EventListener
    @CachePut(value = "user", key = "#event.user.email", cacheManager = "authRedisCacheManager")
    public User handleUpdateUserNickname(UpdateUserInfoCacheEvent event) {
        return event.getUser();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String firebaseUid) throws UsernameNotFoundException {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        return new SecurityUserDetails(user);
    }

    @Transactional
    @CachePut(value = "user", key = "#firebaseToken.uid", cacheManager = "authRedisCacheManager")
    public User create(FirebaseToken firebaseToken) {
        User user = User.builder()
                .firebaseUid(firebaseToken.getUid())
                .email(firebaseToken.getEmail())
                .nickname("user" + UUID.randomUUID())
                .region(Region.NONE)
                .role(Role.ROLE_USER)
                .build();

       return userRepository.save(user);
    }

    @Transactional
    @Cacheable(value = "user", key = "#firebaseToken.email", cacheManager = "authRedisCacheManager")
    public User updateByUsername(FirebaseToken firebaseToken) {
        User user = userRepository.findByFirebaseUid(firebaseToken.getUid())
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.USER_NOT_FOUND));

        return user.updateByFirebaseToken(firebaseToken.getUid(), firebaseToken.getEmail());
    }
}
