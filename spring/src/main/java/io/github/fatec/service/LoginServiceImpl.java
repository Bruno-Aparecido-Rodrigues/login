package io.github.fatec.service;

import io.github.fatec.entity.Login;
import io.github.fatec.repository.LoginRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginRepository repository;

    public LoginServiceImpl(LoginRepository repository) {
        this.repository = repository;
    }

    @Override
    public Login salvar(Login login) {
        return repository.salvar(login);
    }

    @Override
    public Login atualizar(Login login) {
        return repository.atualizar(login);
    }

    @Override
    public void deletar(String id) {
        repository.deletar(id);
    }

    @Override
    public List<Login> listar() {
        return repository.listar();
    }
}
