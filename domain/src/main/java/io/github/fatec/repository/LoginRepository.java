package io.github.fatec.repository;

import io.github.fatec.entity.Login;

import java.util.List;

public interface LoginRepository {
    Login salvar(Login login);
    Login atualizar(Login login);
    void deletar(String id);
    List<Login> listar();
}
