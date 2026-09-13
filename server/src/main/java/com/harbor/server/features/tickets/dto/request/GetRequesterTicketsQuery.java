package com.harbor.server.features.tickets.dto.request;

import com.harbor.server.features.tickets.model.TicketStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetRequesterTicketsQuery(
    String search, TicketStatus status, @Min(1) int page, @Min(1) @Max(100) int limit) {}
