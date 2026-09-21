package com.harbor.server.features.tickets.service;

import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.repository.ServiceRepository;
import com.harbor.server.features.tickets.dto.request.CreateTicketRequest;
import com.harbor.server.features.tickets.dto.response.TicketResponse;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTicket {
  private final CurrentUserProvider currentUserProvider;
  private final OrganizationRepository organizationRepository;
  private final UserRepository userRepository;
  private final ServiceRepository serviceRepository;
  private final TicketRepository ticketRepository;
  private final TicketPriorityCalculator ticketPriorityCalculator;

  public TicketResponse execute(CreateTicketRequest request) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Long organizationId = userDetails.getOrganizationId();

    Organization organization =
        organizationRepository
            .findById(organizationId)
            .orElseThrow(() -> new NotFoundException("Organization not found"));

    com.harbor.server.features.services.model.Service service =
        serviceRepository
            .findByIdAndOrganizationId(request.serviceId(), organizationId)
            .orElseThrow(() -> new NotFoundException("Service not found"));

    ServiceTeam serviceTeam = service.getServiceTeam();

    User requester =
        userRepository
            .findByIdAndOrganizationId(userDetails.getId(), organizationId)
            .orElseThrow(() -> new NotFoundException("User not found"));

    String subject = request.subject().trim();
    String description = request.description().trim();

    TicketPriority priority = ticketPriorityCalculator.calculate(request.assessment());

    Ticket ticket =
        new Ticket(
            organization,
            serviceTeam,
            service,
            requester,
            subject,
            description,
            priority,
            request.assessment().impact(),
            request.assessment().urgency(),
            request.assessment().businessCriticality());

    ticketRepository.save(ticket);

    return new TicketResponse(
        ticket.getId(),
        ticket.getSubject(),
        ticket.getStatus(),
        ticket.getPriority(),
        ticket.getCreatedAt());
  }
}
