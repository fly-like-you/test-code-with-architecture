package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다2() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(123L))
                .uuidHolder(new TestUuidHolder("asdasd"))
                .build();

        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("qkrwnsgh71w@gmail.com")
                        .nickname("qkrwnsgh71w")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("asdasd")
                        .build()
        );

        // when
        ResponseEntity<UserResponse> result = UserController.builder()
                .userReadService(testContainer.userReadService)
                .build()
                .getUserById(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getEmail()).isEqualTo("qkrwnsgh71w@gmail.com");
    }

    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() {
        // given
        UserController userController = UserController.builder()
                .userReadService(new UserReadService() {
                    @Override
                    public User findByEmail(String email) {
                        return null;
                    }

                    @Override
                    public User findByIdOrElseThrow(long id) {
                        return User.builder()
                                .email("qkrwnsgh71w@gmail.com")
                                .nickname("qkrwnsgh71w")
                                .address("Busan")
                                .status(UserStatus.ACTIVE)
                                .build();
                    }
                })
                .build();
        // when
        ResponseEntity<UserResponse> result = userController.getUserById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getEmail()).isEqualTo("qkrwnsgh71w@gmail.com");
    }

    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() {
        // given
        UserController userController = UserController.builder()
                .userReadService(new UserReadService() {
                    @Override
                    public User findByEmail(String email) {
                        return null;
                    }

                    @Override
                    public User findByIdOrElseThrow(long id) {
                        throw new ResourceNotFoundException("Users", id);
                    }
                })
                .build();
        // when
        assertThatThrownBy(
                () -> userController.getUserById(123)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다2() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        UserController userController = UserController.builder()
                .userReadService(testContainer.userReadService)
                .build();


        // when
        assertThatThrownBy(
                () -> userController.getUserById(123)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("qkrwnsgh71w@gmail.com")
                        .nickname("qkrwnsgh71w")
                        .address("Seoul")
                        .status(UserStatus.INACTIVE)
                        .certificationCode("asdasd")
                        .build()
        );

        ResponseEntity<Void> response = testContainer.userController.verifyEmail(1L, "asdasd");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(testContainer.userRepository.findById(1L).get().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() {
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("qkrwnsgh71w@gmail.com")
                        .nickname("qkrwnsgh71w")
                        .address("Seoul")
                        .status(UserStatus.INACTIVE)
                        .certificationCode("asdasd")
                        .build()
        );


        assertThatThrownBy(
                () -> testContainer.userController.verifyEmail(1L, "일치하지않는코드")
        ).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_가지고_올_수_있다() {
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(1L))
                .uuidHolder(new TestUuidHolder("asd"))
                .build();

        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("qkrwnsgh71w@gmail.com")
                        .nickname("qkrwnsgh71w")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("asdasd")
                        .build()
        );

        ResponseEntity<MyProfileResponse> response = testContainer.userController.getMyInfo("qkrwnsgh71w@gmail.com");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(response.getBody().getLastLoginAt()).isEqualTo(1L);

    }

}