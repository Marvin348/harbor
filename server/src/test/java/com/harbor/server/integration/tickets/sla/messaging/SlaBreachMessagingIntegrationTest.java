package com.harbor.server.integration.tickets.sla.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.messaging.SlaBreachProducer;
import com.harbor.server.features.tickets.sla.messaging.SlaMessagingConfig;
import com.harbor.server.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest(properties = "spring.rabbitmq.listener.simple.auto-startup=false")
class SlaBreachMessagingIntegrationTest extends AbstractIntegrationTest {

  @Autowired private SlaBreachProducer slaBreachProducer;
  @Autowired private RabbitTemplate rabbitTemplate;

  @Test
  void shouldRouteSlaBreachJobToConfiguredQueue() throws Exception {
    SlaBreachJob job = new SlaBreachJob(42L);

    slaBreachProducer.send(job);

    Message receivedMessage =
        rabbitTemplate.receive(SlaMessagingConfig.SLA_BREACH_QUEUE, 5_000);
    assertNotNull(receivedMessage);

    SlaBreachJob receivedJob =
        JsonMapper.shared().readValue(receivedMessage.getBody(), SlaBreachJob.class);

    assertEquals(job, receivedJob);
  }
}
