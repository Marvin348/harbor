package com.harbor.server.features.services.service;

import com.harbor.server.common.security.CurrentUserProvider;
import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.services.dto.response.ServiceResponse;
import com.harbor.server.features.services.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetServices {
  private final CurrentUserProvider currentUserProvider;
  private final ServiceRepository serviceRepository;

  public List<ServiceResponse> execute() {
    CustomUserDetails userDetails = currentUserProvider.getCurrentUser();

    Long organizationId = userDetails.getOrganizationId();

    List<com.harbor.server.features.services.model.Service> services =
        serviceRepository.findByOrganizationId(organizationId);

    return services.stream()
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
