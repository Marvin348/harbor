package com.harbor.server.features.tickets.dto.request;

import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetServiceTeamTicketsQuery(
    String search,
    TicketStatus status,
    TicketPriority priority,
    @Min(1) int page,
    @Min(1) @Max(100) int limit) {}
