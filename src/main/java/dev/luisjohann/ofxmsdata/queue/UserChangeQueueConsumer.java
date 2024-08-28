package dev.luisjohann.ofxmsdata.queue;

import dev.luisjohann.ofxmsdata.dto.SseUserChangeMessageDTO;
import dev.luisjohann.ofxmsdata.model.User;
import dev.luisjohann.ofxmsdata.repository.r2dbc.UserRepositoryAsync;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserChangeQueueConsumer {

    final UserRepositoryAsync repository;

    @RabbitListener(queues = {"${queue.name.user-change}"})
    public void receive(SseUserChangeMessageDTO messageDTO) throws InterruptedException {
        log.info("--- Receive event to save User {}, name:{}, status:{}", messageDTO.userId(), messageDTO.email(), messageDTO.name());

        repository.findById(messageDTO.userId())
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalUser -> repository.save(
                        new User(messageDTO.userId(), messageDTO.email(), messageDTO.name(), !optionalUser.isPresent()))
                )
                .doOnError((error) -> log.error("Erro ao salvar User: " + error.getMessage(), error))
                .doOnSuccess((user) -> log.info("User Salvo {} : {} : {}", user.getId(), user.getEmail(), user.getName()))
                .subscribe();
    }
}