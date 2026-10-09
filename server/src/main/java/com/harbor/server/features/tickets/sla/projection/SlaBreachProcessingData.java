package com.harbor.server.features.tickets.sla.projection;

import com.harbor.server.features.tickets.sla.model.SlaBreachProcessingStatus;
import com.harbor.server.features.tickets.sla.model.SlaBreachType;

public record SlaBreachProcessingData(
    Long ticketSlaId, SlaBreachType breachType, SlaBreachProcessingStatus status) {}
