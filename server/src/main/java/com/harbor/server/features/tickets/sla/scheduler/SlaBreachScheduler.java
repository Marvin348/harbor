package com.harbor.server.features.tickets.sla.scheduler;

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

  @Scheduled(fixedRate = 300000)
  void enqueueResponseBreachCandidates() {
    List<Long> responseBreachCandidateIds =
        ticketSlaRepository.findOverdueResponseSlaIds(LocalDateTime.now());

    System.out.println(responseBreachCandidateIds);
  }
}
