package com.harbor.server.features.serviceTeam.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetServiceTeamsQuery(String search, @Min(1) int page, @Min(1) @Max(100) int limit) {}
