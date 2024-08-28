package dev.luisjohann.ofxmsdata.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.luisjohann.ofxmsdata.model.User;
import dev.luisjohann.ofxmsdata.repository.r2dbc.UserRepositoryAsync;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

   final UserRepositoryAsync repository;

   public Flux<User> findByNameContaining(String name) {
      return repository.findByNameContaining(name);
   }

   public Mono<User> update(String id, User user) {
      return repository.findById(id)
            .map(Optional::of)
            .defaultIfEmpty(Optional.empty())
            .flatMap(optionalTutorial -> {
               if (optionalTutorial.isPresent()) {
                  // user.setId(id);
                  var sUser = new User(id, user.getEmail(), user.getName(), false);
                  return repository.save(sUser);
                  // return repository.save(user);
               }

               return Mono.empty();
            });
   }
}
