package com.harbor.server.features.tickets.sla.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.harbor.server.features.tickets.sla.messaging.SlaBreachJob;
import com.harbor.server.features.tickets.sla.messaging.SlaBreachProducer;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessing;
import com.harbor.server.features.tickets.sla.model.SlaBreachProcessingStatus;
import com.harbor.server.features.tickets.sla.repository.SlaBreachProcessingRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlaBreachRecoverySchedulerTest {

  @Mock private SlaBreachProcessingRepository slaBreachProcessingRepository;
  @Mock private SlaBreachProducer slaBreachProducer;
  @InjectMocks private SlaBreachRecoveryScheduler slaBreachRecoveryScheduler;

  @Test
  void shouldStopWhenNoRecoverableProcessingRecordsExist() {
    when(slaBreachProcessingRepository.findRecoverable(
            eq(SlaBreachProcessingStatus.PENDING), any(LocalDateTime.class)))
        .thenReturn(List.of());

    slaBreachRecoveryScheduler.processPendingSlaBreaches();

    verify(slaBreachProcessingRepository)
        .findRecoverable(eq(SlaBreachProcessingStatus.PENDING), any(LocalDateTime.class));
    verifyNoMoreInteractions(slaBreachProcessingRepository);
    verifyNoInteractions(slaBreachProducer);
  }

  @Test
  void shouldRequeueRecoverableProcessingRecord() {
    long processingId = 42L;
    SlaBreachProcessing breach = mock(SlaBreachProcessing.class);
    when(breach.getId()).thenReturn(processingId);
    when(slaBreachProcessingRepository.findRecoverable(
            eq(SlaBreachProcessingStatus.PENDING), any(LocalDateTime.class)))
        .thenReturn(List.of(breach));
    when(slaBreachProcessingRepository.markQueued(eq(processingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachRecoveryScheduler.processPendingSlaBreaches();

    InOrder callOrder = inOrder(slaBreachProcessingRepository, slaBreachProducer);
    ArgumentCaptor<LocalDateTime> cutoffCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
    ArgumentCaptor<LocalDateTime> queuedAtCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
    callOrder
        .verify(slaBreachProcessingRepository)
        .findRecoverable(eq(SlaBreachProcessingStatus.PENDING), cutoffCaptor.capture());
    callOrder.verify(slaBreachProducer).send(new SlaBreachJob(processingId));
    callOrder
        .verify(slaBreachProcessingRepository)
        .markQueued(eq(processingId), queuedAtCaptor.capture());

    assertEquals(cutoffCaptor.getValue().plusMinutes(2), queuedAtCaptor.getValue());
    verifyNoMoreInteractions(slaBreachProcessingRepository, slaBreachProducer);
  }

  @Test
  void shouldRequeueMultipleRecoverableProcessingRecords() {
    long firstProcessingId = 42L;
    long secondProcessingId = 43L;
    SlaBreachProcessing firstBreach = mock(SlaBreachProcessing.class);
    SlaBreachProcessing secondBreach = mock(SlaBreachProcessing.class);
    when(firstBreach.getId()).thenReturn(firstProcessingId);
    when(secondBreach.getId()).thenReturn(secondProcessingId);
    when(slaBreachProcessingRepository.findRecoverable(
            eq(SlaBreachProcessingStatus.PENDING), any(LocalDateTime.class)))
        .thenReturn(List.of(firstBreach, secondBreach));
    when(slaBreachProcessingRepository.markQueued(eq(firstProcessingId), any(LocalDateTime.class)))
        .thenReturn(1);
    when(slaBreachProcessingRepository.markQueued(eq(secondProcessingId), any(LocalDateTime.class)))
        .thenReturn(1);

    slaBreachRecoveryScheduler.processPendingSlaBreaches();

    InOrder callOrder = inOrder(slaBreachProcessingRepository, slaBreachProducer);
    callOrder
        .verify(slaBreachProcessingRepository)
        .findRecoverable(eq(SlaBreachProcessingStatus.PENDING), any(LocalDateTime.class));
    callOrder.verify(slaBreachProducer).send(new SlaBreachJob(firstProcessingId));
    callOrder
        .verify(slaBreachProcessingRepository)
        .markQueued(eq(firstProcessingId), any(LocalDateTime.class));
    callOrder.verify(slaBreachProducer).send(new SlaBreachJob(secondProcessingId));
    callOrder
        .verify(slaBreachProcessingRepository)
        .markQueued(eq(secondProcessingId), any(LocalDateTime.class));
    verifyNoMoreInteractions(slaBreachProcessingRepository, slaBreachProducer);
  }

  @Test
  void shouldNotMarkProcessingRecordAsQueuedWhenSendingFails() {
    long processingId = 42L;
    SlaBreachProcessing breach = mock(SlaBreachProcessing.class);
    RuntimeException expectedException = new RuntimeException("RabbitMQ unavailable");
    when(breach.getId()).thenReturn(processingId);
    when(slaBreachProcessingRepository.findRecoverable(
            eq(SlaBreachProcessingStatus.PENDING), any(LocalDateTime.class)))
        .thenReturn(List.of(breach));
    doThrow(expectedException).when(slaBreachProducer).send(new SlaBreachJob(processingId));

    RuntimeException actualException =
        assertThrows(
            RuntimeException.class, () -> slaBreachRecoveryScheduler.processPendingSlaBreaches());

    assertSame(expectedException, actualException);
    verify(slaBreachProcessingRepository, never()).markQueued(anyLong(), any(LocalDateTime.class));
  }
}
