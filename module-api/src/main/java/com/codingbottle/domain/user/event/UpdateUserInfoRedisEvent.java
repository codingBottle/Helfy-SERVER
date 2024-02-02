package com.codingbottle.domain.user.event;

import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.model.UserUpdateInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateUserInfoRedisEvent extends ApplicationEvent {
    private final User user;
    private final UserUpdateInfo userUpdateInfo;

    public UpdateUserInfoRedisEvent(Object source, User user, String nickname, Region region) {
        super(source);
        this.user = user;
        this.userUpdateInfo = UserUpdateInfo.of(nickname, region);
    }
}
