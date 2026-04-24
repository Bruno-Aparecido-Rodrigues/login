package io.github.fatec.repository;

import io.github.fatec.entity.Login;
import io.github.fatec.repository.adapter.LoginRepositoryAdapter;
import io.github.fatec.repository.mongo.LoginRepositoryMongo;
import io.github.fatec.repository.orm.LoginOrmMongo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LoginRepositoryImpl implements LoginRepository {

    private final LoginRepositoryMongo repository;

    public LoginRepositoryImpl(LoginRepositoryMongo repository) {
        this.repository = repository;
    }

    @Override
    public Login salvar(Login login) {
        LoginOrmMongo orm = LoginRepositoryAdapter.castEntity(login);
        LoginOrmMongo saved = repository.save(orm);
        return LoginRepositoryAdapter.castOrm(saved);
    }

    @Override
    public Login atualizar(Login login) {
        LoginOrmMongo orm = LoginRepositoryAdapter.castEntity(login);
        LoginOrmMongo updated = repository.save(orm);
        return LoginRepositoryAdapter.castOrm(updated);
    }

    @Override
    public void deletar(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Login> listar() {
        return repository.findAll()
                .stream()
                .map(LoginRepositoryAdapter::castOrm)
                .collect(Collectors.toList());
    }
}
