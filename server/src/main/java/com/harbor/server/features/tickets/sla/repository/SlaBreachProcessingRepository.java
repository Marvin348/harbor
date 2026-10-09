package com.harbor.server.features.tickets.sla.repository;

import com.harbor.server.features.tickets.sla.model.SlaBreachProcessing;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface SlaBreachProcessingRepository
    extends JpaRepository<SlaBreachProcessing, Long>, SlaBreachProcessingRepositoryCustom {

  @Transactional
  @Modifying
  @Query(
"""
UPDATE SlaBreachProcessing sbp
SET sbp.queuedAt = :queuedAt
WHERE sbp.id = :processingId
""")
  int markQueued(Long processingId, LocalDateTime queuedAt);

  @Modifying
  @Query(
      """
          UPDATE SlaBreachProcessing sbp
          SET sbp.status = 'COMPLETED',
              sbp.processedAt = :processedAt
          WHERE sbp.id = :processingId
            AND sbp.status = 'PROCESSING'
          """)
  int markCompleted(
      @Param("processingId") Long processingId, @Param("processedAt") LocalDateTime processedAt);

  @Query(
      """
    SELECT sbp
    FROM SlaBreachProcessing sbp
    WHERE sbp.status = :status
      AND sbp.queuedAt IS NULL
      AND sbp.createdAt < :cutoff
    """)
  List<SlaBreachProcessing> findRecoverable(
      @Param("status") SlaBreachProcessingStatus status, @Param("cutoff") LocalDateTime cutoff);
}
