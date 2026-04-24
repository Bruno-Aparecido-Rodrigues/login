package io.github.fatec.service;

import io.github.fatec.entity.Login;

import java.util.List;

public interface LoginService {
    Login salvar(Login login);
    Login atualizar(Login login);
    void deletar(String id);
    List<Login> listar();
}
