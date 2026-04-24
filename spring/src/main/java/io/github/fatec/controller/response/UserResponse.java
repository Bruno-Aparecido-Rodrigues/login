package io.github.fatec.controller.response;

import io.github.fatec.entity.enumerable.UserRole;

import java.util.List;

public record UserResponse(
        String id,
        String username,
        String email,
        String cep,
        List<UserRole> roles
) {
}
