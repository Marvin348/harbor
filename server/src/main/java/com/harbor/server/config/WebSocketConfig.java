package com.harbor.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final TicketSubscriptionInterceptor ticketSubscriptionInterceptor;

  @Value("${app.client-url}")
  private String clientUrl;

  @Value("${spring.rabbitmq.host}")
  private String rabbitmqHost;

  @Value("${app.rabbitmq.stomp.port}")
  private int stompPort;

  @Value("${spring.rabbitmq.username}")
  private String rabbitmqUser;

  @Value("${spring.rabbitmq.password}")
  private String rabbitmqPassword;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOrigins(clientUrl);
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry
        .enableStompBrokerRelay("/topic")
        .setRelayHost(rabbitmqHost)
        .setRelayPort(stompPort)
        .setClientLogin(rabbitmqUser)
        .setClientPasscode(rabbitmqPassword)
        .setSystemLogin(rabbitmqUser)
        .setSystemPasscode(rabbitmqPassword);

    registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(ticketSubscriptionInterceptor);
  }
}
