package com.harbor.server.features.auth.dto.response;

import com.harbor.server.features.user.model.OrganizationRole;

public record RegisterResponse(
    Long userId,
    Long organizationId,
    String email,
    String firstName,
    String lastName,
    String organizationName,
    OrganizationRole role) {}
