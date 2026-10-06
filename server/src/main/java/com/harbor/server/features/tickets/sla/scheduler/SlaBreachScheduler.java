package com.harbor.server.features.tickets.sla.scheduler;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.messaging.SlaBreachProducer;
import com.harbor.server.features.tickets.sla.model.SlaBreachType;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SlaBreachScheduler {
  private final TicketSlaRepository ticketSlaRepository;
  private final SlaBreachProducer slaBreachProducer;

  @Scheduled(fixedRate = 300000)
  void enqueueBreachCandidates() {
    LocalDateTime now = LocalDateTime.now();

    enqueueCandidates(
        ticketSlaRepository.findResponseBreachCandidateIds(now), SlaBreachType.RESPONSE);

    enqueueCandidates(
        ticketSlaRepository.findResolutionBreachCandidateIds(now), SlaBreachType.RESOLUTION);
  }

  private void enqueueCandidates(List<Long> ids, SlaBreachType breachType) {
    for (Long id : ids) {
      slaBreachProducer.send(new SlaBreachJob(id, breachType));
    }
  }
}
