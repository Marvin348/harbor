package com.harbor.server.features.serviceTeam.repository;

import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamDetailsResponse;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamListItemResponse;
import com.harbor.server.features.serviceTeam.dto.response.ServiceTeamOptionResponse;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServiceTeamRepository extends JpaRepository<ServiceTeam, Long> {
  boolean existsByNameIgnoreCaseAndOrganizationId(String name, Long organizationId);

  List<ServiceTeam> findByOrganizationId(Long organizationId);

  Optional<ServiceTeam> findByIdAndOrganizationId(Long id, Long organizationId);

  boolean existsByIdAndOrganizationId(Long id, Long organizationId);

  List<ServiceTeamOptionResponse> findByOrganizationIdOrderByNameAsc(Long organizationId);

  @Query(
      value =
          """
        SELECT NEW com.harbor.server.features.serviceTeam.dto.response.ServiceTeamListItemResponse(
            st.id,
            st.name,
            st.description,
            COUNT(DISTINCT stm.id),
            COUNT(DISTINCT s.id),
            st.createdAt)
        FROM ServiceTeam st
        LEFT JOIN Service s
            ON s.serviceTeam.id = st.id
        LEFT JOIN ServiceTeamMember stm
            ON stm.serviceTeam.id = st.id
        WHERE st.organization.id = :organizationId
        AND (
             :search IS NULL
             OR LOWER(st.name) LIKE LOWER(CONCAT('%', CAST(:search AS STRING), '%' ))
        )
        GROUP BY st.id, st.name, st.description, st.createdAt
        """,
      countQuery =
          """
        SELECT COUNT(st.id)
        FROM ServiceTeam st
        WHERE st.organization.id = :organizationId
        AND (
            :search IS NULL
            OR LOWER(st.name) LIKE LOWER(CONCAT('%', CAST(:search AS STRING), '%')))
        """)
  Page<ServiceTeamListItemResponse> findServiceTeams(
      Long organizationId, Pageable pageable, String search);

  @Query(
      """
        SELECT NEW com.harbor.server.features.serviceTeam.dto.response.ServiceTeamDetailsResponse(
        st.id,
        st.name,
        st.description,
        COUNT(DISTINCT stm.id),
        COUNT(DISTINCT s.id)
        )
        FROM ServiceTeam st
        LEFT JOIN Service s
            ON s.serviceTeam.id = st.id
        LEFT JOIN ServiceTeamMember stm
            ON stm.serviceTeam.id = st.id
        WHERE st.id = :serviceTeamId
        AND st.organization.id = :organizationId
        GROUP BY st.id, st.name, st.description
        """)
  Optional<ServiceTeamDetailsResponse> findServiceTeamDetails(
      Long organizationId, Long serviceTeamId);
}
