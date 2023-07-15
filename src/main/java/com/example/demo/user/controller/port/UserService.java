package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

public interface UserService {

    public User getByEmail(String email);

    public User getById(long id);

    public User create(UserCreate userCreate);

    public User update(long id, UserUpdate userUpdate);

    public void login(long id);

    public void verifyEmail(long id, String certificationCode);
}
