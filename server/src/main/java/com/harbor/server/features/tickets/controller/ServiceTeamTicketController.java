package com.harbor.server.features.tickets.controller;

import com.harbor.server.common.dto.PageResponse;
import com.harbor.server.features.tickets.dto.request.GetServiceTeamTicketsQuery;
import com.harbor.server.features.tickets.dto.response.ServiceTeamTicketListItemResponse;
import com.harbor.server.features.tickets.service.GetServiceTeamTickets;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServiceTeamTicketController {
  private final GetServiceTeamTickets getServiceTeamTickets;

  @GetMapping("/service-teams/{id}/tickets")
  public PageResponse<ServiceTeamTicketListItemResponse> getServiceTeamTickets(
      @PathVariable @Positive Long id, @Valid GetServiceTeamTicketsQuery query) {
    return getServiceTeamTickets.execute(id, query);
  }
}
