package com.harbor.server.features.tickets.sla.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.amqp.autoconfigure.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlaMessagingConfig {

  public static final String SLA_EXCHANGE = "harbor.sla";
  public static final String SLA_BREACH_QUEUE = "sla.breach";
  public static final String SLA_BREACH_ROUTING_KEY = "sla.breach";

  // DEAD LETTER
  public static final String SLA_DEAD_LETTER_EXCHANGE = "harbor.sla.dlx";
  public static final String SLA_BREACH_DLQ = "sla.breach.dlq";
  public static final String SLA_BREACH_DLQ_ROUTING_KEY = "sla.breach.dlq";

  @Bean
  public DirectExchange slaExchange() {
    return new DirectExchange(SLA_EXCHANGE, true, false);
  }

  @Bean
  public Queue slaBreachQueue() {
    return QueueBuilder.durable(SLA_BREACH_QUEUE)
        .deadLetterExchange(SLA_DEAD_LETTER_EXCHANGE)
        .deadLetterRoutingKey(SLA_BREACH_DLQ_ROUTING_KEY)
        .build();
  }

  @Bean
  public Binding slaBreachBinding(Queue slaBreachQueue, DirectExchange slaExchange) {
    return BindingBuilder.bind(slaBreachQueue).to(slaExchange).with(SLA_BREACH_ROUTING_KEY);
  }

  @Bean
  public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public RabbitTemplateCustomizer rabbitTemplateCustomizer(JacksonJsonMessageConverter converter) {
    return rabbitTemplate -> rabbitTemplate.setMessageConverter(converter);
  }

  // DEAD LETTER
  @Bean
  public DirectExchange slaDeadLetterExchange() {
    return new DirectExchange(SLA_DEAD_LETTER_EXCHANGE, true, false);
  }

  @Bean
  public Queue slaDeadLetterQueue() {
    return QueueBuilder.durable(SLA_BREACH_DLQ).build();
  }

  @Bean
  public Binding slaBreachDeadLetterBinding(
      Queue slaDeadLetterQueue, DirectExchange slaDeadLetterExchange) {
    return BindingBuilder.bind(slaDeadLetterQueue)
        .to(slaDeadLetterExchange)
        .with(SLA_BREACH_DLQ_ROUTING_KEY);
  }
}
