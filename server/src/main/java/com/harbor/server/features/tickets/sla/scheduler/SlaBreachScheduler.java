package com.harbor.server.features.tickets.sla.scheduler;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.messaging.SlaBreachProducer;
import com.harbor.server.features.tickets.sla.model.SlaBreachType;
import com.harbor.server.features.tickets.sla.projection.SlaBreachCandidate;
import com.harbor.server.features.tickets.sla.repository.SlaBreachProcessingRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SlaBreachScheduler {
  private final TicketSlaRepository ticketSlaRepository;
  private final SlaBreachProducer slaBreachProducer;
  private final SlaBreachProcessingRepository slaBreachProcessingRepository;

  @Scheduled(fixedRate = 300000)
  void enqueueBreachCandidates() {
    LocalDateTime now = LocalDateTime.now();

    enqueueCandidatesPaginated(SlaBreachType.RESPONSE, now);
    enqueueCandidatesPaginated(SlaBreachType.RESOLUTION, now);
  }

  private void enqueueCandidatesPaginated(SlaBreachType breachType, LocalDateTime now) {
    Long lastId = 0L;

    while (true) {
      List<SlaBreachCandidate> candidates =
          breachType == SlaBreachType.RESPONSE
              ? ticketSlaRepository.findResponseBreachCandidateIds(now, lastId)
              : ticketSlaRepository.findResolutionBreachCandidateIds(now, lastId);

      if (candidates.isEmpty()) {
        break;
      }

      enqueueCandidates(candidates, breachType, now);

      lastId = candidates.getLast().ticketSlaId();
    }
  }

  private void enqueueCandidates(
      List<SlaBreachCandidate> candidates, SlaBreachType breachType, LocalDateTime now) {
    for (SlaBreachCandidate candidate : candidates) {

      Optional<Long> processingId =
          slaBreachProcessingRepository.createIfAbsent(
              candidate.organizationId(), candidate.ticketSlaId(), breachType, now);

      if (processingId.isEmpty()) {
        continue;
      }

      slaBreachProducer.send(new SlaBreachJob(processingId.get()));

      int updatedRows = slaBreachProcessingRepository.markQueued(processingId.get(), now);

      if (updatedRows != 1) {
        throw new IllegalStateException(
            "Expected exactly one SlaBreachProcessing to be marked as queued");
      }
    }
  }
}
