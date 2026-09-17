package com.harbor.server.features.serviceTeam.dto.response;

import jakarta.validation.constraints.NotNull;

public record ServiceTeamOptionResponse(@NotNull Long id, @NotNull String name) {}
