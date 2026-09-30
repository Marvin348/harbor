package com.harbor.server.features.tickets.communication.message.controller;

import com.harbor.server.features.tickets.communication.message.dto.request.CreateTicketMessageRequest;
import com.harbor.server.features.tickets.communication.message.dto.response.TicketMessageResponse;
import com.harbor.server.features.tickets.communication.message.service.CreateTicketMessage;
import com.harbor.server.features.tickets.communication.message.service.GetTicketMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketMessageController {

  private final GetTicketMessages getTicketMessages;
  private final CreateTicketMessage createTicketMessage;

  @GetMapping("/{id}/messages")
  public List<TicketMessageResponse> getTicketMessages(@PathVariable @Positive Long id) {
    return getTicketMessages.execute(id);
  }

  @PostMapping("/{id}/messages")
  @ResponseStatus(HttpStatus.CREATED)
  public void createTicketMessage(
      @PathVariable @Positive Long id, @Valid @RequestBody CreateTicketMessageRequest body) {
    createTicketMessage.execute(id, body);
  }
}
