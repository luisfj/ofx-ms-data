package dev.luisjohann.ofxmsdata.queue;

import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import dev.luisjohann.ofxmsdata.dto.SseImportedMessageDTO;
import dev.luisjohann.ofxmsdata.dto.SseMessageDTO;
import dev.luisjohann.ofxmsdata.service.ImportacaoService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImportedQueueConsumer {

   private static final Logger LOGGER = LoggerFactory.getLogger(ImportedQueueConsumer.class);

   final ImportacaoService importacaoService;
   final SenderQueueSse senderQueueSse;

   @RabbitListener(queues = { "${queue.name.imported}" })
   public void receive(SseImportedMessageDTO messageDTO) throws InterruptedException {
      LOGGER.info("Received message queue imported.queue");

      LOGGER.warn("--- IMPORTAR {} itens!!", messageDTO.operations().size());

      var task = importacaoService.importarOperacoesAsync(messageDTO.userId(), messageDTO.userImportedId(),
            messageDTO.ueId(), messageDTO.fileName(),
            messageDTO.operations());

      task.whenCompleteAsync((importacao, error) -> {
         if (Objects.nonNull(error)) {
            LOGGER.error(error.getMessage(), error);
            var message = new SseMessageDTO(messageDTO.userId(), "Save data Fail", error.getMessage(),
                  LocalDateTime.now());
            senderQueueSse.send(message);

         } else {
            LOGGER.info("-- Importacao salva com sucesso");

            var message = new SseMessageDTO(messageDTO.userId(), "Data saved successfully",
                  String.format("Importação de arquivo salva com sucesso! Importação com ID: %s", importacao.getId()),
                  LocalDateTime.now());

            senderQueueSse.send(message);
         }
      });

      LOGGER.warn("---xxx-x-x-x-- FIM METODO IMPORTAR itens!!", messageDTO.operations().size());
   }
}