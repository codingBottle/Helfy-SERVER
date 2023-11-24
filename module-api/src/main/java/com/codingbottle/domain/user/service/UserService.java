package com.codingbottle.domain.user.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.user.model.UserNicknameRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Transactional
    public User updateNickname(UserNicknameRequest userNicknameRequest, User user) {
        return user.updateNickname(userNicknameRequest.nickname());
    }
}
