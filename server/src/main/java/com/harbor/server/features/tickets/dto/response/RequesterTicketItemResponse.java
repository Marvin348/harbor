package com.harbor.server.features.tickets.dto.response;

import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequesterTicketItemResponse(
    @NotNull Long id,
    @NotNull String subject,
    @NotNull String serviceName,
    @NotNull TicketStatus status,

    @Schema(nullable = true) TicketPriority priority,
    @Schema(nullable = true) String assignedAgentName,

    @NotNull LocalDateTime createdAt,
    @NotNull LocalDateTime updatedAt) {}
