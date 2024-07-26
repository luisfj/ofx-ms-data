package dev.luisjohann.ofxmsdata.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.luisjohann.ofxmsdata.dto.SseMessageDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SenderQueueSse {

   final RabbitTemplate rabbitTemplate;

   @Value("${queue.routing-key.sse}")
   private String routingKeySse;

   @Value("${exchange.name}")
   private String exchangeName;

   public void send(SseMessageDTO message) {
      rabbitTemplate.convertAndSend(exchangeName, routingKeySse, message);
   }
}
