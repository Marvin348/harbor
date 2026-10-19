package com.harbor.server.features.tickets.messages;

import com.harbor.server.features.tickets.communication.message.event.TicketMessageCreatedEvent;
import com.harbor.server.features.tickets.communication.message.listener.TicketMessageEventListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicketMessageEventListenerTest {

  @Mock private SimpMessagingTemplate messagingTemplate;
  @InjectMocks private TicketMessageEventListener ticketMessageEventListener;

  @Test
  void shouldSendNewMessageToTicketTopic() {
    TicketMessageCreatedEvent event = new TicketMessageCreatedEvent(345L);

    ticketMessageEventListener.onMessageCreated(event);

    verify(messagingTemplate).convertAndSend("/topic/tickets.345", "NEW_MESSAGE");
  }
}
