package com.harbor.server.features.tickets.agent.controller;

import com.harbor.server.features.tickets.agent.dto.response.AgentTicketDetailsResponse;
import com.harbor.server.features.tickets.agent.dto.response.AgentTicketHeaderResponse;
import com.harbor.server.features.tickets.agent.service.ClaimTicket;
import com.harbor.server.features.tickets.agent.service.GetAgentTicketDetails;
import com.harbor.server.features.tickets.agent.service.GetAgentTicketHeader;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AgentTicketController {

  private final GetAgentTicketDetails getAgentTicketDetails;
  private final GetAgentTicketHeader getAgentTicketHeader;
  private final ClaimTicket claimTicket;

  @GetMapping("/tickets/{id}/agent")
  public AgentTicketDetailsResponse getAgentTicketDetails(@PathVariable @Positive Long id) {
    return getAgentTicketDetails.execute(id);
  }

  @PatchMapping("/tickets/{id}/claim")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void claimTicket(@PathVariable @Positive Long id) {
    claimTicket.execute(id);
  }

  @GetMapping("/tickets/{id}/header")
  public AgentTicketHeaderResponse getAgentTicketHeader(@PathVariable @Positive Long id) {
    return getAgentTicketHeader.execute(id);
  }
}
