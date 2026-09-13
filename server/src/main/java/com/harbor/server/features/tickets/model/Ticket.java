package com.harbor.server.features.tickets.model;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@NoArgsConstructor
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne(optional = false)
  @JoinColumn(name = "requester_id", nullable = false)
  private User requester;

  @ManyToOne(optional = false)
  @JoinColumn(name = "service_id", nullable = false)
  private Service service;

  @ManyToOne(optional = false)
  @JoinColumn(name = "service_team_id")
  private ServiceTeam serviceTeam;

  @ManyToOne
  @JoinColumn(name = "assigned_agent_id")
  private User assignedAgent;

  @Setter
  @Column(nullable = false, length = 150)
  private String subject;

  @Setter
  @Column(length = 2000)
  private String description;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TicketImpact impact;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TicketUrgency urgency;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(name = "business_criticality", nullable = false)
  private TicketBusinessCriticality businessCriticality;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column
  // TODO(Harbor): Replace hardcoded priority with matrix calculation service
  private TicketPriority priority;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TicketStatus status;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public Ticket(
      Organization organization,
      ServiceTeam serviceTeam,
      Service service,
      User requester,
      String subject,
      String description,
      TicketImpact impact,
      TicketUrgency urgency,
      TicketBusinessCriticality businessCriticality) {
    this.organization = organization;
    this.serviceTeam = serviceTeam;
    this.service = service;
    this.requester = requester;
    this.subject = subject;
    this.description = description;
    this.impact = impact;
    this.urgency = urgency;
    this.businessCriticality = businessCriticality;
    this.status = TicketStatus.OPEN;
  }
}
