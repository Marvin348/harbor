package com.harbor.server.features.services.repository;

import com.harbor.server.features.services.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
  boolean existsByNameIgnoreCaseAndServiceTeamIdAndOrganizationId(
      String name, Long serviceTeamId, Long organizationId);

  List<Service> findByOrganizationId(Long organizationId);

  List<Service> findAllByServiceTeamIdAndOrganizationId(Long serviceTeamId, Long organizationId);
}
