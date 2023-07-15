package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserUpdate;

public interface UserUpdateService {

    public User update(long id, UserUpdate userUpdate);
}
