package com.harbor.server.features.tickets.communication.message.repository;

import com.harbor.server.features.tickets.communication.message.dto.response.TicketMessageResponse;
import com.harbor.server.features.tickets.communication.message.model.TicketMessage;
import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketMessageRepository extends JpaRepository<TicketMessage, Long> {

  @Query(
      """
        SELECT new com.harbor.server.features.tickets.communication.message.dto.response.TicketMessageResponse(
            tm.id,
            a.id,
            CONCAT(a.firstName, ' ', a.lastName),
            a.role,
            tm.type,
            tm.body,
            tm.createdAt)
        FROM TicketMessage tm
        JOIN tm.author a
        WHERE tm.ticket.id = :ticketId
        AND tm.organization.id = :organizationId
        ORDER BY tm.createdAt ASC, tm.id ASC
        """)
  List<TicketMessageResponse> findTicketMessagesByTicketIdAndOrganizationId(
      Long ticketId, Long organizationId);

  @Query(
      """
        SELECT new com.harbor.server.features.tickets.communication.message.dto.response.TicketMessageResponse(
            tm.id,
            a.id,
            CONCAT(a.firstName, ' ', a.lastName),
            a.role,
            tm.type,
            tm.body,
            tm.createdAt)
        FROM TicketMessage tm
        JOIN tm.author a
        WHERE tm.ticket.id = :ticketId
        AND tm.organization.id = :organizationId
        AND tm.type = :type
        ORDER BY tm.createdAt ASC, tm.id ASC
        """)
  List<TicketMessageResponse> findTicketMessagesByTicketIdAndOrganizationIdAndType(
      Long ticketId, Long organizationId, TicketMessageType type);
}
