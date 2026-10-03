package com.harbor.server.features.tickets.sla.service;

import com.harbor.server.common.exception.BadRequestException;
import com.harbor.server.common.exception.ConflictException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.tickets.sla.dto.request.CreateSlaPolicyRequest;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSlaPolicy {

  private final CurrentUserProvider currentUserProvider;
  private final SlaPolicyRepository slaPolicyRepository;
  private final OrganizationRepository organizationRepository;

  public void execute(CreateSlaPolicyRequest request) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    if (slaPolicyRepository.existsByOrganizationIdAndTicketPriority(
        userDetails.getOrganizationId(), request.ticketPriority())) {
      throw new ConflictException("SLA policy for this priority already exists");
    }

    if (request.resolutionTimeMinutes() <= request.responseTimeMinutes()) {
      throw new BadRequestException("Resolution time must be greater than response time");
    }

    Organization organization =
        organizationRepository.getReferenceById(userDetails.getOrganizationId());

    String name = request.name().trim();

    SlaPolicy policy =
        new SlaPolicy(
            organization,
            name,
            request.ticketPriority(),
            request.responseTimeMinutes(),
            request.resolutionTimeMinutes());

    slaPolicyRepository.save(policy);
  }
}
