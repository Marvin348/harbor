package com.harbor.server.features.serviceTeam.dto.response;

import jakarta.validation.constraints.NotNull;

public record ServiceTeamResponse(
    @NotNull Long id, @NotNull String name, @NotNull String description) {}
