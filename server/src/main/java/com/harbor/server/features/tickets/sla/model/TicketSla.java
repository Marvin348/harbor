package com.harbor.server.features.tickets.sla.model;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.tickets.model.Ticket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_slas")
@Getter
@NoArgsConstructor
public class TicketSla {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ticket_id", nullable = false, unique = true)
  private Ticket ticket;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "sla_policy_id", nullable = false)
  private SlaPolicy slaPolicy;

  @Column(name = "response_due_at", nullable = false)
  private LocalDateTime responseDueAt;

  @Column(name = "resolution_due_at", nullable = false)
  private LocalDateTime resolutionDueAt;

  @Column(name = "first_responded_at", nullable = true)
  private LocalDateTime firstRespondedAt;

  @Column(name = "resolved_at", nullable = true)
  private LocalDateTime resolvedAt;

  @Column(name = "response_breached_at", nullable = true)
  private LocalDateTime responseBreachedAt;

  @Column(name = "resolution_breached_at", nullable = true)
  private LocalDateTime resolutionBreachedAt;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  public TicketSla(
      Organization organization,
      Ticket ticket,
      SlaPolicy slaPolicy,
      LocalDateTime responseDueAt,
      LocalDateTime resolutionDueAt) {
    this.organization = organization;
    this.ticket = ticket;
    this.slaPolicy = slaPolicy;
    this.responseDueAt = responseDueAt;
    this.resolutionDueAt = resolutionDueAt;
  }
}
