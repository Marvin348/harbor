package com.harbor.server.features.tickets.agent.dto.response;

import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record AgentTicketHeaderResponse(
    @NotNull Long id,
    @NotNull String subject,
    @NotNull TicketStatus status,
    @NotNull TicketPriority priority,
    String assignedAgentName) {}
