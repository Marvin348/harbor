package com.harbor.server.features.tickets.sla.dto.response;

import com.harbor.server.features.tickets.model.TicketPriority;
import jakarta.validation.constraints.NotNull;

public record SlaPolicyResponse(
    @NotNull Long id,
    @NotNull String name,
    @NotNull TicketPriority ticketPriority,
    @NotNull int responseTimeMinutes,
    @NotNull int resolutionTimeMinutes,
    @NotNull boolean enabled) {}
