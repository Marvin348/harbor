package com.harbor.server.features.tickets.sla.repository;

import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaPolicyRepository extends JpaRepository<SlaPolicy, Long> {

  boolean existsByOrganizationIdAndTicketPriority(
      Long organizationId, TicketPriority ticketPriority);

  List<SlaPolicy> findAllByOrganizationIdOrderByIdAsc(Long organizationId);
}
