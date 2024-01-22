package com.codingbottle.domain.user.event;

import com.codingbottle.domain.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateNicknameCacheEvent extends ApplicationEvent {
    private final User user;

    public UpdateNicknameCacheEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
