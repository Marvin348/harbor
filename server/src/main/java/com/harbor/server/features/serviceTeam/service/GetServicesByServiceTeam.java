package com.harbor.server.features.serviceTeam.service;

import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import com.harbor.server.features.services.dto.response.ServiceResponse;
import com.harbor.server.features.services.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetServicesByServiceTeam {

  private final CurrentUserProvider currentUserProvider;
  private final ServiceRepository serviceRepository;
  private final ServiceTeamRepository serviceTeamRepository;

  public List<ServiceResponse> execute(Long serviceTeamId) {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Long organizationId = userDetails.getOrganizationId();

    serviceTeamRepository
        .findByIdAndOrganizationId(serviceTeamId, organizationId)
        .orElseThrow(() -> new NotFoundException("Service team not found"));

    return serviceRepository
        .findAllByServiceTeamIdAndOrganizationId(serviceTeamId, organizationId)
        .stream()
        .map(
            service ->
                new ServiceResponse(
                    service.getId(),
                    service.getName(),
                    service.getDescription(),
                    service.getStatus(),
                    service.getCreatedAt()))
        .toList();
  }
}
