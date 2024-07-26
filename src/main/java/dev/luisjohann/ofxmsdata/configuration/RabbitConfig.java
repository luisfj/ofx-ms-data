package dev.luisjohann.ofxmsdata.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitConfig {

   @Value("${queue.name.sse}")
   private String queueNameSse;
   @Value("${queue.name.imported}")
   private String queueImported;

   @Value("${queue.routing-key.sse}")
   private String routingKeySse;
   @Value("${queue.routing-key.imported}")
   private String routingKeyImported;

   @Value("${exchange.name}")
   private String exchangeName;

   @Bean(name = "queueSse")
   public Queue queueSse() {
      return new Queue(queueNameSse, true);
   }

   @Bean(name = "queueImported")
   public Queue queueImported() {
      return new Queue(queueImported, true);
   }

   @Bean
   DirectExchange exchange() {
      return new DirectExchange(exchangeName);
   }

   @Bean
   Binding queueBinding(@Qualifier(value = "queueSse") Queue sseQueue, DirectExchange exchange) {
      return BindingBuilder.bind(sseQueue).to(exchange).with(routingKeySse);
   }

   @Bean
   Binding queueBindingImported(@Qualifier(value = "queueImported") Queue importedQueue, DirectExchange exchange) {
      return BindingBuilder.bind(importedQueue).to(exchange).with(routingKeyImported);
   }

   @Bean
   public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
      final var rabbitTemplate = new RabbitTemplate(connectionFactory);
      rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
      return rabbitTemplate;
   }

   @Bean
   public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
      return new Jackson2JsonMessageConverter();
   }

}
