package com.codingbottle.common.security;


import com.codingbottle.domain.user.entity.Role;
import com.codingbottle.domain.user.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        String email = annotation.email();
        String role = annotation.role();

        User user = User.builder()
                .nickname(annotation.username())
                .email(email)
                .region(annotation.region())
                .role(Role.ROLE_ADMIN)
                .profileImage("https://d1csu9i9ktup9e.cloudfront.net/default.png")
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority(role)));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}