package io.github.fatec.controller;

import io.github.fatec.controller.adapter.AuthControllerAdapter;
import io.github.fatec.controller.adapter.UserControllerAdapter;
import io.github.fatec.controller.request.LoginRequest;
import io.github.fatec.controller.request.UserRequest;
import io.github.fatec.controller.response.AuthResponse;
import io.github.fatec.entity.Login;
import io.github.fatec.entity.Token;
import io.github.fatec.entity.User;
import io.github.fatec.repository.UserRepository;
import io.github.fatec.security.JwtSecurity;
import io.github.fatec.security.TokenSecurity;
import io.github.fatec.security.dto.AuthUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fatec/login")
public class AuthController {

    private static final String ACCESS_TOKEN_COOKIE = "access_token";

    private final UserRepository repository;
    private final JwtSecurity jwtSecurity;
    private final TokenSecurity tokenSecurity;

    public AuthController(
            UserRepository repository,
            JwtSecurity jwtSecurity,
            TokenSecurity tokenSecurity) {
        this.repository = repository;
        this.jwtSecurity = jwtSecurity;
        this.tokenSecurity = tokenSecurity;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/create")
    public AuthResponse create(@RequestBody UserRequest request, HttpServletResponse response) {
        User user = repository.save(UserControllerAdapter.cast(request));
        Login login = new Login(user.id(), user.username(), request.password(),
                                user.roles().stream().map(Enum::name).toList());
        AuthUserDetails userDetails = tokenSecurity.autenticar(login);
        Token token = tokenSecurity.gerarToken(login);
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(token.value()).toString());
        return new AuthResponse(
                userDetails.user().id(),
                userDetails.user().username(),
                userDetails.user().roles().stream().map(Enum::name).toList(),
                token.value());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth")
    public AuthResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Login login = AuthControllerAdapter.cast(request);
        AuthUserDetails userDetails = tokenSecurity.autenticar(login);
        Token token = tokenSecurity.gerarToken(login);
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(token.value()).toString());
        return new AuthResponse(
                userDetails.user().id(),
                userDetails.user().username(),
                userDetails.user().roles().stream().map(Enum::name).toList(),
                token.value());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/v1/logout")
    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private ResponseCookie buildCookie(String tokenValue) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, tokenValue)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtSecurity.getExpirationSeconds())
                .build();
    }
}