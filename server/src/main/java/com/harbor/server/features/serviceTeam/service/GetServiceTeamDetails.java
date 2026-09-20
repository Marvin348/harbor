package com.harbor.server.features.serviceTeam.service;

import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamDetailsResponse;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetServiceTeamDetails {

  private final ServiceTeamRepository serviceTeamRepository;
  private final CurrentUserProvider currentUserProvider;

  public ServiceTeamDetailsResponse execute(Long id) {
    Long organizationId = currentUserProvider.getCurrentOrganizationId();

    return serviceTeamRepository
        .findServiceTeamDetails(organizationId, id)
        .orElseThrow(() -> new NotFoundException("Service Team Not Found"));
  }
}
