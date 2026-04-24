package io.github.fatec.repository.adapter;

import io.github.fatec.entity.Login;
import io.github.fatec.repository.orm.LoginOrmMongo;

public class LoginRepositoryAdapter {

    public static LoginOrmMongo castEntity(Login login) {
        return new LoginOrmMongo(
                login.id(),
                login.username(),
                login.password(),
                login.roles()
        );
    }

    public static Login castOrm(LoginOrmMongo orm) {
        return new Login(
                orm.id(),
                orm.username(),
                orm.password(),
                orm.roles()
        );
    }
}
