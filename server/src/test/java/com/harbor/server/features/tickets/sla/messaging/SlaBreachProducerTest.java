package com.harbor.server.features.tickets.sla.messaging;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class SlaBreachProducerTest {

  @Mock private RabbitTemplate rabbitTemplate;
  @InjectMocks private SlaBreachProducer slaBreachProducer;

  @Test
  void shouldSendJobToSlaExchangeWithBreachRoutingKey() {
    SlaBreachJob job = new SlaBreachJob(42L);

    slaBreachProducer.send(job);

    verify(rabbitTemplate)
        .convertAndSend(
            SlaMessagingConfig.SLA_EXCHANGE,
            SlaMessagingConfig.SLA_BREACH_ROUTING_KEY,
            job);
  }
}
