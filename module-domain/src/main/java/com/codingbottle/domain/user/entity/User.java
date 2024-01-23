package com.codingbottle.domain.user.entity;

import com.codingbottle.domain.region.entity.Region;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {
    private static final long serialVersionUID = -8334446707837563525L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String firebaseUid;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region = Region.NONE;

    public User updateByFirebaseToken(final String firebaseUid, final String email) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        return this;
    }

    public User updateRegion(Region region) {
        this.region = region;
        return this;
    }

    public User updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public User updateInfo(String nickname, Region region) {
        this.nickname = nickname;
        this.region = region;
        return this;
    }

    @Builder
    public User(String firebaseUid, String email, String nickname, Role role, Region region) {
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id)
                && Objects.equal(firebaseUid, user.firebaseUid)
                && role == user.role
                && Objects.equal(nickname, user.nickname)
                && Objects.equal(email, user.email)
                && region == user.region;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, firebaseUid, nickname, role, email, region);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firebaseUid='" + firebaseUid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", region=" + region +
                '}';
    }
}
