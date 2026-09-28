package com.harbor.server.features.tickets.agent.service;

import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.tickets.agent.dto.response.AgentTicketHeaderResponse;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.user.model.OrganizationRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAgentTicketHeader {

  private final CurrentUserProvider currentUserProvider;
  private final TicketRepository ticketRepository;

  public AgentTicketHeaderResponse execute(Long id) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    return ticketRepository
        .findAgentTicketHeader(
            id,
            userDetails.getOrganizationId(),
            userDetails.getId(),
            userDetails.getRole() == OrganizationRole.ORGANIZATION_ADMIN)
        .orElseThrow(() -> new NotFoundException("Ticket not found"));
  }
}
