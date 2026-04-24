package io.github.fatec.controller;

import io.github.fatec.controller.dto.request.LoginRequest;
import io.github.fatec.controller.dto.request.LoginUpdateRequest;
import io.github.fatec.controller.dto.response.LoginResponse;
import io.github.fatec.entity.Login;
import io.github.fatec.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fatec/login/login")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public LoginResponse salvar(@RequestBody LoginRequest request) {
        Login login = new Login(null, request.username(), request.password(), request.roles());
        Login salvo = service.salvar(login);
        return new LoginResponse(salvo.id(), salvo.username(), salvo.roles());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public LoginResponse atualizar(@RequestBody LoginUpdateRequest request) {
        Login login = new Login(request.id(), request.username(), request.password(), request.roles());
        Login atualizado = service.atualizar(login);
        return new LoginResponse(atualizado.id(), atualizado.username(), atualizado.roles());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable String id) {
        service.deletar(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<LoginResponse> listar() {
        return service.listar()
                .stream()
                .map(l -> new LoginResponse(l.id(), l.username(), l.roles()))
                .collect(Collectors.toList());
    }
}
