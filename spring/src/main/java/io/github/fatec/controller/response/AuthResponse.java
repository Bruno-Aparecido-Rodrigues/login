package io.github.fatec.controller.response;

import java.util.List;

public record AuthResponse(
        String id,
        String username,
        List<String> roles,
        String token
) {
}