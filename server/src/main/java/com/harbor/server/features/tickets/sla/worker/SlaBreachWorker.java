package com.harbor.server.features.tickets.sla.worker;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.messaging.SlaMessagingConfig;
import com.harbor.server.features.tickets.sla.projection.SlaBreachProcessingData;
import com.harbor.server.features.tickets.sla.repository.SlaBreachProcessingRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SlaBreachWorker {

  private final TicketSlaRepository ticketSlaRepository;
  private final SlaBreachProcessingRepository slaBreachProcessingRepository;

  @Transactional
  @RabbitListener(queues = SlaMessagingConfig.SLA_BREACH_QUEUE)
  public void process(SlaBreachJob job) {
    LocalDateTime now = LocalDateTime.now();

    Optional<SlaBreachProcessingData> data =
        slaBreachProcessingRepository.claimForProcessing(job.slaBreachProcessingId(), now);

    if (data.isEmpty()) {
      return;
    }

    switch (data.get().breachType()) {
      case RESPONSE -> ticketSlaRepository.markResponseBreached(data.get().ticketSlaId(), now);
      case RESOLUTION -> ticketSlaRepository.markResolutionBreached(data.get().ticketSlaId(), now);
    }

    int completedRows =
        slaBreachProcessingRepository.markCompleted(job.slaBreachProcessingId(), now);

    if (completedRows != 1) {
      throw new IllegalStateException("Expected exactly one SlaBreachProcessing to be completed");
    }
  }
}
