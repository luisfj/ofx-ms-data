package dev.luisjohann.ofxmsdata.repository.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import dev.luisjohann.ofxmsdata.model.User;
import reactor.core.publisher.Flux;

@Repository
// public interface UserRepository extends R2dbcRepository<User, Long> {
public interface UserRepositoryAsync extends R2dbcRepository<User, String> {

   Flux<User> findByNameContaining(String name);
}
