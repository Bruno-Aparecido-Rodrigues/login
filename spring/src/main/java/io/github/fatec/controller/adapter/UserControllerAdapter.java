package io.github.fatec.controller.adapter;

import io.github.fatec.controller.request.UserRequest;
import io.github.fatec.controller.request.UserUpdateRequest;
import io.github.fatec.entity.User;

import java.util.UUID;

public class UserControllerAdapter {
    private UserControllerAdapter() {
    }

    public static User cast(UserRequest request) {
        return new User(
                UUID.randomUUID().toString(),
                request.username(),
                request.password(),
                request.email(),
                request.cep(),
                request.roles());
    }

    public static User cast(UserUpdateRequest request) {
        return new User(
                request.id(),
                request.username(),
                request.password(),
                request.email(),
                request.cep(),
                request.roles());
    }
}
