package com.codingbottle.auth.entity;

import com.codingbottle.domain.region.entity.Region;
import com.google.common.base.Objects;
import com.google.firebase.auth.FirebaseToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region = Region.NONE;

    public void update(FirebaseToken token) {
        this.username = token.getUid();
        this.email = token.getEmail();
    }

    public User updateRegion(Region region) {
        this.region = region;

        return this;
    }

    public User updateNickname(String nickname) {
        this.username = nickname;

        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Builder
    public User(String username, String email, String name, String picture, Role role, Region region) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id)
                && Objects.equal(username, user.username)
                && role == user.role
                && Objects.equal(email, user.email)
                && region == user.region;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, username, role, email, region);
    }
}
