package com.harbor.server.features.tickets.dto.request;

import com.harbor.server.features.tickets.model.TicketBusinessCriticality;
import com.harbor.server.features.tickets.model.TicketImpact;
import com.harbor.server.features.tickets.model.TicketUrgency;
import jakarta.validation.constraints.NotNull;

public record TicketAssessmentRequest(
    @NotNull TicketImpact impact,
    @NotNull TicketUrgency urgency,
    @NotNull TicketBusinessCriticality businessCriticality) {}
