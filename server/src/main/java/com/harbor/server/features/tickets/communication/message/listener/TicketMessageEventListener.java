package com.harbor.server.features.tickets.communication.message.listener;

import com.harbor.server.features.tickets.communication.message.event.TicketMessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TicketMessageEventListener {

  private final SimpMessagingTemplate messagingTemplate;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onMessageCreated(TicketMessageCreatedEvent event) {
    messagingTemplate.convertAndSend("/topic/tickets." + event.ticketId(), "NEW_MESSAGE");
  }
}
