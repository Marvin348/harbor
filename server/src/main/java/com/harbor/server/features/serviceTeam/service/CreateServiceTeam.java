package com.harbor.server.features.serviceTeam.service;

import com.harbor.server.common.exception.ConflictException;
import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.serviceTeam.dto.request.CreateServiceTeamRequest;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamResponse;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateServiceTeam {
  private final CurrentUserProvider currentUserProvider;
  private final ServiceTeamRepository serviceTeamRepository;
  private final OrganizationRepository organizationRepository;

  public ServiceTeamResponse createServiceTeam(CreateServiceTeamRequest request) {

    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Long organizationId = userDetails.getOrganizationId();

    String name = request.name().trim();

    if (serviceTeamRepository.existsByNameIgnoreCaseAndOrganizationId(name, organizationId)) {
      throw new ConflictException("Service team already exists");
    }

    Organization organization =
        organizationRepository
            .findById(organizationId)
            .orElseThrow(() -> new NotFoundException("Organization not found"));

    String description = request.description().trim();

    ServiceTeam serviceTeam = new ServiceTeam(name, organization, description);

    serviceTeamRepository.save(serviceTeam);

    return new ServiceTeamResponse(
        serviceTeam.getId(), serviceTeam.getName(), serviceTeam.getDescription());
  }
}
