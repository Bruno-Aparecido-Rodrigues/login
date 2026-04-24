package io.github.fatec.entity;

import io.github.fatec.entity.enumerable.UserRole;

import java.util.List;

public record User(
        String id,
        String username,
        String password,
        String email,
        String cep,
        List<UserRole> roles
) {
}
