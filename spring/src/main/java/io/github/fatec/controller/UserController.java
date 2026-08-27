package io.github.fatec.controller;

import io.github.fatec.controller.adapter.UserControllerAdapter;
import io.github.fatec.controller.request.UserRequest;
import io.github.fatec.controller.request.UserUpdateRequest;
import io.github.fatec.controller.response.UserResponse;
import io.github.fatec.entity.User;
import io.github.fatec.repository.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fatec/login")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/save")
    public UserResponse save(@RequestBody UserRequest request) {
        User saved = repository.save(UserControllerAdapter.cast(request));
        return toResponse(saved);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/user/update")
    public UserResponse update(@RequestBody UserUpdateRequest request) {
        User updated = repository.update(UserControllerAdapter.cast(request));
        return toResponse(updated);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/user/delete/{id}")
    public void delete(@PathVariable String id) {
        repository.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/all")
    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{id}")
    public UserResponse findById(@PathVariable String id) {
        User user = repository.findById(id);
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.id(),
                user.username(),
                user.email(),
                user.cep(),
                user.roles());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicate(DuplicateKeyException e) {
        return "Usuário já existe";
    }
}