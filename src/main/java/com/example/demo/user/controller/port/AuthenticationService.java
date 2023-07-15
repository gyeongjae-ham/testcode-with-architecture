package com.example.demo.user.controller.port;

public interface AuthenticationService {

    public void login(long id);

    public void verifyEmail(long id, String certificationCode);
}
