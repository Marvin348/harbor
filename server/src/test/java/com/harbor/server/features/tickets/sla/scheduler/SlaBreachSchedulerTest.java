package com.harbor.server.features.tickets.sla.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.messaging.SlaBreachProducer;
import com.harbor.server.features.tickets.sla.model.SlaBreachType;
import com.harbor.server.features.tickets.sla.projection.SlaBreachCandidate;
import com.harbor.server.features.tickets.sla.repository.SlaBreachProcessingRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlaBreachSchedulerTest {

  @Mock private TicketSlaRepository ticketSlaRepository;
  @Mock private SlaBreachProcessingRepository slaBreachProcessingRepository;
  @Mock private SlaBreachProducer slaBreachProducer;
  @InjectMocks private SlaBreachScheduler slaBreachScheduler;

  @Test
  void shouldEnqueueResponseBreachCandidate() {
    long ticketSlaId = 42L;
    long organizationId = 7L;
    long processingId = 100L;
    SlaBreachCandidate candidate = new SlaBreachCandidate(ticketSlaId, organizationId);

    when(ticketSlaRepository.findResponseBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of(candidate));
    when(ticketSlaRepository.findResolutionBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of());
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(ticketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class)))
        .thenReturn(Optional.of(processingId));
    when(slaBreachProcessingRepository.markQueued(eq(processingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachScheduler.enqueueBreachCandidates();

    InOrder callOrder =
        inOrder(ticketSlaRepository, slaBreachProcessingRepository, slaBreachProducer);
    ArgumentCaptor<LocalDateTime> nowCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
    callOrder.verify(ticketSlaRepository).findResponseBreachCandidateIds(nowCaptor.capture());
    LocalDateTime usedNow = nowCaptor.getValue();

    callOrder
        .verify(slaBreachProcessingRepository)
        .createIfAbsent(organizationId, ticketSlaId, SlaBreachType.RESPONSE, usedNow);
    callOrder.verify(slaBreachProducer).send(new SlaBreachJob(processingId));
    callOrder.verify(slaBreachProcessingRepository).markQueued(processingId, usedNow);
    callOrder.verify(ticketSlaRepository).findResolutionBreachCandidateIds(usedNow);
  }

  @Test
  void shouldEnqueueResolutionBreachCandidate() {
    long ticketSlaId = 43L;
    long organizationId = 7L;
    long processingId = 101L;
    SlaBreachCandidate candidate = new SlaBreachCandidate(ticketSlaId, organizationId);

    when(ticketSlaRepository.findResolutionBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of(candidate));
    when(ticketSlaRepository.findResponseBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of());
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(ticketSlaId),
            eq(SlaBreachType.RESOLUTION),
            any(LocalDateTime.class)))
        .thenReturn(Optional.of(processingId));
    when(slaBreachProcessingRepository.markQueued(eq(processingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachScheduler.enqueueBreachCandidates();

    InOrder callOrder =
        inOrder(ticketSlaRepository, slaBreachProcessingRepository, slaBreachProducer);

    ArgumentCaptor<LocalDateTime> nowCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
    callOrder.verify(ticketSlaRepository).findResponseBreachCandidateIds(nowCaptor.capture());
    LocalDateTime usedNow = nowCaptor.getValue();

    callOrder.verify(ticketSlaRepository).findResolutionBreachCandidateIds(usedNow);
    callOrder
        .verify(slaBreachProcessingRepository)
        .createIfAbsent(organizationId, ticketSlaId, SlaBreachType.RESOLUTION, usedNow);
    callOrder.verify(slaBreachProducer).send(new SlaBreachJob(processingId));
    callOrder.verify(slaBreachProcessingRepository).markQueued(processingId, usedNow);
  }

  @Test
  void shouldContinueWithNextCandidateWhenProcessingRecordAlreadyExists() {
    long organizationId = 7L;
    long existingTicketSlaId = 43L;
    long newTicketSlaId = 44L;
    long newProcessingId = 101L;
    SlaBreachCandidate existingCandidate =
        new SlaBreachCandidate(existingTicketSlaId, organizationId);
    SlaBreachCandidate newCandidate = new SlaBreachCandidate(newTicketSlaId, organizationId);

    when(ticketSlaRepository.findResponseBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of(existingCandidate, newCandidate));
    when(ticketSlaRepository.findResolutionBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of());
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(existingTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class)))
        .thenReturn(Optional.empty());
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(newTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class)))
        .thenReturn(Optional.of(newProcessingId));
    when(slaBreachProcessingRepository.markQueued(eq(newProcessingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachScheduler.enqueueBreachCandidates();

    verify(slaBreachProcessingRepository)
        .createIfAbsent(
            eq(organizationId),
            eq(existingTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class));
    verify(slaBreachProcessingRepository)
        .createIfAbsent(
            eq(organizationId),
            eq(newTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class));
    verify(slaBreachProducer).send(new SlaBreachJob(newProcessingId));
    verifyNoMoreInteractions(slaBreachProducer);
    verify(slaBreachProcessingRepository).markQueued(eq(newProcessingId), any(LocalDateTime.class));
  }

  @Test
  void shouldThrowWhenProcessingRecordCannotBeMarkedAsQueued() {
    long ticketSlaId = 43L;
    long organizationId = 7L;
    long processingId = 101L;
    SlaBreachCandidate candidate = new SlaBreachCandidate(ticketSlaId, organizationId);

    when(ticketSlaRepository.findResolutionBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of(candidate));
    when(ticketSlaRepository.findResponseBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of());
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(ticketSlaId),
            eq(SlaBreachType.RESOLUTION),
            any(LocalDateTime.class)))
        .thenReturn(Optional.of(processingId));
    when(slaBreachProcessingRepository.markQueued(eq(processingId), any(LocalDateTime.class)))
        .thenReturn(0);

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class, () -> slaBreachScheduler.enqueueBreachCandidates());

    assertEquals(
        "Expected exactly one SlaBreachProcessing to be marked as queued", exception.getMessage());
  }

  @Test
  void shouldenqueueMultipleCandidates() {
    long organizationId = 7L;
    long oneTicketSlaId = 42L;
    long twoTicketSlaId = 43L;
    long threeTicketSlaId = 44L;
    long fourTicketSlaId = 55L;

    long oneProcessingId = 101L;
    long twoProcessingId = 102L;
    long threeProcessingId = 103L;
    long fourProcessingId = 104L;

    SlaBreachCandidate oneCandidate = new SlaBreachCandidate(oneTicketSlaId, organizationId);
    SlaBreachCandidate twoCandidate = new SlaBreachCandidate(twoTicketSlaId, organizationId);
    SlaBreachCandidate threeCandidate = new SlaBreachCandidate(threeTicketSlaId, organizationId);
    SlaBreachCandidate fourCandidate = new SlaBreachCandidate(fourTicketSlaId, organizationId);

    when(ticketSlaRepository.findResponseBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of(oneCandidate, twoCandidate, threeCandidate, fourCandidate));
    when(ticketSlaRepository.findResolutionBreachCandidateIds(any(LocalDateTime.class)))
        .thenReturn(List.of());
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(oneTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class)))
        .thenReturn(Optional.of(oneProcessingId));
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(twoTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class)))
        .thenReturn(Optional.of(twoProcessingId));
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(threeTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class)))
        .thenReturn(Optional.of(threeProcessingId));
    when(slaBreachProcessingRepository.createIfAbsent(
            eq(organizationId),
            eq(fourTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class)))
        .thenReturn(Optional.of(fourProcessingId));
    when(slaBreachProcessingRepository.markQueued(eq(oneProcessingId), any(LocalDateTime.class)))
        .thenReturn(1);
    when(slaBreachProcessingRepository.markQueued(eq(twoProcessingId), any(LocalDateTime.class)))
        .thenReturn(1);
    when(slaBreachProcessingRepository.markQueued(eq(threeProcessingId), any(LocalDateTime.class)))
        .thenReturn(1);
    when(slaBreachProcessingRepository.markQueued(eq(fourProcessingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachScheduler.enqueueBreachCandidates();

    verify(slaBreachProcessingRepository)
        .createIfAbsent(
            eq(organizationId),
            eq(oneTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class));
    verify(slaBreachProcessingRepository)
        .createIfAbsent(
            eq(organizationId),
            eq(twoTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class));
    verify(slaBreachProcessingRepository)
        .createIfAbsent(
            eq(organizationId),
            eq(threeTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class));
    verify(slaBreachProcessingRepository)
        .createIfAbsent(
            eq(organizationId),
            eq(fourTicketSlaId),
            eq(SlaBreachType.RESPONSE),
            any(LocalDateTime.class));

    verify(slaBreachProducer).send(new SlaBreachJob(oneProcessingId));
    verify(slaBreachProducer).send(new SlaBreachJob(twoProcessingId));
    verify(slaBreachProducer).send(new SlaBreachJob(threeProcessingId));
    verify(slaBreachProducer).send(new SlaBreachJob(fourProcessingId));

    verify(slaBreachProcessingRepository).markQueued(eq(oneProcessingId), any(LocalDateTime.class));
    verify(slaBreachProcessingRepository).markQueued(eq(twoProcessingId), any(LocalDateTime.class));
    verify(slaBreachProcessingRepository)
        .markQueued(eq(threeProcessingId), any(LocalDateTime.class));
    verify(slaBreachProcessingRepository)
        .markQueued(eq(fourProcessingId), any(LocalDateTime.class));
  }
}
