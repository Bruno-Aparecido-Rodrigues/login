package io.github.fatec.repository;

import io.github.fatec.entity.User;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User update(User user);
    void delete(String id);
    List<User> findAll();
    User findByUsername(String username);
    User findById(String id);
}
