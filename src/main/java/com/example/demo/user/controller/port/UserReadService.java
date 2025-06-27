package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;

public interface UserReadService {
    User findByEmail(String email);

    User findByIdOrElseThrow(long id);
}
