package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.infrastructure.SystemClockHolder;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String address;
    private final String certificationCode;
    private final UserStatus status;
    private final Long lastLoginAt;

    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreate userCreate, UuidHolder uuidHolder) {
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .address(userCreate.getAddress())
                .lastLoginAt(Clock.systemUTC().millis())
                .status(UserStatus.PENDING)
                .certificationCode(uuidHolder.random())
                .build();
    }

    public User update(UserUpdate userUpdate) {

        return User.builder()
                .id(id)
                .email(email)
                .nickname(userUpdate.getNickname())
                .address(userUpdate.getAddress())
                .lastLoginAt(lastLoginAt)
                .certificationCode(certificationCode)
                .status(status)
                .build();
    }

    public User login(ClockHolder systemClockHolder) {
        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .lastLoginAt(systemClockHolder.millis())
                .certificationCode(certificationCode)
                .status(status)
                .build();
    }

    public User certificate(String certificationCode) {

        if (!certificationCode.equals(this.certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .lastLoginAt(lastLoginAt)
                .certificationCode(certificationCode)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
