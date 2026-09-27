package com.harbor.server.features.tickets.agent.service;

import com.harbor.server.common.exception.ConflictException;
import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.tickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClaimTicket {

  private final CurrentUserProvider currentUserProvider;
  private final TicketRepository ticketRepository;

  @Transactional
  public void execute(Long id) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    boolean accessible =
        ticketRepository.existsAccessibleTicketForAgent(
            id, userDetails.getOrganizationId(), userDetails.getId());

    if (!accessible) {
      throw new NotFoundException("Ticket not found");
    }

    int updatedRows =
        ticketRepository.claimTicket(id, userDetails.getOrganizationId(), userDetails.getId());

    if (updatedRows == 0) {
      throw new ConflictException("Ticket could not be claimed");
    }
  }
}
