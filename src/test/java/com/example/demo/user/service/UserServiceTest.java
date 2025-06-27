package com.example.demo.user.service;


import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserServiceTest {

    private UserServiceImpl userService;

    // 테스트 전에 userService를 초기화합니다. (Test Fixture)
    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                .clockHolder(new TestClockHolder(123123123L))
                .uuidHolder(new TestUuidHolder("asdasdasdasd"))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationServiceImpl(fakeMailSender))
                .build();


        fakeUserRepository.save(
                User.builder()
                    .id(1L)
                    .email("qkrwnsgh71w@gmail.com")
                    .nickname("qkrwnsgh71w")
                    .address("Seoul")
                    .certificationCode("123456")
                    .status(UserStatus.ACTIVE)
                    .lastLoginAt(0L)
                    .build()
        );
        fakeUserRepository.save(
                User.builder()
                        .id(2L)
                        .email("qkrwnsgh71w@gmail.com")
                        .nickname("qkrwnsgh71w")
                        .address("Seoul")
                        .certificationCode("123456")
                        .status(UserStatus.PENDING)
                        .lastLoginAt(0L)
                        .build()
        );
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // given
        // when
        userService.login(1L);
        // then
        User user = userService.findByIdOrElseThrow(1L);
        assertThat(user.getLastLoginAt()).isEqualTo(123123123L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        // given
        // when

        userService.verifyEmail(2L, "123456");
        User user = userService.findByIdOrElseThrow(2L);
        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // given
        // when
        // then
        assertThatThrownBy(() ->
                userService.verifyEmail(2, "wrong-code")
        ).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
