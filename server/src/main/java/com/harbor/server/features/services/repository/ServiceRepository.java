package com.harbor.server.features.services.repository;

import com.harbor.server.features.services.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
  boolean existsByNameIgnoreCaseAndServiceTeamIdAndOrganizationId(
      String name, Long serviceTeamId, Long organizationId);

  Optional<Service> findByIdAndOrganizationId(Long id, Long organizationId);

  List<Service> findByOrganizationId(Long organizationId);

  List<Service> findAllByServiceTeamIdAndOrganizationId(Long serviceTeamId, Long organizationId);
}
