package io.github.fatec.repository.orm;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "logins")
public record LoginOrmMongo(
        @Id
        String id,
        String username,
        String password,
        List<String> roles
) {}
