package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.infrastructure.SystemUuidHolder;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .address("Seoul")
                .email("qkrwnsgh71w@gmail.com")
                .nickname("qkrwnsgh71w")
                .build();

        // when
        UuidHolder uuidHolder = new TestUuidHolder("aaaa");
        User user = User.from(userCreate, uuidHolder);

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getEmail()).isEqualTo("qkrwnsgh71w@gmail.com");
        assertThat(user.getNickname()).isEqualTo("qkrwnsgh71w");
        assertThat(user.getCertificationCode()).isEqualTo("aaaa");
    }

    @Test
    public void UserUpdate_객체로_데이터를_업데이트_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("qkrwnsgh71w@gmail.com")
                .nickname("qkrwnsgh71w")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .address("Busan")
                .nickname("qkrwnsgh71ww")
                .build();

        // when
        User update = user.update(userUpdate);

        // then
        assertThat(update.getEmail()).isEqualTo("qkrwnsgh71w@gmail.com");
        assertThat(update.getNickname()).isEqualTo("qkrwnsgh71ww");
        assertThat(update.getAddress()).isEqualTo("Busan");
        assertThat(update.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(update.getLastLoginAt()).isEqualTo(100L);
        assertThat(update.getCertificationCode()).isEqualTo("aaaa");
    }

    @Test
    public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        // given
        User user = User.builder()
                .email("qkrwnsgh71w@gmail.com")
                .nickname("qkrwnsgh71w")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaa")
                .build();

        // when
        ClockHolder clockHolder = new TestClockHolder(1000L);
        User login = user.login(clockHolder);

        // then
        assertThat(login.getId()).isNull();
        assertThat(login.getLastLoginAt()).isEqualTo(1000L);
    }

    @Test
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        User user = User.builder()
                .email("qkrwnsgh71w@gmail.com")
                .nickname("qkrwnsgh71w")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaa")
                .build();
        // when
        user =  user.certificate("aaaa");

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    public void 잘못된_인증_코드로_계정을_활성화_하려면_에러를_던진다() {
        // given
        User user = User.builder()
                .email("qkrwnsgh71w@gmail.com")
                .nickname("qkrwnsgh71w")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaa")
                .build();
        // when
        // then
        assertThatThrownBy(() -> user.certificate("aaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}