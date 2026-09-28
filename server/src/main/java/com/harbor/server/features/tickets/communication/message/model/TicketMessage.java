package com.harbor.server.features.tickets.communication.message.model;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_messages")
@Getter
@NoArgsConstructor
public class TicketMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ticket_id", nullable = false)
  private Ticket ticket;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TicketMessageType type;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String body;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

  public TicketMessage(
      Ticket ticket, Organization organization, User author, TicketMessageType type, String body) {
    this.ticket = ticket;
    this.organization = organization;
    this.author = author;
    this.type = type;
    this.body = body;
  }

  public TicketMessage(
      Ticket ticket,
      Organization organization,
      User author,
      TicketMessageType type,
      String body,
      LocalDateTime createdAt) {
    this(ticket, organization, author, type, body);
    this.createdAt = createdAt;
  }
}
