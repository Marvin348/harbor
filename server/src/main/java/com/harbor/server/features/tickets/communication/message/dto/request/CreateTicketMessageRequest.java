package com.harbor.server.features.tickets.communication.message.dto.request;

import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketMessageRequest(
    @NotNull(message = "Message type is required") TicketMessageType type,
    @NotBlank(message = "Message body must not be blank") String body) {}
