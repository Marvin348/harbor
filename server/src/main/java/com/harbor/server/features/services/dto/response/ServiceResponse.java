package com.harbor.server.features.services.dto.response;

import com.harbor.server.features.services.model.ServiceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ServiceResponse(
    @NotNull Long id,
    @NotNull String name,
    String description,
    @NotNull ServiceStatus status,
    @NotNull LocalDateTime createdAt) {}
