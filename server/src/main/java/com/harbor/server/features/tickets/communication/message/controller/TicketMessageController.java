package com.harbor.server.features.tickets.communication.message.controller;

import com.harbor.server.features.tickets.communication.message.dto.response.TicketMessageResponse;
import com.harbor.server.features.tickets.communication.message.service.GetTicketMessages;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketMessageController {

  private final GetTicketMessages getTicketMessages;

  @GetMapping("/{id}/messages")
  public List<TicketMessageResponse> getTicketMessages(@PathVariable @Positive Long id) {
    return getTicketMessages.execute(id);
  }
}
