package io.github.fatec.controller;

import io.github.fatec.controller.adapter.AuthControllerAdapter;
import io.github.fatec.controller.request.LoginRequest;
import io.github.fatec.controller.response.AuthResponse;
import io.github.fatec.entity.Token;
import io.github.fatec.security.JwtSecurity;
import io.github.fatec.security.TokenSecurity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fatec/login")
public class AuthController {

    private static final String ACCESS_TOKEN_COOKIE = "access_token";

    private final TokenSecurity tokenSecurity;
    private final JwtSecurity jwtSecurity;

    public AuthController(TokenSecurity tokenSecurity, JwtSecurity jwtSecurity) {
        this.tokenSecurity = tokenSecurity;
        this.jwtSecurity = jwtSecurity;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Token token = tokenSecurity.gerarToken(AuthControllerAdapter.cast(request));
        return new AuthResponse(token.value());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth/cookie")
    public void loginWithCookie(@RequestBody LoginRequest request, HttpServletResponse response) {
        Token token = tokenSecurity.gerarToken(AuthControllerAdapter.cast(request));

        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, token.value())
                .httpOnly(true)
                .secure(false)       // trocar pra true em produção com HTTPS
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtSecurity.getExpirationSeconds())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}