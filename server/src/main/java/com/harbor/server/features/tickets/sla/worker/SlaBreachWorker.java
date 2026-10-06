package com.harbor.server.features.tickets.sla.worker;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.messaging.SlaMessagingConfig;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SlaBreachWorker {

  private final TicketSlaRepository ticketSlaRepository;

  @Transactional
  @RabbitListener(queues = SlaMessagingConfig.SLA_BREACH_QUEUE)
  public void process(SlaBreachJob job) {
    LocalDateTime now = LocalDateTime.now();

    int updatedRows =
        switch (job.slaBreachType()) {
          case RESPONSE -> ticketSlaRepository.markResponseBreached(job.ticketSlaId(), now);
          case RESOLUTION -> ticketSlaRepository.markResolutionBreached(job.ticketSlaId(), now);
        };

    if (updatedRows == 0) {
      return;
    }
  }
}
