package dev.luisjohann.ofxmsdata.queue;

import dev.luisjohann.ofxmsdata.dto.SseUeChangeMessageDTO;
import dev.luisjohann.ofxmsdata.model.UnidadeEconomica;
import dev.luisjohann.ofxmsdata.model.User;
import dev.luisjohann.ofxmsdata.repository.r2dbc.UnidadeEconomicaRepositoryAsync;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UeChangeQueueConsumer {

    final UnidadeEconomicaRepositoryAsync repository;

    @RabbitListener(queues = {"${queue.name.ue-change}"})
    public void receive(SseUeChangeMessageDTO messageDTO) throws InterruptedException {
        log.info("--- Receive event to save UE {}, name:{}, status:{}", messageDTO.id(), messageDTO.name(), messageDTO.status());

        repository.findById(messageDTO.id())
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalUe -> repository.save(
                        new UnidadeEconomica(messageDTO.id(), messageDTO.name(), messageDTO.status(), !optionalUe.isPresent()))
                )
                .doOnError((error) -> log.error("Erro ao salvar UE: "+error.getMessage(), error))
                .doOnSuccess((ue) -> log.info("UE Salva {} : {} : {}", ue.getId(), ue.getName(), ue.getStatus()))
                .subscribe();
    }
}