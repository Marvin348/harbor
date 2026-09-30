package com.harbor.server.features.tickets.communication.message.service;

import com.harbor.server.common.exception.ForbiddenException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.tickets.communication.message.dto.request.CreateTicketMessageRequest;
import com.harbor.server.features.tickets.communication.message.model.TicketMessage;
import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;
import com.harbor.server.features.tickets.communication.message.repository.TicketMessageRepository;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.tickets.service.TicketAccessService;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTicketMessage {

  private final CurrentUserProvider currentUserProvider;
  private final TicketMessageRepository ticketMessageRepository;
  private final TicketRepository ticketRepository;
  private final OrganizationRepository organizationRepository;
  private final UserRepository userRepository;
  private final TicketAccessService ticketAccessService;

  public void execute(Long ticketId, CreateTicketMessageRequest request) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    ticketAccessService.ensureCanAccessTicket(ticketId, userDetails);

    if (userDetails.getRole() == OrganizationRole.REQUESTER
        && request.type() != TicketMessageType.REPLY) {
      throw new ForbiddenException("Requester cannot create internal notes");
    }

    Ticket ticket = ticketRepository.getReferenceById(ticketId);
    Organization organization =
        organizationRepository.getReferenceById(userDetails.getOrganizationId());
    User author = userRepository.getReferenceById(userDetails.getId());

    TicketMessage message =
        new TicketMessage(ticket, organization, author, request.type(), request.body());

    ticketMessageRepository.save(message);
  }
}
