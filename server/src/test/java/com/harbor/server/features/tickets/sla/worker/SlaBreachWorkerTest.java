package com.harbor.server.features.tickets.sla.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessingStatus;
import com.harbor.server.features.tickets.sla.model.SlaBreachType;
import com.harbor.server.features.tickets.sla.projection.SlaBreachProcessingData;
import com.harbor.server.features.tickets.sla.repository.SlaBreachProcessingRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlaBreachWorkerTest {

  @Mock private TicketSlaRepository ticketSlaRepository;
  @Mock private SlaBreachProcessingRepository slaBreachProcessingRepository;
  @InjectMocks private SlaBreachWorker slaBreachWorker;

  @Test
  void shouldStopProcessingWhenJobCannotBeClaimed() {
    long processingId = 42L;
    SlaBreachJob job = new SlaBreachJob(processingId);

    when(slaBreachProcessingRepository.claimForProcessing(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(Optional.empty());

    slaBreachWorker.process(job);

    verify(slaBreachProcessingRepository)
        .claimForProcessing(eq(processingId), any(LocalDateTime.class));
    verifyNoMoreInteractions(slaBreachProcessingRepository);
    verifyNoInteractions(ticketSlaRepository);
  }

  @Test
  void shouldProcessResponseBreachAndCompleteProcessingRecord() {
    long processingId = 42L;
    long ticketSlaId = 100L;
    SlaBreachJob job = new SlaBreachJob(processingId);
    SlaBreachProcessingData processingData =
        new SlaBreachProcessingData(
            ticketSlaId, SlaBreachType.RESPONSE, SlaBreachProcessingStatus.PROCESSING);

    when(slaBreachProcessingRepository.claimForProcessing(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(Optional.of(processingData));
    when(ticketSlaRepository.markResponseBreached(
            eq(ticketSlaId), any(LocalDateTime.class)))
        .thenReturn(1);
    when(slaBreachProcessingRepository.markCompleted(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachWorker.process(job);

    InOrder callOrder = inOrder(slaBreachProcessingRepository, ticketSlaRepository);
    ArgumentCaptor<LocalDateTime> nowCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
    callOrder
        .verify(slaBreachProcessingRepository)
        .claimForProcessing(eq(processingId), nowCaptor.capture());
    LocalDateTime usedNow = nowCaptor.getValue();
    callOrder.verify(ticketSlaRepository).markResponseBreached(ticketSlaId, usedNow);
    callOrder.verify(slaBreachProcessingRepository).markCompleted(processingId, usedNow);
    verifyNoMoreInteractions(slaBreachProcessingRepository, ticketSlaRepository);
  }

  @Test
  void shouldProcessResolutionBreachAndCompleteProcessingRecord() {
    long processingId = 42L;
    long ticketSlaId = 100L;
    SlaBreachJob job = new SlaBreachJob(processingId);
    SlaBreachProcessingData processingData =
        new SlaBreachProcessingData(
            ticketSlaId, SlaBreachType.RESOLUTION, SlaBreachProcessingStatus.PROCESSING);

    when(slaBreachProcessingRepository.claimForProcessing(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(Optional.of(processingData));
    when(ticketSlaRepository.markResolutionBreached(
            eq(ticketSlaId), any(LocalDateTime.class)))
        .thenReturn(1);
    when(slaBreachProcessingRepository.markCompleted(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachWorker.process(job);

    InOrder callOrder = inOrder(slaBreachProcessingRepository, ticketSlaRepository);
    ArgumentCaptor<LocalDateTime> nowCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
    callOrder
        .verify(slaBreachProcessingRepository)
        .claimForProcessing(eq(processingId), nowCaptor.capture());
    LocalDateTime usedNow = nowCaptor.getValue();
    callOrder.verify(ticketSlaRepository).markResolutionBreached(ticketSlaId, usedNow);
    callOrder.verify(slaBreachProcessingRepository).markCompleted(processingId, usedNow);
    verifyNoMoreInteractions(slaBreachProcessingRepository, ticketSlaRepository);
  }

  @Test
  void shouldCompleteProcessingWhenBreachUpdateAffectsNoRows() {
    long processingId = 42L;
    long ticketSlaId = 100L;
    SlaBreachJob job = new SlaBreachJob(processingId);
    SlaBreachProcessingData processingData =
        new SlaBreachProcessingData(
            ticketSlaId, SlaBreachType.RESPONSE, SlaBreachProcessingStatus.PROCESSING);

    when(slaBreachProcessingRepository.claimForProcessing(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(Optional.of(processingData));
    when(ticketSlaRepository.markResponseBreached(
            eq(ticketSlaId), any(LocalDateTime.class)))
        .thenReturn(0);
    when(slaBreachProcessingRepository.markCompleted(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachWorker.process(job);

    verify(ticketSlaRepository)
        .markResponseBreached(eq(ticketSlaId), any(LocalDateTime.class));
    verify(slaBreachProcessingRepository)
        .markCompleted(eq(processingId), any(LocalDateTime.class));
  }

  @Test
  void shouldThrowWhenProcessingRecordCannotBeCompleted() {
    long processingId = 42L;
    long ticketSlaId = 100L;
    SlaBreachJob job = new SlaBreachJob(processingId);
    SlaBreachProcessingData processingData =
        new SlaBreachProcessingData(
            ticketSlaId, SlaBreachType.RESPONSE, SlaBreachProcessingStatus.PROCESSING);

    when(slaBreachProcessingRepository.claimForProcessing(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(Optional.of(processingData));
    when(ticketSlaRepository.markResponseBreached(
            eq(ticketSlaId), any(LocalDateTime.class)))
        .thenReturn(1);
    when(slaBreachProcessingRepository.markCompleted(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(0);

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> slaBreachWorker.process(job));

    assertEquals(
        "Expected exactly one SlaBreachProcessing to be completed", exception.getMessage());
  }

  @Test
  void shouldNotCompleteProcessingWhenMarkingBreachFails() {
    long processingId = 42L;
    long ticketSlaId = 100L;
    SlaBreachJob job = new SlaBreachJob(processingId);
    SlaBreachProcessingData processingData =
        new SlaBreachProcessingData(
            ticketSlaId, SlaBreachType.RESPONSE, SlaBreachProcessingStatus.PROCESSING);
    RuntimeException expectedException = new RuntimeException("Database error");

    when(slaBreachProcessingRepository.claimForProcessing(
            eq(processingId), any(LocalDateTime.class)))
        .thenReturn(Optional.of(processingData));
    when(ticketSlaRepository.markResponseBreached(
            eq(ticketSlaId), any(LocalDateTime.class)))
        .thenThrow(expectedException);

    RuntimeException actualException =
        assertThrows(RuntimeException.class, () -> slaBreachWorker.process(job));

    assertSame(expectedException, actualException);
    verify(slaBreachProcessingRepository)
        .claimForProcessing(eq(processingId), any(LocalDateTime.class));
    verifyNoMoreInteractions(slaBreachProcessingRepository);
  }
}
