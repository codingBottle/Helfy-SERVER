package com.codingbottle.service;

import com.codingbottle.auth.domain.entity.Role;
import com.codingbottle.auth.domain.entity.User;
import com.codingbottle.auth.domain.repository.UserRepository;
import com.codingbottle.exception.UserNotFoundException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Transactional
    public User create(FirebaseToken firebaseToken, Role role) {
        User user = User.builder()
                .username(firebaseToken.getUid())
                .email(firebaseToken.getEmail())
                .name(firebaseToken.getName())
                .picture(firebaseToken.getPicture())
                .role(role)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public User updateByUsername(FirebaseToken firebaseToken) {
        User user = userRepository.findByUsername(firebaseToken.getUid())
                .orElseThrow(() -> new UserNotFoundException(String.format("해당 유저(%s)를 찾을 수 없습니다.", firebaseToken.getName())));

        user.update(firebaseToken);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("해당 유저(%s)를 찾을 수 없습니다.", username)));
    }
}
