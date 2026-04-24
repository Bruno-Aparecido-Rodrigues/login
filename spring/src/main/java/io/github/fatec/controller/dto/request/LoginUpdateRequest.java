package io.github.fatec.controller.dto.request;

import java.util.List;

public record LoginUpdateRequest(
        String id,
        String username,
        String password,
        List<String> roles
) {}
