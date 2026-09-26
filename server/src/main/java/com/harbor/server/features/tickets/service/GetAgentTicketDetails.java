package com.harbor.server.features.tickets.service;

import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.tickets.dto.response.AgentTicketDetailsResponse;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.user.model.OrganizationRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAgentTicketDetails {

  private final CurrentUserProvider currentUserProvider;
  private final TicketRepository ticketRepository;

  public AgentTicketDetailsResponse execute(Long id) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    return ticketRepository
        .findAgentTicketDetails(
            id,
            userDetails.getOrganizationId(),
            userDetails.getId(),
            userDetails.getRole() == OrganizationRole.ORGANIZATION_ADMIN)
        .orElseThrow(() -> new NotFoundException("Ticket Not Found"));
  }
}
