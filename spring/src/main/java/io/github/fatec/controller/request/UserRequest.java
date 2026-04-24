package io.github.fatec.controller.request;

import io.github.fatec.entity.enumerable.UserRole;

import java.util.List;

public record UserRequest(
        String username,
        String password,
        String email,
        String cep,
        List<UserRole> roles
) {
}
