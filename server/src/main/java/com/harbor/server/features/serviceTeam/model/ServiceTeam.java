package com.harbor.server.features.serviceTeam.model;

import com.harbor.server.features.organization.model.Organization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_teams")
@Getter
@NoArgsConstructor
public class ServiceTeam {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false, length = 100)
  private String name;

  @Setter
  @Column(nullable = false, length = 250)
  private String description;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(optional = false)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  public ServiceTeam(String name, Organization organization, String description) {
    this.name = name;
    this.description = description;
    this.organization = organization;
  }
}
