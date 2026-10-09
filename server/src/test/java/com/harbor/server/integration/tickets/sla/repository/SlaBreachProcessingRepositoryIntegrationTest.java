package com.harbor.server.integration.tickets.sla.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessing;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessingStatus;
import com.harbor.server.features.tickets.sla.model.SlaBreachType;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.model.TicketSla;
import com.harbor.server.features.tickets.sla.projection.SlaBreachProcessingData;
import com.harbor.server.features.tickets.sla.repository.SlaBreachProcessingRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import com.harbor.server.features.user.model.User;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class SlaBreachProcessingRepositoryIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired private SlaBreachProcessingRepository slaBreachProcessingRepository;
  @Autowired private TicketSlaRepository ticketSlaRepository;
  @PersistenceContext private EntityManager entityManager;

  @Nested
  class CreateIfAbsentTests {

    @Test
    void shouldCreatePendingSlaBreachProcessing() {
      TicketSla ticketSla = createTicketSla();
      LocalDateTime createdAt = LocalDateTime.of(2026, 10, 7, 12, 30);

      Optional<Long> processingId =
          slaBreachProcessingRepository.createIfAbsent(
              ticketSla.getOrganization().getId(),
              ticketSla.getId(),
              SlaBreachType.RESPONSE,
              createdAt);

      assertTrue(processingId.isPresent());

      SlaBreachProcessing processing =
          slaBreachProcessingRepository.findById(processingId.orElseThrow()).orElseThrow();

      assertAll(
          () ->
              assertEquals(
                  ticketSla.getOrganization().getId(), processing.getOrganization().getId()),
          () -> assertEquals(ticketSla.getId(), processing.getTicketSla().getId()),
          () -> assertEquals(SlaBreachProcessingStatus.PENDING, processing.getStatus()),
          () -> assertEquals(SlaBreachType.RESPONSE, processing.getBreachType()),
          () -> assertEquals(createdAt, processing.getCreatedAt()),
          () -> assertNull(processing.getQueuedAt()),
          () -> assertNull(processing.getProcessingStartedAt()),
          () -> assertNull(processing.getProcessedAt()));
    }

    @Test
    void shouldNotCreateDuplicateForSameTicketSlaAndBreachType() {
      TicketSla ticketSla = createTicketSla();
      LocalDateTime firstCreatedAt = LocalDateTime.of(2026, 10, 7, 12, 30);

      Optional<Long> firstProcessingId =
          slaBreachProcessingRepository.createIfAbsent(
              ticketSla.getOrganization().getId(),
              ticketSla.getId(),
              SlaBreachType.RESPONSE,
              firstCreatedAt);

      Optional<Long> duplicateProcessingId =
          slaBreachProcessingRepository.createIfAbsent(
              ticketSla.getOrganization().getId(),
              ticketSla.getId(),
              SlaBreachType.RESPONSE,
              firstCreatedAt.plusMinutes(5));

      assertAll(
          () -> assertTrue(firstProcessingId.isPresent()),
          () -> assertTrue(duplicateProcessingId.isEmpty()),
          () -> assertEquals(1, slaBreachProcessingRepository.count()));

      SlaBreachProcessing processing =
          slaBreachProcessingRepository.findById(firstProcessingId.orElseThrow()).orElseThrow();

      assertEquals(firstCreatedAt, processing.getCreatedAt());
    }

    @Test
    void shouldCreateResponseAndResolutionProcessingForSameTicketSla() {
      TicketSla ticketSla = createTicketSla();
      LocalDateTime createdAt = LocalDateTime.of(2026, 10, 7, 12, 30);

      Optional<Long> responseProcessingId =
          slaBreachProcessingRepository.createIfAbsent(
              ticketSla.getOrganization().getId(),
              ticketSla.getId(),
              SlaBreachType.RESPONSE,
              createdAt);

      Optional<Long> resolutionProcessingId =
          slaBreachProcessingRepository.createIfAbsent(
              ticketSla.getOrganization().getId(),
              ticketSla.getId(),
              SlaBreachType.RESOLUTION,
              createdAt);

      List<SlaBreachType> breachTypes =
          slaBreachProcessingRepository.findAll().stream()
              .map(SlaBreachProcessing::getBreachType)
              .toList();

      assertAll(
          () -> assertTrue(responseProcessingId.isPresent()),
          () -> assertTrue(resolutionProcessingId.isPresent()),
          () -> assertNotEquals(responseProcessingId, resolutionProcessingId),
          () -> assertEquals(2, slaBreachProcessingRepository.count()),
          () -> assertTrue(breachTypes.contains(SlaBreachType.RESPONSE)),
          () -> assertTrue(breachTypes.contains(SlaBreachType.RESOLUTION)));
    }
  }

  @Nested
  class ClaimForProcessingTests {

    @ParameterizedTest
    @EnumSource(SlaBreachType.class)
    void shouldClaimPendingSlaBreachProcessing(SlaBreachType breachType) {
      SlaBreachProcessing processing = createSlaBreachProcessing(breachType);
      LocalDateTime processingStartedAt = LocalDateTime.of(2026, 10, 8, 9, 15);

      Optional<SlaBreachProcessingData> data =
          slaBreachProcessingRepository.claimForProcessing(processing.getId(), processingStartedAt);

      assertTrue(data.isPresent());

      SlaBreachProcessingData claimedData = data.orElseThrow();
      SlaBreachProcessing updatedProcessing =
          slaBreachProcessingRepository.findById(processing.getId()).orElseThrow();

      assertAll(
          () -> assertEquals(processing.getTicketSla().getId(), claimedData.ticketSlaId()),
          () -> assertEquals(breachType, claimedData.breachType()),
          () -> assertEquals(SlaBreachProcessingStatus.PROCESSING, claimedData.status()),
          () -> assertEquals(SlaBreachProcessingStatus.PROCESSING, updatedProcessing.getStatus()),
          () -> assertEquals(processingStartedAt, updatedProcessing.getProcessingStartedAt()),
          () -> assertNull(updatedProcessing.getQueuedAt()),
          () -> assertNull(updatedProcessing.getProcessedAt()));
    }

    @Test
    void shouldNotClaimSameSlaBreachProcessingTwice() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESPONSE);
      LocalDateTime firstProcessingStartedAt = LocalDateTime.of(2026, 10, 8, 9, 15);

      Optional<SlaBreachProcessingData> firstClaim =
          slaBreachProcessingRepository.claimForProcessing(
              processing.getId(), firstProcessingStartedAt);

      Optional<SlaBreachProcessingData> secondClaim =
          slaBreachProcessingRepository.claimForProcessing(
              processing.getId(), firstProcessingStartedAt.plusMinutes(1));

      SlaBreachProcessing updatedProcessing =
          slaBreachProcessingRepository.findById(processing.getId()).orElseThrow();

      assertAll(
          () -> assertTrue(firstClaim.isPresent()),
          () -> assertTrue(secondClaim.isEmpty()),
          () -> assertEquals(SlaBreachProcessingStatus.PROCESSING, updatedProcessing.getStatus()),
          () -> assertEquals(firstProcessingStartedAt, updatedProcessing.getProcessingStartedAt()));
    }

    @Test
    void shouldReturnEmptyWhenSlaBreachProcessingDoesNotExist() {
      Optional<SlaBreachProcessingData> data =
          slaBreachProcessingRepository.claimForProcessing(
              Long.MAX_VALUE, LocalDateTime.of(2026, 10, 8, 9, 15));

      assertTrue(data.isEmpty());
    }

    @Test
    void shouldAllowOnlyOneConcurrentClaim() throws Exception {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESOLUTION);
      LocalDateTime processingStartedAt = LocalDateTime.of(2026, 10, 8, 9, 15);
      CountDownLatch ready = new CountDownLatch(2);
      CountDownLatch start = new CountDownLatch(1);
      ExecutorService executor = Executors.newFixedThreadPool(2);

      try {
        Future<Optional<SlaBreachProcessingData>> firstClaim =
            executor.submit(
                () -> {
                  ready.countDown();
                  start.await();
                  return slaBreachProcessingRepository.claimForProcessing(
                      processing.getId(), processingStartedAt);
                });

        Future<Optional<SlaBreachProcessingData>> secondClaim =
            executor.submit(
                () -> {
                  ready.countDown();
                  start.await();
                  return slaBreachProcessingRepository.claimForProcessing(
                      processing.getId(), processingStartedAt);
                });

        assertTrue(ready.await(5, TimeUnit.SECONDS));
        start.countDown();

        long successfulClaims =
            List.of(firstClaim.get(5, TimeUnit.SECONDS), secondClaim.get(5, TimeUnit.SECONDS))
                .stream()
                .filter(Optional::isPresent)
                .count();

        SlaBreachProcessing updatedProcessing =
            slaBreachProcessingRepository.findById(processing.getId()).orElseThrow();

        assertAll(
            () -> assertEquals(1, successfulClaims),
            () -> assertEquals(SlaBreachProcessingStatus.PROCESSING, updatedProcessing.getStatus()),
            () -> assertEquals(processingStartedAt, updatedProcessing.getProcessingStartedAt()));
      } finally {
        start.countDown();
        executor.shutdownNow();
      }
    }
  }

  @Nested
  class MarkQueuedTests {

    @Test
    void shouldMarkPendingSlaBreachProcessingAsQueued() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESOLUTION);
      LocalDateTime queuedAt = LocalDateTime.of(2026, 10, 8, 9, 15);

      int updatedRows = slaBreachProcessingRepository.markQueued(processing.getId(), queuedAt);

      SlaBreachProcessing updatedProcessing =
          slaBreachProcessingRepository.findById(processing.getId()).orElseThrow();

      assertAll(
          () -> assertEquals(1, updatedRows),
          () -> assertEquals(queuedAt, updatedProcessing.getQueuedAt()),
          () -> assertEquals(SlaBreachProcessingStatus.PENDING, updatedProcessing.getStatus()),
          () -> assertNull(updatedProcessing.getProcessingStartedAt()),
          () -> assertNull(updatedProcessing.getProcessedAt()));
    }

    @Test
    void shouldMarkQueuedWhenWorkerAlreadyClaimedProcessing() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESPONSE);
      LocalDateTime processingStartedAt = LocalDateTime.of(2026, 10, 8, 9, 15);
      LocalDateTime queuedAt = processingStartedAt.plusSeconds(1);

      Optional<SlaBreachProcessingData> claimedData =
          slaBreachProcessingRepository.claimForProcessing(processing.getId(), processingStartedAt);

      int updatedRows = slaBreachProcessingRepository.markQueued(processing.getId(), queuedAt);

      SlaBreachProcessing updatedProcessing =
          slaBreachProcessingRepository.findById(processing.getId()).orElseThrow();

      assertAll(
          () -> assertTrue(claimedData.isPresent()),
          () -> assertEquals(1, updatedRows),
          () -> assertEquals(queuedAt, updatedProcessing.getQueuedAt()),
          () -> assertEquals(SlaBreachProcessingStatus.PROCESSING, updatedProcessing.getStatus()),
          () -> assertEquals(processingStartedAt, updatedProcessing.getProcessingStartedAt()),
          () -> assertNull(updatedProcessing.getProcessedAt()));
    }

    @Test
    void shouldReturnZeroWhenSlaBreachProcessingDoesNotExist() {
      int updatedRows =
          slaBreachProcessingRepository.markQueued(
              Long.MAX_VALUE, LocalDateTime.of(2026, 10, 8, 9, 15));

      assertEquals(0, updatedRows);
    }
  }

  @Nested
  @Transactional
  class MarkCompletedTests {

    @Test
    void shouldMarkProcessingAsCompleted() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESPONSE);
      LocalDateTime queuedAt = LocalDateTime.of(2026, 10, 8, 9, 15);
      LocalDateTime processingStartedAt = queuedAt.plusMinutes(1);
      LocalDateTime processedAt = processingStartedAt.plusMinutes(1);

      int queuedRows = slaBreachProcessingRepository.markQueued(processing.getId(), queuedAt);
      Optional<SlaBreachProcessingData> claimedData =
          slaBreachProcessingRepository.claimForProcessing(processing.getId(), processingStartedAt);

      int completedRows =
          slaBreachProcessingRepository.markCompleted(processing.getId(), processedAt);

      entityManager.clear();
      SlaBreachProcessing updatedProcessing =
          slaBreachProcessingRepository.findById(processing.getId()).orElseThrow();

      assertAll(
          () -> assertEquals(1, queuedRows),
          () -> assertTrue(claimedData.isPresent()),
          () -> assertEquals(1, completedRows),
          () -> assertEquals(SlaBreachProcessingStatus.COMPLETED, updatedProcessing.getStatus()),
          () -> assertEquals(queuedAt, updatedProcessing.getQueuedAt()),
          () -> assertEquals(processingStartedAt, updatedProcessing.getProcessingStartedAt()),
          () -> assertEquals(processedAt, updatedProcessing.getProcessedAt()));
    }

    @Test
    void shouldNotMarkPendingSlaBreachProcessingAsCompleted() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESOLUTION);

      int completedRows =
          slaBreachProcessingRepository.markCompleted(
              processing.getId(), LocalDateTime.of(2026, 10, 8, 9, 15));

      entityManager.clear();
      SlaBreachProcessing unchangedProcessing =
          slaBreachProcessingRepository.findById(processing.getId()).orElseThrow();

      assertAll(
          () -> assertEquals(0, completedRows),
          () -> assertEquals(SlaBreachProcessingStatus.PENDING, unchangedProcessing.getStatus()),
          () -> assertNull(unchangedProcessing.getProcessingStartedAt()),
          () -> assertNull(unchangedProcessing.getProcessedAt()));
    }

    @Test
    void shouldNotCompleteSameSlaBreachProcessingTwice() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESPONSE);
      LocalDateTime processingStartedAt = LocalDateTime.of(2026, 10, 8, 9, 15);
      LocalDateTime firstProcessedAt = processingStartedAt.plusMinutes(1);

      Optional<SlaBreachProcessingData> claimedData =
          slaBreachProcessingRepository.claimForProcessing(processing.getId(), processingStartedAt);

      int firstCompletedRows =
          slaBreachProcessingRepository.markCompleted(processing.getId(), firstProcessedAt);
      int secondCompletedRows =
          slaBreachProcessingRepository.markCompleted(
              processing.getId(), firstProcessedAt.plusMinutes(1));

      entityManager.clear();
      SlaBreachProcessing completedProcessing =
          slaBreachProcessingRepository.findById(processing.getId()).orElseThrow();

      assertAll(
          () -> assertTrue(claimedData.isPresent()),
          () -> assertEquals(1, firstCompletedRows),
          () -> assertEquals(0, secondCompletedRows),
          () -> assertEquals(SlaBreachProcessingStatus.COMPLETED, completedProcessing.getStatus()),
          () -> assertEquals(firstProcessedAt, completedProcessing.getProcessedAt()));
    }

    @Test
    void shouldReturnZeroWhenSlaBreachProcessingDoesNotExist() {
      int completedRows =
          slaBreachProcessingRepository.markCompleted(
              Long.MAX_VALUE, LocalDateTime.of(2026, 10, 8, 9, 15));

      assertEquals(0, completedRows);
    }
  }

  @Nested
  class FindRecoverableTests {

    @Test
    void shouldFindOldPendingSlaBreachProcessingWithoutQueuedAt() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESPONSE);
      LocalDateTime cutoff = processing.getCreatedAt().plusMinutes(1);

      List<SlaBreachProcessing> recoverableProcessings =
          slaBreachProcessingRepository.findRecoverable(
              SlaBreachProcessingStatus.PENDING, cutoff);

      assertAll(
          () -> assertEquals(1, recoverableProcessings.size()),
          () -> assertEquals(processing.getId(), recoverableProcessings.getFirst().getId()),
          () ->
              assertEquals(
                  SlaBreachProcessingStatus.PENDING,
                  recoverableProcessings.getFirst().getStatus()),
          () -> assertNull(recoverableProcessings.getFirst().getQueuedAt()));
    }

    @Test
    void shouldNotFindSlaBreachProcessingCreatedExactlyAtCutoff() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESPONSE);

      List<SlaBreachProcessing> recoverableProcessings =
          slaBreachProcessingRepository.findRecoverable(
              SlaBreachProcessingStatus.PENDING, processing.getCreatedAt());

      assertTrue(recoverableProcessings.isEmpty());
    }

    @Test
    void shouldNotFindSlaBreachProcessingCreatedAfterCutoff() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESOLUTION);
      LocalDateTime cutoff = processing.getCreatedAt().minusMinutes(1);

      List<SlaBreachProcessing> recoverableProcessings =
          slaBreachProcessingRepository.findRecoverable(
              SlaBreachProcessingStatus.PENDING, cutoff);

      assertTrue(recoverableProcessings.isEmpty());
    }

    @Test
    void shouldNotFindSlaBreachProcessingThatWasAlreadyQueued() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESPONSE);
      LocalDateTime queuedAt = processing.getCreatedAt().plusSeconds(10);
      LocalDateTime cutoff = processing.getCreatedAt().plusMinutes(1);

      int updatedRows = slaBreachProcessingRepository.markQueued(processing.getId(), queuedAt);

      List<SlaBreachProcessing> recoverableProcessings =
          slaBreachProcessingRepository.findRecoverable(
              SlaBreachProcessingStatus.PENDING, cutoff);

      assertAll(
          () -> assertEquals(1, updatedRows),
          () -> assertTrue(recoverableProcessings.isEmpty()));
    }

    @Test
    void shouldOnlyFindSlaBreachProcessingWithRequestedStatus() {
      SlaBreachProcessing processing = createSlaBreachProcessing(SlaBreachType.RESOLUTION);
      LocalDateTime processingStartedAt = processing.getCreatedAt().plusSeconds(10);
      LocalDateTime cutoff = processing.getCreatedAt().plusMinutes(1);

      Optional<SlaBreachProcessingData> claimedData =
          slaBreachProcessingRepository.claimForProcessing(
              processing.getId(), processingStartedAt);

      List<SlaBreachProcessing> pendingProcessings =
          slaBreachProcessingRepository.findRecoverable(
              SlaBreachProcessingStatus.PENDING, cutoff);
      List<SlaBreachProcessing> processingProcessings =
          slaBreachProcessingRepository.findRecoverable(
              SlaBreachProcessingStatus.PROCESSING, cutoff);

      assertAll(
          () -> assertTrue(claimedData.isPresent()),
          () -> assertTrue(pendingProcessings.isEmpty()),
          () -> assertEquals(1, processingProcessings.size()),
          () -> assertEquals(processing.getId(), processingProcessings.getFirst().getId()));
    }
  }

  private TicketSla createTicketSla() {
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
        new TicketSla(
            organization,
            ticket,
            slaPolicy,
            ticket.getCreatedAt().plusMinutes(30),
            ticket.getCreatedAt().plusMinutes(240)));
  }

  private SlaBreachProcessing createSlaBreachProcessing(SlaBreachType breachType) {
    TicketSla ticketSla = createTicketSla();

    return slaBreachProcessingRepository.saveAndFlush(
        new SlaBreachProcessing(ticketSla.getOrganization(), ticketSla, breachType));
  }
}
