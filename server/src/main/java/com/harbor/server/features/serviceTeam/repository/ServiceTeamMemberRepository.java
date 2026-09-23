package com.harbor.server.features.serviceTeam.repository;

import com.harbor.server.features.serviceTeam.model.ServiceTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTeamMemberRepository extends JpaRepository<ServiceTeamMember, Long> {
    boolean existsByServiceTeamIdAndUserId(Long id, Long userId);

}
