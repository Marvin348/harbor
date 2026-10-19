package com.harbor.server.features.tickets.messages.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.harbor.server.common.exception.ForbiddenException;
import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.tickets.communication.message.dto.request.CreateTicketMessageRequest;
import com.harbor.server.features.tickets.communication.message.event.TicketMessageCreatedEvent;
import com.harbor.server.features.tickets.communication.message.model.TicketMessage;
import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;
import com.harbor.server.features.tickets.communication.message.repository.TicketMessageRepository;
import com.harbor.server.features.tickets.communication.message.service.CreateTicketMessage;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.tickets.service.TicketAccessService;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class CreateTicketMessageServiceTest {

  private static final long TICKET_ID = 1L;
  private static final long ORGANIZATION_ID = 7L;
  private static final long USER_ID = 42L;

  @Mock private CurrentUserProvider currentUserProvider;
  @Mock private TicketMessageRepository ticketMessageRepository;
  @Mock private TicketRepository ticketRepository;
  @Mock private OrganizationRepository organizationRepository;
  @Mock private UserRepository userRepository;
  @Mock private TicketAccessService ticketAccessService;
  @Mock private ApplicationEventPublisher eventPublisher;
  @Mock private CustomUserDetails userDetails;
  @Mock private Ticket ticket;
  @Mock private Organization organization;
  @Mock private User author;

  @InjectMocks private CreateTicketMessage createTicketMessage;

  @Test
  void shouldSaveMessageAndPublishCreatedEvent() {
    CreateTicketMessageRequest request =
        new CreateTicketMessageRequest(TicketMessageType.INTERNAL_NOTE, "TEST");
    givenAuthorizedUser(OrganizationRole.AGENT);

    createTicketMessage.execute(TICKET_ID, request);

    verify(ticketAccessService).ensureCanAccessTicket(TICKET_ID, userDetails);

    ArgumentCaptor<TicketMessage> messageCaptor = ArgumentCaptor.forClass(TicketMessage.class);
    InOrder saveAndPublishOrder = inOrder(ticketMessageRepository, eventPublisher);
    saveAndPublishOrder.verify(ticketMessageRepository).save(messageCaptor.capture());
    saveAndPublishOrder
        .verify(eventPublisher)
        .publishEvent(new TicketMessageCreatedEvent(TICKET_ID));

    TicketMessage savedMessage = messageCaptor.getValue();
    assertSame(ticket, savedMessage.getTicket());
    assertSame(organization, savedMessage.getOrganization());
    assertSame(author, savedMessage.getAuthor());
    assertEquals(TicketMessageType.INTERNAL_NOTE, savedMessage.getType());
    assertEquals("TEST", savedMessage.getBody());
  }

  @Test
  void shouldRejectInternalNoteFromRequesterWithoutSavingOrPublishingEvent() {
    CreateTicketMessageRequest request =
        new CreateTicketMessageRequest(TicketMessageType.INTERNAL_NOTE, "TEST");
    when(currentUserProvider.getCurrentUser()).thenReturn(userDetails);
    when(userDetails.getRole()).thenReturn(OrganizationRole.REQUESTER);

    ForbiddenException exception =
        assertThrows(
            ForbiddenException.class, () -> createTicketMessage.execute(TICKET_ID, request));

    assertEquals("Requester cannot create internal notes", exception.getMessage());
    verify(ticketAccessService).ensureCanAccessTicket(TICKET_ID, userDetails);
    verifyNoInteractions(
        ticketMessageRepository,
        ticketRepository,
        organizationRepository,
        userRepository,
        eventPublisher);
  }

  @Test
  void shouldNotSaveOrPublishEventWhenTicketAccessIsDenied() {
    CreateTicketMessageRequest request =
        new CreateTicketMessageRequest(TicketMessageType.REPLY, "TEST");
    NotFoundException accessException = new NotFoundException("Ticket not found");
    when(currentUserProvider.getCurrentUser()).thenReturn(userDetails);
    doThrow(accessException)
        .when(ticketAccessService)
        .ensureCanAccessTicket(TICKET_ID, userDetails);

    NotFoundException thrownException =
        assertThrows(
            NotFoundException.class, () -> createTicketMessage.execute(TICKET_ID, request));

    assertSame(accessException, thrownException);
    verifyNoInteractions(
        ticketMessageRepository,
        ticketRepository,
        organizationRepository,
        userRepository,
        eventPublisher);
  }

  @Test
  void shouldNotPublishEventWhenSavingMessageFails() {
    CreateTicketMessageRequest request =
        new CreateTicketMessageRequest(TicketMessageType.REPLY, "TEST");
    IllegalStateException persistenceException = new IllegalStateException("Save failed");
    givenAuthorizedUser(OrganizationRole.AGENT);
    when(ticketMessageRepository.save(any(TicketMessage.class))).thenThrow(persistenceException);

    IllegalStateException thrownException =
        assertThrows(
            IllegalStateException.class, () -> createTicketMessage.execute(TICKET_ID, request));

    assertSame(persistenceException, thrownException);
    verifyNoInteractions(eventPublisher);
  }

  private void givenAuthorizedUser(OrganizationRole role) {
    when(currentUserProvider.getCurrentUser()).thenReturn(userDetails);
    when(userDetails.getRole()).thenReturn(role);
    when(userDetails.getOrganizationId()).thenReturn(ORGANIZATION_ID);
    when(userDetails.getId()).thenReturn(USER_ID);
    when(ticketRepository.getReferenceById(TICKET_ID)).thenReturn(ticket);
    when(organizationRepository.getReferenceById(ORGANIZATION_ID)).thenReturn(organization);
    when(userRepository.getReferenceById(USER_ID)).thenReturn(author);
  }
}
