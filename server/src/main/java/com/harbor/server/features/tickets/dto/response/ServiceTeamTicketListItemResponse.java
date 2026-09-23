package com.harbor.server.features.tickets.dto.response;

import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ServiceTeamTicketListItemResponse(
    @NotNull Long id,
    @NotNull String subject,
    @NotNull TicketPriority priority,
    @NotNull TicketStatus status,
    @NotNull String requesterName,
    @NotNull String serviceName,
    Long assignedAgentId,
    String assignedAgentName,
    @NotNull LocalDateTime createdAt) {}
