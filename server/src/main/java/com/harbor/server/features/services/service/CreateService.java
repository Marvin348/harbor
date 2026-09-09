package com.harbor.server.features.services.service;

import com.harbor.server.common.exception.ConflictException;
import com.harbor.server.common.exception.NotFoundException;
import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import com.harbor.server.features.services.dto.request.CreateServiceRequest;
import com.harbor.server.features.services.dto.response.ServiceResponse;
import com.harbor.server.features.services.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateService {

  private final CurrentUserProvider currentUserProvider;
  private final OrganizationRepository organizationRepository;
  private final ServiceTeamRepository serviceTeamRepository;
  private final ServiceRepository serviceRepository;

  public ServiceResponse execute(CreateServiceRequest request) {

    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Long organizationId = userDetails.getOrganizationId();

    String name = request.name().trim();

    Organization organization =
        organizationRepository
            .findById(organizationId)
            .orElseThrow(() -> new NotFoundException("Organization not found"));

    ServiceTeam serviceTeam =
        serviceTeamRepository
            .findByIdAndOrganizationId(request.serviceTeamId(), organizationId)
            .orElseThrow(() -> new NotFoundException("Service team not found"));

    if (serviceRepository.existsByNameIgnoreCaseAndServiceTeamIdAndOrganizationId(
        name, serviceTeam.getId(), organizationId)) {
      throw new ConflictException("Service name already exists");
    }

    String description =
        request.description() == null || request.description().isBlank()
            ? null
            : request.description().trim();

    com.harbor.server.features.services.model.Service service =
        new com.harbor.server.features.services.model.Service(
            name, description, organization, serviceTeam);

    serviceRepository.save(service);

    return new ServiceResponse(
        service.getId(),
        service.getName(),
        service.getDescription(),
        service.getStatus(),
        service.getCreatedAt());
  }
}
