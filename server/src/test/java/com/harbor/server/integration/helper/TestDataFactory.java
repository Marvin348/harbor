package com.harbor.server.integration.helper;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.serviceTeam.model.ServiceTeamMember;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamMemberRepository;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.services.repository.ServiceRepository;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketBusinessCriticality;
import com.harbor.server.features.tickets.model.TicketImpact;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import com.harbor.server.features.tickets.model.TicketUrgency;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class TestDataFactory {
  private final OrganizationRepository organizationRepository;
  private final UserRepository userRepository;
  private final ServiceTeamRepository serviceTeamRepository;
  private final ServiceTeamMemberRepository serviceTeamMemberRepository;
  private final ServiceRepository serviceRepository;
  private final TicketRepository ticketRepository;
  private final PasswordEncoder passwordEncoder;

  public Organization createOrganization() {
    return organizationRepository.save(new Organization("Harbor Test GmbH"));
  }

  public User createAdmin() {
    Organization organization = createOrganization();

    return createUser(
        "Max",
        "Mustermann",
        "max.mustermann@example.com",
        OrganizationRole.ORGANIZATION_ADMIN,
        organization);
  }

  public User createAgent() {
    Organization organization = createOrganization();

    return createUser(
        "Erika",
        "Musterfrau",
        "erika.musterfrau@example.com",
        OrganizationRole.AGENT,
        organization);
  }

  public User createRequester() {
    Organization organization = createOrganization();

    return createUser(
        "Rita",
        "Requester",
        "rita.requester@example.com",
        OrganizationRole.REQUESTER,
        organization);
  }

  public ServiceTeam createServiceTeam(Organization organization, String name) {
    return serviceTeamRepository.save(
        new ServiceTeam(name, organization, "Handles internal support requests"));
  }

  public Service createService(Organization organization, ServiceTeam serviceTeam, String name) {
    return serviceRepository.save(
        new Service(name, "This is the description for the service", organization, serviceTeam));
  }

  public ServiceTeamMember createServiceTeamMember(
      User user, ServiceTeam serviceTeam, Organization organization) {
    return serviceTeamMemberRepository.save(new ServiceTeamMember(user, serviceTeam, organization));
  }

  public Ticket createTicket(
      User requester,
      Service service,
      String subject,
      TicketStatus status,
      TicketPriority priority) {
    Ticket ticket =
        new Ticket(
            service.getOrganization(),
            service.getServiceTeam(),
            service,
            requester,
            subject,
            "This is the description for the ticket",
            priority,
            TicketImpact.SINGLE_USER,
            TicketUrgency.WORK_DEGRADED,
            TicketBusinessCriticality.NORMAL);
    ticket.setStatus(status);

    return ticketRepository.save(ticket);
  }

  public User createUser(
      String firstName,
      String lastName,
      String email,
      OrganizationRole role,
      Organization organization) {
    return userRepository.save(
        new User(
            firstName,
            lastName,
            email,
            passwordEncoder.encode("Password123!"),
            role,
            organization));
  }
}
