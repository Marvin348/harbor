package com.harbor.server.features.organization.repository;

import com.harbor.server.features.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
