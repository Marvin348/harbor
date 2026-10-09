package com.harbor.server.integration.tickets.sla;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import com.harbor.server.features.tickets.sla.repository.SlaBreachProcessingRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import com.harbor.server.features.tickets.sla.scheduler.SlaBreachScheduler;
import com.harbor.server.features.user.model.User;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

class SlaBreachFlowIntegrationTest extends AbstractControllerIntegrationTest {

  @Autowired private SlaBreachProcessingRepository slaBreachProcessingRepository;
  @Autowired private TicketSlaRepository ticketSlaRepository;
  @Autowired private SlaBreachScheduler slaBreachScheduler;

  @Test
  void shouldProcessResponseBreachFromSchedulerToWorker() {
    LocalDateTime now = LocalDateTime.now();
    TicketSla ticketSla = createTicketSla(now.minusMinutes(1), now.plusHours(4));

    ReflectionTestUtils.invokeMethod(slaBreachScheduler, "enqueueBreachCandidates");

    await()
        .atMost(10, SECONDS)
        .untilAsserted(
            () -> {
              TicketSla updatedTicketSla =
                  ticketSlaRepository.findById(ticketSla.getId()).orElseThrow();
              List<SlaBreachProcessing> processingRecords = slaBreachProcessingRepository.findAll();

              assertEquals(1, processingRecords.size());

              SlaBreachProcessing processing = processingRecords.getFirst();
              assertEquals(SlaBreachType.RESPONSE, processing.getBreachType());
              assertEquals(SlaBreachProcessingStatus.COMPLETED, processing.getStatus());
              assertNotNull(processing.getQueuedAt());
              assertNotNull(processing.getProcessingStartedAt());
              assertNotNull(processing.getProcessedAt());
              assertNotNull(updatedTicketSla.getResponseBreachedAt());
              assertNull(updatedTicketSla.getResolutionBreachedAt());
            });
  }

  @Test
  void shouldProcessResolutionBreachFromSchedulerToWorker() {
    LocalDateTime now = LocalDateTime.now();
    TicketSla ticketSla = createTicketSla(now.plusHours(4), now.minusMinutes(1));

    ReflectionTestUtils.invokeMethod(slaBreachScheduler, "enqueueBreachCandidates");

    await()
        .atMost(10, SECONDS)
        .untilAsserted(
            () -> {
              TicketSla updatedTicketSla =
                  ticketSlaRepository.findById(ticketSla.getId()).orElseThrow();
              List<SlaBreachProcessing> processingRecords = slaBreachProcessingRepository.findAll();

              assertEquals(1, processingRecords.size());

              SlaBreachProcessing processing = processingRecords.getFirst();
              assertEquals(SlaBreachType.RESOLUTION, processing.getBreachType());
              assertEquals(SlaBreachProcessingStatus.COMPLETED, processing.getStatus());
              assertNotNull(processing.getQueuedAt());
              assertNotNull(processing.getProcessingStartedAt());
              assertNotNull(processing.getProcessedAt());
              assertNotNull(updatedTicketSla.getResolutionBreachedAt());
              assertNull(updatedTicketSla.getResponseBreachedAt());
            });
  }

  @Test
  void shouldNotCreateDuplicateProcessingWhenSchedulerRunsTwice() {
    LocalDateTime now = LocalDateTime.now();
    TicketSla ticketSla = createTicketSla(now.minusMinutes(1), now.plusHours(4));

    ReflectionTestUtils.invokeMethod(slaBreachScheduler, "enqueueBreachCandidates");
    ReflectionTestUtils.invokeMethod(slaBreachScheduler, "enqueueBreachCandidates");

    await()
        .atMost(10, SECONDS)
        .untilAsserted(
            () -> {
              TicketSla updatedTicketSla =
                  ticketSlaRepository.findById(ticketSla.getId()).orElseThrow();
              List<SlaBreachProcessing> processingRecords = slaBreachProcessingRepository.findAll();

              assertEquals(1, slaBreachProcessingRepository.count());

              SlaBreachProcessing processing = processingRecords.getFirst();
              assertEquals(SlaBreachType.RESPONSE, processing.getBreachType());
              assertEquals(SlaBreachProcessingStatus.COMPLETED, processing.getStatus());
              assertNotNull(processing.getQueuedAt());
              assertNotNull(processing.getProcessingStartedAt());
              assertNotNull(processing.getProcessedAt());
              assertNotNull(updatedTicketSla.getResponseBreachedAt());
              assertNull(updatedTicketSla.getResolutionBreachedAt());
            });
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

    return ticketSlaRepository.saveAndFlush(
        new TicketSla(organization, ticket, slaPolicy, responseDueAt, resolutionDueAt));
  }
}
