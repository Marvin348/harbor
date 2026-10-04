package com.harbor.server.features.tickets.sla.repository;

import com.harbor.server.features.tickets.sla.model.TicketSla;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketSlaRepository extends JpaRepository<TicketSla, Long> {
  boolean existsByTicketId(Long ticketId);

  @Query(
      """
          SELECT ts.id
          FROM TicketSla ts
          WHERE ts.responseDueAt < :now
          AND ts.firstRespondedAt IS NULL
          AND ts.responseBreachedAt IS NULL
          """)
  List<Long> findOverdueResponseSlaIds(@Param("now") LocalDateTime now);
}
