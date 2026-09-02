package com.harbor.server.features.auth.dto.response;

import com.harbor.server.features.user.model.OrganizationRole;
import jakarta.validation.constraints.NotNull;

public record LoginResponse(
    @NotNull Long id,
    @NotNull String email,
    @NotNull String firstName,
    @NotNull String lastName,
    @NotNull OrganizationRole role) {}
