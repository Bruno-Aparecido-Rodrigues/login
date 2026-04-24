package io.github.fatec.controller;

import io.github.fatec.controller.adapter.AuthControllerAdapter;
import io.github.fatec.controller.request.LoginRequest;
import io.github.fatec.controller.response.AuthResponse;
import io.github.fatec.entity.Token;
import io.github.fatec.security.TokenSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fatec/login")
public class AuthController {
    private final TokenSecurity tokenSecurity;

    public AuthController(TokenSecurity tokenSecurity) {
        this.tokenSecurity = tokenSecurity;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Token token = tokenSecurity.gerarToken(AuthControllerAdapter.cast(request));
        return new AuthResponse(token.value());
    }
}
