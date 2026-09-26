package com.harbor.server.features.tickets.dto.response;

import com.harbor.server.features.tickets.model.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgentTicketDetailsResponse(
    @NotNull Long id,
    @NotNull String subject,
    @NotNull String description,
    @NotNull TicketStatus status,
    @NotNull TicketPriority priority,
    @NotNull TicketImpact impact,
    @NotNull TicketUrgency urgency,
    @NotNull TicketBusinessCriticality businessCriticality,
    @NotNull Long requesterId,
    @NotNull String requesterName,
    @NotNull String requesterEmail,
    @NotNull Long serviceId,
    @NotNull String serviceName,
    @NotNull Long serviceTeamId,
    @NotNull String serviceTeamName,
    String assignedAgentName,
    @NotNull LocalDateTime createdAt,
    @NotNull LocalDateTime updatedAt) {}
