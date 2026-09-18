package com.harbor.server.features.tickets.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
    @NotNull(message = "Service is required") Long serviceId,
    @NotBlank(message = "Subject is required")
        @Size(max = 150, message = "Subject must not exceed 150 characters")
        String subject,
    @NotBlank(message = "Description is required")
        @Size(max = 1500, message = "Description must not exceed 1500 characters")
        String description,
    @NotNull(message = "Assessment is required") @Valid TicketAssessmentRequest assessment) {}
