package com.harbor.server.features.serviceTeam.dto.response;

import jakarta.validation.constraints.NotNull;

public record ServiceTeamDetailsResponse(
    @NotNull Long id,
    @NotNull String name,
    @NotNull String description,
    @NotNull long agentCount,
    @NotNull long serviceCount) {}
