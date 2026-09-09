package com.harbor.server.features.services.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateServiceRequest(
    @NotBlank @Size(max = 100) String name,
    @Size(max = 250) String description,
    @NotNull Long serviceTeamId) {}
