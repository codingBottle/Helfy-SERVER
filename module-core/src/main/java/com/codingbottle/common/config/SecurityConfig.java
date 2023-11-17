package com.codingbottle.common.config;

import com.codingbottle.auth.service.UserDetailService;
import com.codingbottle.common.security.entry.CustomAccessDeniedHandler;
import com.codingbottle.common.security.entry.CustomAuthenticationEntryPoint;
import com.codingbottle.common.security.filter.FirebaseTokenFilter;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailService userDetailService;
    private final FirebaseAuth firebaseAuth;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest()
                                .authenticated())
                .addFilterBefore(firebaseTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity.ignoring().requestMatchers("/docs/**","/api-docs/**", "/swagger-ui/**","/favicon.ico");
    }

    public FirebaseTokenFilter firebaseTokenFilter() {
        return new FirebaseTokenFilter(userDetailService, firebaseAuth);
    }
}
