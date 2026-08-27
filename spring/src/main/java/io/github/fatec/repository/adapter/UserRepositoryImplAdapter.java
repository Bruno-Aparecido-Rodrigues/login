package io.github.fatec.repository.adapter;

import io.github.fatec.entity.User;
import io.github.fatec.repository.orm.UserOrm;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRepositoryImplAdapter {
    private UserRepositoryImplAdapter() {
    }

    public static User cast(UserOrm orm, PasswordEncoder passwordEncoder) {
        return new User(
                orm.id(),
                orm.username(),
                orm.password(),
                orm.email(),
                orm.cep(),
                orm.roles());
    }

    public static UserOrm cast(User user, PasswordEncoder passwordEncoder) {
        return new UserOrm(
                user.id(),
                user.username(),
                passwordEncoder.encode(user.password()),
                user.email(),
                user.cep(),
                user.roles());
    }


    public static UserOrm cast(User user) {
        return new UserOrm(
                user.id(),
                user.username(),
                user.password(),
                user.email(),
                user.cep(),
                user.roles());
    }
}