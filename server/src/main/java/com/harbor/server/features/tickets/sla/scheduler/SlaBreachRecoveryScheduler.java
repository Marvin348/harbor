package com.harbor.server.features.tickets.sla.scheduler;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.messaging.SlaBreachProducer;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessing;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessingStatus;
import com.harbor.server.features.tickets.sla.repository.SlaBreachProcessingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SlaBreachRecoveryScheduler {
  private final SlaBreachProcessingRepository slaBreachProcessingRepository;
  private final SlaBreachProducer slaBreachProducer;

  @Scheduled(fixedRate = 300000)
  void processPendingSlaBreaches() {
    LocalDateTime now = LocalDateTime.now();

    List<SlaBreachProcessing> breaches =
        slaBreachProcessingRepository.findRecoverable(
            SlaBreachProcessingStatus.PENDING, now.minusMinutes(2));

    if (breaches.isEmpty()) return;

    for (SlaBreachProcessing breach : breaches) {
      slaBreachProducer.send(new SlaBreachJob(breach.getId()));

      slaBreachProcessingRepository.markQueued(breach.getId(), now);
    }
  }
}
