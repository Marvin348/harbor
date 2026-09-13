package com.harbor.server.features.tickets.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
    @NotNull Long serviceId,
    @NotBlank @Size(max = 150) String subject,
    @NotBlank @Size(max = 1500) String description,
    @NotNull @Valid TicketAssessmentRequest assessment) {}
