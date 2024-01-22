package com.codingbottle.domain.user.event;

import com.codingbottle.domain.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateNicknameRedisEvent extends ApplicationEvent {
    private final User user;
    private final String nickname;

    public UpdateNicknameRedisEvent(Object source, User user, String nickname) {
        super(source);
        this.user = user;
        this.nickname = nickname;
    }
}
