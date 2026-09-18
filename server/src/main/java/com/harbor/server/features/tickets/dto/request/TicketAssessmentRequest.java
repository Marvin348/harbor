package com.harbor.server.features.tickets.dto.request;

import com.harbor.server.features.tickets.model.TicketBusinessCriticality;
import com.harbor.server.features.tickets.model.TicketImpact;
import com.harbor.server.features.tickets.model.TicketUrgency;
import jakarta.validation.constraints.NotNull;

public record TicketAssessmentRequest(
    @NotNull(message = "Impact is required") TicketImpact impact,
    @NotNull(message = "Urgency is required") TicketUrgency urgency,
    @NotNull(message = "Business criticality is required")
        TicketBusinessCriticality businessCriticality) {}
