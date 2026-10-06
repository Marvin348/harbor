package com.harbor.server.features.tickets.sla.repository;

import com.harbor.server.features.tickets.sla.model.TicketSla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketSlaRepository extends JpaRepository<TicketSla, Long> {
  boolean existsByTicketId(Long ticketId);

  @Query(
      """
          SELECT ts.id
          FROM TicketSla ts
          WHERE ts.responseDueAt < :now
          AND (ts.firstRespondedAt IS NULL OR ts.firstRespondedAt > ts.responseDueAt)
          AND ts.responseBreachedAt IS NULL
          """)
  List<Long> findResponseBreachCandidateIds(@Param("now") LocalDateTime now);

  @Query(
      """
          SELECT ts.id
          FROM TicketSla ts
          WHERE ts.resolutionDueAt < :now
          AND (ts.resolvedAt IS NULL OR ts.resolvedAt > ts.resolutionDueAt)
          AND ts.resolutionBreachedAt IS NULL
          """)
  List<Long> findResolutionBreachCandidateIds(@Param("now") LocalDateTime now);

  @Modifying
  @Query(
      value =
          """
         UPDATE ticket_slas
         SET response_breached_at = :now
         WHERE id = :ticketSlaId
         AND response_due_at < :now
         AND (first_responded_at IS NULL OR first_responded_at > response_due_at)
         AND response_breached_at IS NULL
         """,
      nativeQuery = true)
  int markResponseBreached(@Param("ticketSlaId") Long ticketSlaId, @Param("now") LocalDateTime now);

  @Modifying
  @Query(
      value =
          """
          UPDATE ticket_slas
          SET resolution_breached_at = :now
          WHERE id = :ticketSlaId
          AND resolution_due_at < :now
          AND (resolved_at IS NULL OR resolved_at > resolution_due_at)
          AND resolution_breached_at IS NULL
          """,
      nativeQuery = true)
  int markResolutionBreached(
      @Param("ticketSlaId") Long ticketSlaId, @Param("now") LocalDateTime now);
}
