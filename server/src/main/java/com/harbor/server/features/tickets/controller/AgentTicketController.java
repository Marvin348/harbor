package com.harbor.server.features.tickets.controller;

import com.harbor.server.features.tickets.dto.response.AgentTicketDetailsResponse;
import com.harbor.server.features.tickets.service.GetAgentTicketDetails;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgentTicketController {

  private final GetAgentTicketDetails getAgentTicketDetails;

  @GetMapping("/tickets/{id}/agent")
  public AgentTicketDetailsResponse getAgentTicketDetails(@PathVariable @Positive Long id) {
      return getAgentTicketDetails.execute(id);
  }
}
