package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;

public interface UserReadService {

    public User getByEmail(String email);

    public User getById(long id);
}
