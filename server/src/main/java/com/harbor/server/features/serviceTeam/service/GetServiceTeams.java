package com.harbor.server.features.serviceTeam.service;

import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.serviceTeam.dto.request.GetServiceTeamsQuery;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamListItemResponse;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetServiceTeams {

  private final CurrentUserProvider currentUserProvider;
  private final ServiceTeamRepository serviceTeamRepository;

  public Page<ServiceTeamListItemResponse> execute(GetServiceTeamsQuery request) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Pageable pageable =
        PageRequest.of(
            request.page() - 1, request.limit(), Sort.by(Sort.Direction.DESC, "createdAt"));

    Long organizationId = userDetails.getOrganizationId();

    return serviceTeamRepository.findServiceTeams(organizationId, pageable, request.search());
  }
}
