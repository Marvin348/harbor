package com.harbor.server.features.tickets.communication.message.service;

import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.tickets.communication.message.dto.response.TicketMessageResponse;
import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;
import com.harbor.server.features.tickets.communication.message.repository.TicketMessageRepository;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.user.model.OrganizationRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTicketMessages {

  private final CurrentUserProvider currentUserProvider;
  private final TicketMessageRepository ticketMessageRepository;
  private final TicketRepository ticketRepository;

  public List<TicketMessageResponse> execute(Long ticketId) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    ensureTicketAccess(ticketId, userDetails);

    if (userDetails.getRole() == OrganizationRole.REQUESTER) {
      return ticketMessageRepository.findTicketMessagesByTicketIdAndOrganizationIdAndType(
          ticketId, userDetails.getOrganizationId(), TicketMessageType.REPLY);
    }

    return ticketMessageRepository.findTicketMessagesByTicketIdAndOrganizationId(
        ticketId, userDetails.getOrganizationId());
  }

  private void ensureTicketAccess(Long ticketId, CustomUserDetails userDetails) {
    switch (userDetails.getRole()) {
      case REQUESTER -> {
        boolean accessible =
            ticketRepository.existsByIdAndOrganizationIdAndRequesterId(
                ticketId, userDetails.getOrganizationId(), userDetails.getId());

        if (!accessible) {
          throw new NotFoundException("Ticket Not Found");
        }
      }
      case AGENT -> {
        boolean accessible =
            ticketRepository.existsAccessibleTicketForAgent(
                ticketId, userDetails.getOrganizationId(), userDetails.getId());

        if (!accessible) {
          throw new NotFoundException("Ticket Not Found");
        }
      }
      case ORGANIZATION_ADMIN -> {
        boolean accessible =
            ticketRepository.existsByIdAndOrganizationId(ticketId, userDetails.getOrganizationId());

        if (!accessible) {
          throw new NotFoundException("Ticket not found");
        }
      }
    }
  }
}
