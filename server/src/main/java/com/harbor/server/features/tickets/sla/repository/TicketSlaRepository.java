package com.harbor.server.features.tickets.sla.repository;

import com.harbor.server.features.tickets.sla.model.TicketSla;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketSlaRepository extends JpaRepository<TicketSla, Long> {
  boolean existsByTicketId(Long ticketId);
}
