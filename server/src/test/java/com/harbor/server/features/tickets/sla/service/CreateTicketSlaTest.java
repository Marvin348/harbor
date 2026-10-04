package com.harbor.server.features.tickets.sla.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.model.TicketSla;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateTicketSlaTest {

  @Mock private SlaPolicyRepository slaPolicyRepository;
  @Mock private TicketSlaRepository ticketSlaRepository;
  @InjectMocks private CreateTicketSla createTicketSla;

  @Test
  void shouldCreateTicketSlaFromMatchingPolicy() {
    Organization organization = mock(Organization.class);
    Ticket ticket = mock(Ticket.class);
    SlaPolicy slaPolicy = mock(SlaPolicy.class);
    LocalDateTime ticketCreatedAt = LocalDateTime.of(2026, 10, 3, 9, 30);

    when(organization.getId()).thenReturn(42L);
    when(ticket.getId()).thenReturn(100L);
    when(ticket.getPriority()).thenReturn(TicketPriority.HIGH);
    when(ticket.getCreatedAt()).thenReturn(ticketCreatedAt);
    when(ticketSlaRepository.existsByTicketId(100L)).thenReturn(false);
    when(slaPolicyRepository.findByOrganizationIdAndTicketPriority(42L, TicketPriority.HIGH))
        .thenReturn(Optional.of(slaPolicy));
    when(slaPolicy.getResponseTimeMinutes()).thenReturn(30);
    when(slaPolicy.getResolutionTimeMinutes()).thenReturn(240);

    createTicketSla.execute(organization, ticket);

    ArgumentCaptor<TicketSla> ticketSlaCaptor = ArgumentCaptor.forClass(TicketSla.class);
    verify(ticketSlaRepository).save(ticketSlaCaptor.capture());

    TicketSla ticketSla = ticketSlaCaptor.getValue();
    assertSame(organization, ticketSla.getOrganization());
    assertSame(ticket, ticketSla.getTicket());
    assertSame(slaPolicy, ticketSla.getSlaPolicy());
    assertEquals(ticketCreatedAt.plusMinutes(30), ticketSla.getResponseDueAt());
    assertEquals(ticketCreatedAt.plusMinutes(240), ticketSla.getResolutionDueAt());
    verify(slaPolicyRepository)
        .findByOrganizationIdAndTicketPriority(42L, TicketPriority.HIGH);
  }

  @Test
  void shouldRejectTicketThatAlreadyHasSla() {
    Organization organization = mock(Organization.class);
    Ticket ticket = mock(Ticket.class);

    when(ticket.getId()).thenReturn(100L);
    when(ticketSlaRepository.existsByTicketId(100L)).thenReturn(true);

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class, () -> createTicketSla.execute(organization, ticket));

    assertEquals("Ticket already has an SLA", exception.getMessage());
    verifyNoInteractions(slaPolicyRepository);
    verify(ticketSlaRepository, never()).save(any(TicketSla.class));
  }

  @Test
  void shouldRejectTicketWhenMatchingPolicyDoesNotExist() {
    Organization organization = mock(Organization.class);
    Ticket ticket = mock(Ticket.class);

    when(organization.getId()).thenReturn(42L);
    when(ticket.getId()).thenReturn(100L);
    when(ticket.getPriority()).thenReturn(TicketPriority.HIGH);
    when(ticketSlaRepository.existsByTicketId(100L)).thenReturn(false);
    when(slaPolicyRepository.findByOrganizationIdAndTicketPriority(42L, TicketPriority.HIGH))
        .thenReturn(Optional.empty());

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class, () -> createTicketSla.execute(organization, ticket));

    assertEquals(
        "SLA-Policy for this organization and priority not found.", exception.getMessage());
    verify(ticketSlaRepository, never()).save(any(TicketSla.class));
  }
}
