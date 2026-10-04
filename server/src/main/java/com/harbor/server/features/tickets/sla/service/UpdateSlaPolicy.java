package com.harbor.server.features.tickets.sla.service;

import com.harbor.server.common.exception.BadRequestException;
import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.features.tickets.sla.dto.request.UpdateSlaPolicyRequest;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateSlaPolicy {

  private final CurrentUserProvider currentUserProvider;
  private final SlaPolicyRepository slaPolicyRepository;

  @Transactional
  public void execute(Long id, UpdateSlaPolicyRequest request) {
    Long organizationId = currentUserProvider.getCurrentOrganizationId();

    if (request.name() == null
        && request.responseTimeMinutes() == null
        && request.resolutionTimeMinutes() == null) {
      throw new BadRequestException("At least one field must be provided");
    }

    SlaPolicy slaPolicy =
        slaPolicyRepository
            .findByIdAndOrganizationId(id, organizationId)
            .orElseThrow(() -> new NotFoundException("SLA policy not found"));

    String name = request.name() == null ? slaPolicy.getName() : request.name().trim();

    int responseTimeMinutes =
        request.responseTimeMinutes() == null
            ? slaPolicy.getResponseTimeMinutes()
            : request.responseTimeMinutes();
    int resolutionTimeMinutes =
        request.resolutionTimeMinutes() == null
            ? slaPolicy.getResolutionTimeMinutes()
            : request.resolutionTimeMinutes();

    if (resolutionTimeMinutes <= responseTimeMinutes) {
      throw new BadRequestException("Resolution time must be greater than response time");
    }

    slaPolicy.updateConfiguration(name, responseTimeMinutes, resolutionTimeMinutes);
  }
}
