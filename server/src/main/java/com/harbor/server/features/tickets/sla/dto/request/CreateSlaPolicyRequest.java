package com.harbor.server.features.tickets.sla.dto.request;

import com.harbor.server.features.tickets.model.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateSlaPolicyRequest(
    @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,
    @NotNull(message = "TicketPriority is required") TicketPriority ticketPriority,
    @NotNull @Positive Integer responseTimeMinutes,
    @NotNull @Positive Integer resolutionTimeMinutes) {}
