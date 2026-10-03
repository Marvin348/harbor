package com.harbor.server.features.tickets.sla.model;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.tickets.model.TicketPriority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sla_policies")
@Getter
@NoArgsConstructor
public class SlaPolicy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @Column(nullable = false, length = 100)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, name = "ticket_priority")
  private TicketPriority ticketPriority;

  @Column(nullable = false)
  private int responseTimeMinutes;

  @Column(nullable = false)
  private int resolutionTimeMinutes;

  @Column(nullable = false)
  private boolean enabled;

  public SlaPolicy(
      Organization organization,
      String name,
      TicketPriority ticketPriority,
      int responseTimeMinutes,
      int resolutionTimeMinutes) {
    this.organization = organization;
    this.name = name;
    this.ticketPriority = ticketPriority;
    this.responseTimeMinutes = responseTimeMinutes;
    this.resolutionTimeMinutes = resolutionTimeMinutes;
    enabled = true;
  }
}
