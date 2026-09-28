package com.harbor.server.features.tickets.communication.message.dto.response;

import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;
import com.harbor.server.features.user.model.OrganizationRole;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TicketMessageResponse(
    @NotNull Long id,
    @NotNull Long authorId,
    @NotNull String authorName,
    @NotNull OrganizationRole authorRole,
    @NotNull TicketMessageType type,
    @NotNull String body,
    @NotNull LocalDateTime createdAt) {}
