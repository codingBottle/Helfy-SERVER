package com.codingbottle.domain.user.event;

import com.codingbottle.domain.user.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateUserInfoCacheEvent extends ApplicationEvent {
    private final User user;

    public UpdateUserInfoCacheEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
