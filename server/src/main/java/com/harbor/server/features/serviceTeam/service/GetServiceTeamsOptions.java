package com.harbor.server.features.serviceTeam.service;

import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamOptionResponse;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetServiceTeamsOptions {
  private final CurrentUserProvider currentUserProvider;
  private final ServiceTeamRepository serviceTeamRepository;

  public List<ServiceTeamOptionResponse> execute() {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Long organizationId = userDetails.getOrganizationId();

    return serviceTeamRepository.findByOrganizationIdOrderByNameAsc(organizationId);
  }
}
