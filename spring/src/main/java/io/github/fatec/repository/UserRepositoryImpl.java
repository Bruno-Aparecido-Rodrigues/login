package io.github.fatec.repository;

import io.github.fatec.entity.User;
import io.github.fatec.repository.adapter.UserRepositoryImplAdapter;
import io.github.fatec.repository.client.UserRepositoryWithMongodb;
import io.github.fatec.repository.orm.UserOrm;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final PasswordEncoder encoder;
    private final UserRepositoryWithMongodb repository;

    public UserRepositoryImpl(
            PasswordEncoder encoder,
            UserRepositoryWithMongodb repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

@Override
public User save(User user) {
    try {
        UserOrm orm = repository.save(UserRepositoryImplAdapter.cast(user, encoder));
        return UserRepositoryImplAdapter.cast(orm, encoder);
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}

    @Override
    public User update(User user) {
        try {
            UserOrm current = repository.findById(user.id())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + user.id()));
            // senha vazia mantém a atual; senha nova é gravada com hash (antes ia em texto puro e o login quebrava)
            UserOrm orm = (user.password() == null || user.password().isBlank())
                    ? repository.save(UserRepositoryImplAdapter.cast(new User(user.id(), user.username(),
                            current.password(), user.email(), user.cep(), user.roles())))
                    : repository.save(UserRepositoryImplAdapter.cast(user, encoder));
            return UserRepositoryImplAdapter.cast(orm, encoder);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(String id) {
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            return repository.findAll().stream()
                    .map(orm -> UserRepositoryImplAdapter.cast(orm, encoder))
                    .toList();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            Optional<UserOrm> optional = repository.findByUsername(username);
            if (optional.isEmpty()) {
                throw new UsernameNotFoundException("Usuário não encontrado");
            }
            return UserRepositoryImplAdapter.cast(optional.get(), encoder);
        } catch (UsernameNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public User findById(String id) {
        try {
            Optional<UserOrm> optional = repository.findById(id);
            if (optional.isEmpty()) {
                throw new RuntimeException("Usuário não encontrado: " + id);
            }
            return UserRepositoryImplAdapter.cast(optional.get(), encoder);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
