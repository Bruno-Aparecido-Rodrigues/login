package io.github.fatec.repository.mongo;

import io.github.fatec.repository.orm.LoginOrmMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginRepositoryMongo extends MongoRepository<LoginOrmMongo, String> {
}
