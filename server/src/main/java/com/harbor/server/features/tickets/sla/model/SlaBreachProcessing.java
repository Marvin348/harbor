package com.harbor.server.features.tickets.sla.model;

import com.harbor.server.features.organization.model.Organization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sla_breach_processing")
@Getter
@NoArgsConstructor
public class SlaBreachProcessing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ticket_sla_id", nullable = false)
  private TicketSla ticketSla;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private SlaBreachProcessingStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "breach_type", nullable = false)
  private SlaBreachType breachType;

  @Column(name = "queued_at", nullable = true)
  private LocalDateTime queuedAt;

  @Column(name = "processing_started_at", nullable = true)
  private LocalDateTime processingStartedAt;

  @Column(name = "processed_at", nullable = true)
  private LocalDateTime processedAt;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

  public SlaBreachProcessing(
      Organization organization, TicketSla ticketSla, SlaBreachType breachType) {
    this.organization = organization;
    this.ticketSla = ticketSla;
    this.breachType = breachType;
    status = SlaBreachProcessingStatus.PENDING;
    queuedAt = null;
    processedAt = null;
    processingStartedAt = null;
  }
}
