package com.harbor.server.features.tickets.dto.response;

import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TicketResponse(
    @NotNull Long id,
    @NotNull String subject,
    @NotNull TicketStatus status,
    TicketPriority priority,
    @NotNull LocalDateTime createdAt) {}
