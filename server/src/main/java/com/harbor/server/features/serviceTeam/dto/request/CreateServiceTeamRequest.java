package com.harbor.server.features.serviceTeam.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateServiceTeamRequest(
    @NotBlank @Size(max = 100) String name, @NotBlank @Size(max = 250) String description) {}
