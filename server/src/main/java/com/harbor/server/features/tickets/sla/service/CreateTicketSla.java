package com.harbor.server.features.tickets.sla.service;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.model.TicketSla;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateTicketSla {

  private final SlaPolicyRepository slaPolicyRepository;
  private final TicketSlaRepository ticketSlaRepository;

  public void execute(Organization organization, Ticket ticket) {

    if (ticketSlaRepository.existsByTicketId(ticket.getId())) {
      throw new IllegalStateException("Ticket already has an SLA");
    }

    SlaPolicy slaPolicy =
        slaPolicyRepository
            .findByOrganizationIdAndTicketPriority(organization.getId(), ticket.getPriority())
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "SLA-Policy for this organization and priority not found."));

    LocalDateTime responseDueAt =
        ticket.getCreatedAt().plusMinutes(slaPolicy.getResponseTimeMinutes());

    LocalDateTime resolutionDueAt =
        ticket.getCreatedAt().plusMinutes(slaPolicy.getResolutionTimeMinutes());

    TicketSla ticketSla =
        new TicketSla(organization, ticket, slaPolicy, responseDueAt, resolutionDueAt);

    ticketSlaRepository.save(ticketSla);
  }
}
