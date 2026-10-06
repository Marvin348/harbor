package com.harbor.server.features.tickets.sla.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlaBreachProducer {

  private final RabbitTemplate rabbitTemplate;

//  queue.add("send-email", payload);
  public void send(SlaBreachJob job) {
    rabbitTemplate.convertAndSend(
        SlaMessagingConfig.SLA_EXCHANGE, SlaMessagingConfig.SLA_BREACH_ROUTING_KEY, job);
  }
}
