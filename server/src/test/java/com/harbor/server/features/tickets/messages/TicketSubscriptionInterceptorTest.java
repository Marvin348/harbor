package com.harbor.server.features.tickets.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.config.TicketSubscriptionInterceptor;
import com.harbor.server.features.tickets.service.TicketAccessService;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class TicketSubscriptionInterceptorTest {

  @Mock private TicketAccessService ticketAccessService;
  @Mock private MessageChannel channel;
  @Mock private Authentication authentication;
  @Mock private CustomUserDetails userDetails;

  @InjectMocks private TicketSubscriptionInterceptor ticketSubscriptionInterceptor;

  @Test
  void shouldAllowSubscriptionWhenUserCanAccessTicket() {
    Message<byte[]> message =
        stompMessage(StompCommand.SUBSCRIBE, "/topic/tickets.42", authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);

    Message<?> result = ticketSubscriptionInterceptor.preSend(message, channel);

    assertSame(message, result);
    verify(ticketAccessService).ensureCanAccessTicket(42L, userDetails);
  }

  @Test
  void shouldRejectStompSend() {
    Message<byte[]> message = stompMessage(StompCommand.SEND, "/app/messages", authentication);

    AccessDeniedException exception =
        assertThrows(
            AccessDeniedException.class,
            () -> ticketSubscriptionInterceptor.preSend(message, channel));

    assertEquals("STOMP SEND not allowed", exception.getMessage());
    verifyNoInteractions(ticketAccessService);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(
      strings = {
        "/topic/tickets",
        "/topic/tickets.invalid",
        "/topic/tickets.-1",
        "/topic/tickets.42.messages",
        "/topic/other.42"
      })
  void shouldRejectSubscriptionToInvalidDestination(String destination) {
    Message<byte[]> message = stompMessage(StompCommand.SUBSCRIBE, destination, authentication);

    AccessDeniedException exception =
        assertThrows(
            AccessDeniedException.class,
            () -> ticketSubscriptionInterceptor.preSend(message, channel));

    assertEquals("Subscription not allowed", exception.getMessage());
    verifyNoInteractions(ticketAccessService);
  }

  @Test
  void shouldRejectSubscriptionWithoutUser() {
    Message<byte[]> message =
        stompMessage(StompCommand.SUBSCRIBE, "/topic/tickets.42", null);

    AccessDeniedException exception =
        assertThrows(
            AccessDeniedException.class,
            () -> ticketSubscriptionInterceptor.preSend(message, channel));

    assertEquals("Unauthenticated", exception.getMessage());
    verifyNoInteractions(ticketAccessService);
  }

  @Test
  void shouldRejectSubscriptionWithUnauthenticatedUser() {
    Message<byte[]> message =
        stompMessage(StompCommand.SUBSCRIBE, "/topic/tickets.42", authentication);
    when(authentication.isAuthenticated()).thenReturn(false);

    AccessDeniedException exception =
        assertThrows(
            AccessDeniedException.class,
            () -> ticketSubscriptionInterceptor.preSend(message, channel));

    assertEquals("Unauthenticated", exception.getMessage());
    verifyNoInteractions(ticketAccessService);
  }

  @Test
  void shouldRejectSubscriptionWithUnexpectedPrincipal() {
    Message<byte[]> message =
        stompMessage(StompCommand.SUBSCRIBE, "/topic/tickets.42", authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn("unexpected principal");

    AccessDeniedException exception =
        assertThrows(
            AccessDeniedException.class,
            () -> ticketSubscriptionInterceptor.preSend(message, channel));

    assertEquals("Unauthenticated", exception.getMessage());
    verifyNoInteractions(ticketAccessService);
  }

  @Test
  void shouldPropagateTicketAccessDenial() {
    Message<byte[]> message =
        stompMessage(StompCommand.SUBSCRIBE, "/topic/tickets.42", authentication);
    NotFoundException accessException = new NotFoundException("Ticket not found");
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    doThrow(accessException).when(ticketAccessService).ensureCanAccessTicket(42L, userDetails);

    NotFoundException thrownException =
        assertThrows(
            NotFoundException.class,
            () -> ticketSubscriptionInterceptor.preSend(message, channel));

    assertSame(accessException, thrownException);
  }

  @Test
  void shouldAllowNonSubscriptionFrame() {
    Message<byte[]> message = stompMessage(StompCommand.CONNECT, null, null);

    Message<?> result = ticketSubscriptionInterceptor.preSend(message, channel);

    assertSame(message, result);
    verifyNoInteractions(ticketAccessService);
  }

  @Test
  void shouldAllowMessageWithoutStompAccessor() {
    Message<byte[]> message = MessageBuilder.withPayload(new byte[0]).build();

    Message<?> result = ticketSubscriptionInterceptor.preSend(message, channel);

    assertSame(message, result);
    verifyNoInteractions(ticketAccessService);
  }

  private Message<byte[]> stompMessage(
      StompCommand command, String destination, Principal user) {
    StompHeaderAccessor accessor = StompHeaderAccessor.create(command);

    if (destination != null) {
      accessor.setDestination(destination);
    }

    if (user != null) {
      accessor.setUser(user);
    }

    return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
  }
}
