package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserUpdate;

public interface UserService {
    User update(Long id, UserUpdate userUpdate);

}
