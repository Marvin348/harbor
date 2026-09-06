package com.harbor.server.features.serviceTeam.service;

import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamResponse;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetServiceTeams {

  private final CurrentUserProvider currentUserProvider;
  private final ServiceTeamRepository serviceTeamRepository;

  public List<ServiceTeamResponse> getServiceTeams() {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Long organizationId = userDetails.getOrganizationId();

    List<ServiceTeam> teams = serviceTeamRepository.findByOrganizationId(organizationId);

    return teams.stream()
        .map(team -> new ServiceTeamResponse(team.getId(), team.getName(), team.getDescription()))
        .toList();
  }
}
