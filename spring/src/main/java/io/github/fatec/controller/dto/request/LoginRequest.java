package io.github.fatec.controller.dto.request;

import java.util.List;

public record LoginRequest(
        String username,
        String password,
        List<String> roles
) {}
