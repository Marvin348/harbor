package com.harbor.server.features.auth.dto.response;

import com.harbor.server.features.user.model.OrganizationRole;

public record LoginResponse(
    Long id, String email, String firstName, String lastName, OrganizationRole role) {}
