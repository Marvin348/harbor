package com.harbor.server.features.tickets.repository;

import com.harbor.server.features.tickets.dto.response.RequesterTicketItemResponse;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Query(
      """
              SELECT new com.harbor.server.features.tickets.dto.response.RequesterTicketItemResponse(
              t.id,
              t.subject,
              s.name,
              t.status,
              t.priority,
              CASE
                     WHEN a.id IS NULL THEN NULL
                     ELSE CONCAT(a.firstName, ' ', a.lastName)
              END,
              t.createdAt,
              t.updatedAt)
              FROM Ticket t
                  JOIN t.service s
                  LEFT JOIN t.assignedAgent a
                  WHERE t.requester.id = :requesterId
                    AND t.organization.id = :organizationId
                    AND (
                        :search IS NULL
                        OR LOWER(t.subject) LIKE LOWER(CONCAT('%', CAST(:search AS STRING), '%'))
                    )
                    AND (
                        :status IS NULL
                        OR t.status = :status
                    )
              """)
  Page<RequesterTicketItemResponse> findRequesterTickets(
      Long requesterId, Long organizationId, Pageable pageable, String search, TicketStatus status);
}
