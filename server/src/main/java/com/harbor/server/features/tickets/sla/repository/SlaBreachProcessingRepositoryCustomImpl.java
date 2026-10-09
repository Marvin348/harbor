package com.harbor.server.features.tickets.sla.repository;

import com.harbor.server.features.tickets.sla.model.SlaBreachType;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessingStatus;
import com.harbor.server.features.tickets.sla.projection.SlaBreachProcessingData;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SlaBreachProcessingRepositoryCustomImpl
    implements SlaBreachProcessingRepositoryCustom {
  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Override
  public Optional<Long> createIfAbsent(
      Long organizationId, Long ticketSlaId, SlaBreachType breachType, LocalDateTime now) {
    String sql =
        """
              INSERT INTO sla_breach_processing (
                  organization_id,
                  ticket_sla_id,
                  status,
                  breach_type,
                  created_at
              )
              VALUES (
                  :organizationId,
                  :ticketSlaId,
                  'PENDING',
                  :breachType,
                  :now
              )
              ON CONFLICT (ticket_sla_id, breach_type)
              DO NOTHING
              RETURNING id
              """;

    MapSqlParameterSource params =
        new MapSqlParameterSource()
            .addValue("organizationId", organizationId)
            .addValue("ticketSlaId", ticketSlaId)
            .addValue("breachType", breachType.name())
            .addValue("now", now);

    return jdbcTemplate.query(
        sql, params, rs -> rs.next() ? Optional.of(rs.getLong("id")) : Optional.empty());
  }

  @Override
  public Optional<SlaBreachProcessingData> claimForProcessing(
      Long processingId, LocalDateTime now) {
    String sql =
        """
        UPDATE sla_breach_processing
        SET status = 'PROCESSING',
            processing_started_at = :now
        WHERE id = :processingId
        AND status = 'PENDING'
        RETURNING ticket_sla_id, breach_type, status
        """;

    MapSqlParameterSource params =
        new MapSqlParameterSource()
            .addValue("processingId", processingId)
            .addValue("now", now);

    return jdbcTemplate.query(
        sql,
        params,
        rs ->
            rs.next()
                ? Optional.of(
                    new SlaBreachProcessingData(
                        rs.getLong("ticket_sla_id"),
                        SlaBreachType.valueOf(rs.getString("breach_type")),
                        SlaBreachProcessingStatus.valueOf(rs.getString("status"))))
                : Optional.empty());
  }
}
