package com.harbor.server.features.tickets.sla.service;

import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.features.tickets.sla.dto.response.SlaPolicyResponse;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSlaPolicies {

  private final CurrentUserProvider currentUserProvider;
  private final SlaPolicyRepository slaPolicyRepository;

  public List<SlaPolicyResponse> execute() {
    Long organizationId = currentUserProvider.getCurrentOrganizationId();

    return slaPolicyRepository.findAllByOrganizationIdOrderByIdAsc(organizationId).stream()
        .map(
            policy ->
                new SlaPolicyResponse(
                    policy.getId(),
                    policy.getName(),
                    policy.getTicketPriority(),
                    policy.getResponseTimeMinutes(),
                    policy.getResolutionTimeMinutes(),
                    policy.isEnabled()))
        .toList();
  }
}
