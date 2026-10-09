package com.harbor.server.integration.tickets.sla.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.model.TicketSla;
import com.harbor.server.features.tickets.sla.projection.SlaBreachCandidate;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import com.harbor.server.features.user.model.User;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class TicketSlaRepositoryIntegrationTest extends AbstractControllerIntegrationTest {
  @Autowired private TicketSlaRepository ticketSlaRepository;
  @Autowired private JdbcTemplate jdbcTemplate;
  @PersistenceContext private EntityManager entityManager;

  @Nested
  class findResponseBreachCandidateIdsTests {

    @Test
    void shouldFindOverdueTicketSlaWithoutFirstResponse() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.minusMinutes(1), now.plusHours(4));

      List<SlaBreachCandidate> candidates = ticketSlaRepository.findResponseBreachCandidateIds(now);

      assertAll(
          () -> assertEquals(1, candidates.size()),
          () -> assertEquals(ticketSla.getId(), candidates.getFirst().ticketSlaId()),
          () ->
              assertEquals(
                  ticketSla.getOrganization().getId(), candidates.getFirst().organizationId()));
    }

    @Test
    void shouldFindOverdueTicketSlaWhenFirstResponseWasLate() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime responseDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(responseDueAt, now.plusHours(4));
      setFirstRespondedAt(ticketSla.getId(), responseDueAt.plusMinutes(1));

      List<SlaBreachCandidate> candidates = ticketSlaRepository.findResponseBreachCandidateIds(now);

      assertAll(
          () -> assertEquals(1, candidates.size()),
          () -> assertEquals(ticketSla.getId(), candidates.getFirst().ticketSlaId()));
    }

    @Test
    void shouldNotFindTicketSlaWhenFirstResponseWasBeforeDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime responseDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(responseDueAt, now.plusHours(4));
      setFirstRespondedAt(ticketSla.getId(), responseDueAt.minusSeconds(1));

      List<SlaBreachCandidate> candidates = ticketSlaRepository.findResponseBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }

    @Test
    void shouldNotFindTicketSlaWhenFirstResponseWasExactlyAtDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime responseDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(responseDueAt, now.plusHours(4));
      setFirstRespondedAt(ticketSla.getId(), responseDueAt);

      List<SlaBreachCandidate> candidates = ticketSlaRepository.findResponseBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }

    @Test
    void shouldNotFindTicketSlaWhenResponseDeadlineIsExactlyNow() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      createTicketSla(now, now.plusHours(4));

      List<SlaBreachCandidate> candidates = ticketSlaRepository.findResponseBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }

    @Test
    void shouldNotFindTicketSlaBeforeResponseDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      createTicketSla(now.plusMinutes(1), now.plusHours(4));

      List<SlaBreachCandidate> candidates = ticketSlaRepository.findResponseBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }

    @Test
    void shouldNotFindTicketSlaThatWasAlreadyMarkedAsResponseBreached() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.plusHours(4), now.minusMinutes(1));
      setResponseBreachedAt(ticketSla.getId(), now.minusSeconds(30));

      List<SlaBreachCandidate> candidates = ticketSlaRepository.findResponseBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }
  }

  @Nested
  class findResolutionBreachCandidateIdsTests {

    @Test
    void shouldFindOverdueTicketSlaWithoutResolution() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), now.minusMinutes(1));

      List<SlaBreachCandidate> candidates =
          ticketSlaRepository.findResolutionBreachCandidateIds(now);

      assertAll(
          () -> assertEquals(1, candidates.size()),
          () -> assertEquals(ticketSla.getId(), candidates.getFirst().ticketSlaId()),
          () ->
              assertEquals(
                  ticketSla.getOrganization().getId(), candidates.getFirst().organizationId()));
    }

    @Test
    void shouldFindOverdueTicketSlaWhenResolutionWasLate() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime resolutionDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), resolutionDueAt);
      setResolvedAt(ticketSla.getId(), resolutionDueAt.plusMinutes(1));

      List<SlaBreachCandidate> candidates =
          ticketSlaRepository.findResolutionBreachCandidateIds(now);

      assertAll(
          () -> assertEquals(1, candidates.size()),
          () -> assertEquals(ticketSla.getId(), candidates.getFirst().ticketSlaId()));
    }

    @Test
    void shouldNotFindTicketSlaWhenResolutionWasBeforeDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime resolutionDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), resolutionDueAt);
      setResolvedAt(ticketSla.getId(), resolutionDueAt.minusSeconds(1));

      List<SlaBreachCandidate> candidates =
          ticketSlaRepository.findResolutionBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }

    @Test
    void shouldNotFindTicketSlaWhenResolutionWasExactlyAtDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime resolutionDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), resolutionDueAt);
      setResolvedAt(ticketSla.getId(), resolutionDueAt);

      List<SlaBreachCandidate> candidates =
          ticketSlaRepository.findResolutionBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }

    @Test
    void shouldNotFindTicketSlaWhenResolutionDeadlineIsExactlyNow() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      createTicketSla(now.minusHours(4), now);

      List<SlaBreachCandidate> candidates =
          ticketSlaRepository.findResolutionBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }

    @Test
    void shouldNotFindTicketSlaBeforeResolutionDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      createTicketSla(now.minusHours(4), now.plusMinutes(1));

      List<SlaBreachCandidate> candidates =
          ticketSlaRepository.findResolutionBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }

    @Test
    void shouldNotFindTicketSlaThatWasAlreadyMarkedAsResolutionBreached() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), now.minusMinutes(1));
      setResolutionBreachedAt(ticketSla.getId(), now.minusSeconds(30));

      List<SlaBreachCandidate> candidates =
          ticketSlaRepository.findResolutionBreachCandidateIds(now);

      assertTrue(candidates.isEmpty());
    }
  }

  @Nested
  @Transactional
  class markResponseBreachedTests {

    @Test
    void shouldMarkOverdueTicketSlaWithoutFirstResponseAsBreached() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.minusMinutes(1), now.plusHours(4));

      int updatedRows = ticketSlaRepository.markResponseBreached(ticketSla.getId(), now);

      TicketSla updatedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(1, updatedRows),
          () -> assertEquals(now, updatedTicketSla.getResponseBreachedAt()));
    }

    @Test
    void shouldMarkTicketSlaAsBreachedWhenFirstResponseWasLate() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime responseDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(responseDueAt, now.plusHours(4));
      setFirstRespondedAt(ticketSla.getId(), responseDueAt.plusMinutes(1));

      int updatedRows = ticketSlaRepository.markResponseBreached(ticketSla.getId(), now);

      TicketSla updatedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(1, updatedRows),
          () -> assertEquals(now, updatedTicketSla.getResponseBreachedAt()));
    }

    @Test
    void shouldNotMarkTicketSlaAsBreachedWhenFirstResponseWasBeforeDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime responseDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(responseDueAt, now.plusHours(4));
      setFirstRespondedAt(ticketSla.getId(), responseDueAt.minusSeconds(1));

      int updatedRows = ticketSlaRepository.markResponseBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () -> assertNull(unchangedTicketSla.getResponseBreachedAt()));
    }

    @Test
    void shouldNotMarkTicketSlaAsBreachedWhenFirstResponseWasExactlyAtDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime responseDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(responseDueAt, now.plusHours(4));
      setFirstRespondedAt(ticketSla.getId(), responseDueAt);

      int updatedRows = ticketSlaRepository.markResponseBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () -> assertNull(unchangedTicketSla.getResponseBreachedAt()));
    }

    @Test
    void shouldNotMarkTicketSlaAsBreachedWhenResponseDeadlineIsExactlyNow() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now, now.plusHours(4));

      int updatedRows = ticketSlaRepository.markResponseBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () -> assertNull(unchangedTicketSla.getResponseBreachedAt()));
    }

    @Test
    void shouldNotMarkTicketSlaAsBreachedBeforeResponseDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.plusMinutes(1), now.plusHours(4));

      int updatedRows = ticketSlaRepository.markResponseBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () -> assertNull(unchangedTicketSla.getResponseBreachedAt()));
    }

    @Test
    void shouldNotOverwriteExistingResponseBreachedAt() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime existingResponseBreachedAt = now.minusMinutes(1);
      TicketSla ticketSla = createTicketSla(now.minusHours(1), now.plusHours(4));
      setResponseBreachedAt(ticketSla.getId(), existingResponseBreachedAt);

      int updatedRows = ticketSlaRepository.markResponseBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () ->
              assertEquals(
                  existingResponseBreachedAt, unchangedTicketSla.getResponseBreachedAt()));
    }

    @Test
    void shouldReturnZeroWhenTicketSlaDoesNotExist() {
      int updatedRows =
          ticketSlaRepository.markResponseBreached(
              Long.MAX_VALUE, LocalDateTime.of(2026, 10, 8, 12, 0));

      assertEquals(0, updatedRows);
    }
  }

  @Nested
  @Transactional
  class markResolutionBreachedTests {

    @Test
    void shouldMarkOverdueUnresolvedTicketSlaAsBreached() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), now.minusMinutes(1));

      int updatedRows = ticketSlaRepository.markResolutionBreached(ticketSla.getId(), now);

      TicketSla updatedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(1, updatedRows),
          () -> assertEquals(now, updatedTicketSla.getResolutionBreachedAt()));
    }

    @Test
    void shouldMarkTicketSlaAsBreachedWhenResolutionWasLate() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime resolutionDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), resolutionDueAt);
      setResolvedAt(ticketSla.getId(), resolutionDueAt.plusMinutes(1));

      int updatedRows = ticketSlaRepository.markResolutionBreached(ticketSla.getId(), now);

      TicketSla updatedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(1, updatedRows),
          () -> assertEquals(now, updatedTicketSla.getResolutionBreachedAt()));
    }

    @Test
    void shouldNotMarkTicketSlaAsBreachedWhenResolutionWasBeforeDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime resolutionDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), resolutionDueAt);
      setResolvedAt(ticketSla.getId(), resolutionDueAt.minusSeconds(1));

      int updatedRows = ticketSlaRepository.markResolutionBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () -> assertNull(unchangedTicketSla.getResolutionBreachedAt()));
    }

    @Test
    void shouldNotMarkTicketSlaAsBreachedWhenResolutionWasExactlyAtDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime resolutionDueAt = now.minusHours(1);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), resolutionDueAt);
      setResolvedAt(ticketSla.getId(), resolutionDueAt);

      int updatedRows = ticketSlaRepository.markResolutionBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () -> assertNull(unchangedTicketSla.getResolutionBreachedAt()));
    }

    @Test
    void shouldNotMarkTicketSlaAsBreachedWhenResolutionDeadlineIsExactlyNow() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), now);

      int updatedRows = ticketSlaRepository.markResolutionBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () -> assertNull(unchangedTicketSla.getResolutionBreachedAt()));
    }

    @Test
    void shouldNotMarkTicketSlaAsBreachedBeforeResolutionDeadline() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), now.plusMinutes(1));

      int updatedRows = ticketSlaRepository.markResolutionBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () -> assertNull(unchangedTicketSla.getResolutionBreachedAt()));
    }

    @Test
    void shouldNotOverwriteExistingResolutionBreachedAt() {
      LocalDateTime now = LocalDateTime.of(2026, 10, 8, 12, 0);
      LocalDateTime existingResolutionBreachedAt = now.minusMinutes(1);
      TicketSla ticketSla = createTicketSla(now.minusHours(4), now.minusHours(1));
      setResolutionBreachedAt(ticketSla.getId(), existingResolutionBreachedAt);

      int updatedRows = ticketSlaRepository.markResolutionBreached(ticketSla.getId(), now);

      TicketSla unchangedTicketSla = reloadTicketSla(ticketSla.getId());

      assertAll(
          () -> assertEquals(0, updatedRows),
          () ->
              assertEquals(
                  existingResolutionBreachedAt, unchangedTicketSla.getResolutionBreachedAt()));
    }

    @Test
    void shouldReturnZeroWhenTicketSlaDoesNotExist() {
      int updatedRows =
          ticketSlaRepository.markResolutionBreached(
              Long.MAX_VALUE, LocalDateTime.of(2026, 10, 8, 12, 0));

      assertEquals(0, updatedRows);
    }
  }

  private TicketSla reloadTicketSla(Long ticketSlaId) {
    entityManager.clear();
    return ticketSlaRepository.findById(ticketSlaId).orElseThrow();
  }

  private TicketSla createTicketSla(LocalDateTime responseDueAt, LocalDateTime resolutionDueAt) {
    User requester = testDataFactory.createRequester();
    Organization organization = requester.getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Support");
    Service service = testDataFactory.createService(organization, serviceTeam, "IT Support");
    Ticket ticket =
        testDataFactory.createTicket(
            requester, service, "Printer does not work", TicketStatus.OPEN, TicketPriority.HIGH);
    SlaPolicy slaPolicy =
        testDataFactory.createSlaPolicy(
            organization, "High priority SLA", TicketPriority.HIGH, 30, 240);

    return ticketSlaRepository.save(
        new TicketSla(organization, ticket, slaPolicy, responseDueAt, resolutionDueAt));
  }

  private void setFirstRespondedAt(Long ticketSlaId, LocalDateTime firstRespondedAt) {
    jdbcTemplate.update(
        "UPDATE ticket_slas SET first_responded_at = ? WHERE id = ?",
        firstRespondedAt,
        ticketSlaId);
  }

  private void setResponseBreachedAt(Long ticketSlaId, LocalDateTime responseBreachedAt) {
    jdbcTemplate.update(
        "UPDATE ticket_slas SET response_breached_at = ? WHERE id = ?",
        responseBreachedAt,
        ticketSlaId);
  }

  private void setResolvedAt(Long ticketSlaId, LocalDateTime resolvedAt) {
    jdbcTemplate.update(
        "UPDATE ticket_slas SET resolved_at = ? WHERE id = ?", resolvedAt, ticketSlaId);
  }

  private void setResolutionBreachedAt(Long ticketSlaId, LocalDateTime resolutionBreachedAt) {
    jdbcTemplate.update(
        "UPDATE ticket_slas SET resolution_breached_at = ? WHERE id = ?",
        resolutionBreachedAt,
        ticketSlaId);
  }
}
