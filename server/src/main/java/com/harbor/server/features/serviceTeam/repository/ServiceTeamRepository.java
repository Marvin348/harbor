package com.harbor.server.features.serviceTeam.repository;

import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceTeamRepository extends JpaRepository<ServiceTeam, Long> {
  boolean existsByNameIgnoreCaseAndOrganizationId(String name, Long organizationId);

  List<ServiceTeam> findByOrganizationId(Long organizationId);
}
