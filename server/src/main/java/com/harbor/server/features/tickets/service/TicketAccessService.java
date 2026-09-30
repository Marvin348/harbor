package com.harbor.server.features.tickets.service;

import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.tickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketAccessService {
  private final TicketRepository ticketRepository;

  public void ensureCanAccessTicket(Long ticketId, CustomUserDetails user) {
    boolean accessible =
        switch (user.getRole()) {
          case REQUESTER ->
              ticketRepository.existsByIdAndOrganizationIdAndRequesterId(
                  ticketId, user.getOrganizationId(), user.getId());

          case AGENT ->
              ticketRepository.existsAccessibleTicketForAgent(
                  ticketId, user.getOrganizationId(), user.getId());

          case ORGANIZATION_ADMIN ->
              ticketRepository.existsByIdAndOrganizationId(ticketId, user.getOrganizationId());
        };

    if (!accessible) {
      throw new NotFoundException("Ticket not found");
    }
  }
}
