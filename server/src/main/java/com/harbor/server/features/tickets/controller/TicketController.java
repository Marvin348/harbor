package com.harbor.server.features.tickets.controller;

import com.harbor.server.features.tickets.dto.request.CreateTicketRequest;
import com.harbor.server.features.tickets.dto.request.GetRequesterTicketsQuery;
import com.harbor.server.features.tickets.dto.response.RequesterTicketItemResponse;
import com.harbor.server.features.tickets.dto.response.TicketResponse;
import com.harbor.server.features.tickets.service.CreateTicket;
import com.harbor.server.features.tickets.service.GetRequesterTickets;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

  private final CreateTicket createTicket;
  private final GetRequesterTickets getRequesterTickets;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TicketResponse createTicket(@Valid @RequestBody CreateTicketRequest body) {
    return createTicket.execute(body);
  }

  @GetMapping
  public Page<RequesterTicketItemResponse> getRequesterTickets(
      @Valid GetRequesterTicketsQuery queryParams) {
    return getRequesterTickets.execute(queryParams);
  }
}
