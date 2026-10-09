package com.harbor.server.features.tickets.sla.repository;

import com.harbor.server.features.tickets.sla.model.SlaBreachType;
import com.harbor.server.features.tickets.sla.projection.SlaBreachProcessingData;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SlaBreachProcessingRepositoryCustom {

  Optional<Long> createIfAbsent(
      Long organizationId, Long ticketSlaId, SlaBreachType breachType, LocalDateTime now);

  Optional<SlaBreachProcessingData> claimForProcessing(Long processingId, LocalDateTime now);
}
