package com.harbor.server.features.tickets.sla.repository;

import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaPolicyRepository extends JpaRepository<SlaPolicy, Long> {

  boolean existsByOrganizationIdAndTicketPriority(
      Long organizationId, TicketPriority ticketPriority);

  Optional<SlaPolicy> findByOrganizationIdAndTicketPriority(
      Long organizationId, TicketPriority ticketPriority);

  List<SlaPolicy> findAllByOrganizationIdOrderByIdAsc(Long organizationId);
}
