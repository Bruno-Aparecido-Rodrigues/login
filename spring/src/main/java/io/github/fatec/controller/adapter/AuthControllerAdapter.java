package io.github.fatec.controller.adapter;

import io.github.fatec.controller.request.LoginRequest;
import io.github.fatec.entity.Login;

public class AuthControllerAdapter {
    private AuthControllerAdapter() {
    }

    public static Login cast(LoginRequest request) {
        return new Login(null, request.username(), request.password(), null);
    }
}
