package com.harbor.server.features.tickets.communication.message.service;

import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.tickets.communication.message.dto.response.TicketMessageResponse;
import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;
import com.harbor.server.features.tickets.communication.message.repository.TicketMessageRepository;
import com.harbor.server.features.tickets.service.TicketAccessService;
import com.harbor.server.features.user.model.OrganizationRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTicketMessages {

  private final CurrentUserProvider currentUserProvider;
  private final TicketMessageRepository ticketMessageRepository;
  private final TicketAccessService ticketAccessService;

  public List<TicketMessageResponse> execute(Long ticketId) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    ticketAccessService.ensureCanAccessTicket(ticketId, userDetails);

    if (userDetails.getRole() == OrganizationRole.REQUESTER) {
      return ticketMessageRepository.findTicketMessagesByTicketIdAndOrganizationIdAndType(
          ticketId, userDetails.getOrganizationId(), TicketMessageType.REPLY);
    }

    return ticketMessageRepository.findTicketMessagesByTicketIdAndOrganizationId(
        ticketId, userDetails.getOrganizationId());
  }
}
