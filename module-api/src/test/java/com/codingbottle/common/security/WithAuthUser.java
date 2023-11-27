package com.codingbottle.common.security;

import com.codingbottle.domain.region.entity.Region;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthUserSecurityContextFactory.class)
public @interface WithAuthUser {
    String username() default "username";
    String email() default "email";
    String picture() default "picture url";
    Region region() default Region.SEOUL;
    String role() default "ROLE_USER";
}