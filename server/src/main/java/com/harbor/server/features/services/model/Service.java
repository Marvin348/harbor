package com.harbor.server.features.services.model;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Getter
@NoArgsConstructor
public class Service {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false, length = 100)
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(name = "organization_id")
  private Organization organization;

  @ManyToOne(optional = false)
  @JoinColumn(name = "service_team_id")
  private ServiceTeam serviceTeam;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ServiceStatus status;

  @Setter
  @Column(length = 250)
  private String description;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  public Service(
      String name, String description, Organization organization, ServiceTeam serviceTeam) {
    this.name = name;
    this.description = description;
    this.organization = organization;
    this.serviceTeam = serviceTeam;
    this.status = ServiceStatus.DRAFT;
  }
}
