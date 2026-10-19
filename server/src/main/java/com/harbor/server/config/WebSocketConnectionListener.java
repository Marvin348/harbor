package com.harbor.server.config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;

@Component
public class WebSocketConnectionListener {

  @EventListener
  public void handleSessionConnected(SessionConnectedEvent event) {
    Principal principal = event.getUser();

    if (principal == null) {
      System.out.println("WebSocket connected: ANONYMOUS");
      return;
    }

    System.out.println("WebSocket connected: " + principal.getName());
  }
}
