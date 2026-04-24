package io.github.fatec.controller.dto.response;

import java.util.List;

public record LoginResponse(
        String id,
        String username,
        List<String> roles
) {}
