package com.harbor.server.features.serviceTeam.dto.response;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ServiceTeamListItemResponse(
    @NotNull Long id,
    @NotNull String name,
    @NotNull String description,
    @NotNull long agentCount,
    @NotNull long serviceCount,
    @NotNull LocalDateTime createdAt) {}
