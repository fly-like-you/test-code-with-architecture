package com.example.demo.post.domain;

import com.example.demo.post.service.PostService;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    @Test
    void PostCreate으로_게시물을_만들_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .content("테스트용 게시물입니다.")
                .writerId(1L)
                .build();
        User writer = User.builder()
                .email("qkrwnsgh71w@gmail.com")
                .nickname("qkrwnsgh71w")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode(UUID.randomUUID().toString())
                .build();
        // when
        Post post = Post.from(writer, postCreate);

        assertThat(post.getContent()).isEqualTo("테스트용 게시물입니다.");
        assertThat(post.getWriter().getEmail()).isEqualTo("qkrwnsgh71w@gmail.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("qkrwnsgh71w");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void PostUpdate로_게시물을_수정할_수_있다() {
        // given
        Post post = Post.builder()
                .id(1L)
                .content("기존 게시물 내용")
                .writer(User.builder()
                        .email("")
                        .build())
                .build();
        PostUpdate postUpdate = PostUpdate.builder()
                        .content("수정 게시물 내용")
                        .build();

        post = post.update(postUpdate);
        assertThat(post.getContent()).isEqualTo("수정 게시물 내용");


    }

}