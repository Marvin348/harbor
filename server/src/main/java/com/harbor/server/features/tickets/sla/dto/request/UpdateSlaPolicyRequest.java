package com.harbor.server.features.tickets.sla.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateSlaPolicyRequest(
    @Pattern(regexp = "(?s).*\\S.*", message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,
    @Positive Integer responseTimeMinutes,
    @Positive Integer resolutionTimeMinutes) {}
