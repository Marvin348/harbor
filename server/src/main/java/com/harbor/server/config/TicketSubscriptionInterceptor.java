package com.harbor.server.config;

import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.tickets.service.TicketAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketSubscriptionInterceptor implements ChannelInterceptor {

  private final TicketAccessService ticketAccessService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (accessor == null) {
      return message;
    }

    StompCommand command = accessor.getCommand();

    if (command == StompCommand.SEND) {
      throw new AccessDeniedException("STOMP SEND not allowed");
    }

    if (command != StompCommand.SUBSCRIBE) {
      return message;
    }

    String destination = accessor.getDestination();

    if (destination == null || !destination.matches("/topic/tickets\\.[0-9]+")) {
      throw new AccessDeniedException("Subscription not allowed");
    }

    Authentication authentication = (Authentication) accessor.getUser();

    if (authentication == null
        || !authentication.isAuthenticated()
        || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
      throw new AccessDeniedException("Unauthenticated");
    }

    Long ticketId = Long.valueOf(destination.substring("/topic/tickets.".length()));

    ticketAccessService.ensureCanAccessTicket(ticketId, user);

    return message;
  }
}
