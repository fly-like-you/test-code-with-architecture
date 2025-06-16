package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {
    @Test
    public void Post로_응답을_생성할_수_있다() {
        // given
        Post post = Post.builder()
                .writer(User.builder()
                        .email("qkrwnsgh71w@gmail.com")
                        .nickname("qkrwnsgh71w")
                        .address("Seoul")
                        .status(UserStatus.PENDING)
                        .certificationCode(UUID.randomUUID().toString())
                        .build())
                .content("content")
                .build();
        // when
        PostResponse postResponse = PostResponse.from(post);

        // then
        assertThat(postResponse.getContent()).isEqualTo("content");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("qkrwnsgh71w@gmail.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("qkrwnsgh71w");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.PENDING);
    }
}