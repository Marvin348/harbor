package com.harbor.server.features.serviceTeam.model;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_team_members")
@Getter
@NoArgsConstructor
public class ServiceTeamMember {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "service_team_id", nullable = false)
  private ServiceTeam serviceTeam;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}
