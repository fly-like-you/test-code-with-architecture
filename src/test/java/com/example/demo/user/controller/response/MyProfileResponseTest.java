package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MyProfileResponseTest {

    @Test
    public void User으로_응답을_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("qkrwnsgh71w@gmail.com")
                .nickname("qkrwnsgh71w")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .build();
        // when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("qkrwnsgh71w@gmail.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("qkrwnsgh71w");
        assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.PENDING);
    }
}