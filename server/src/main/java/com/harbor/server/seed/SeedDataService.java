package com.harbor.server.seed;

import static com.harbor.server.features.tickets.sla.service.CreateDefaultSlaPolicies.SLA_DEFAULTS;
import static com.harbor.server.features.tickets.sla.service.CreateDefaultSlaPolicies.policyNameFor;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.serviceTeam.model.ServiceTeamMember;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamMemberRepository;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import com.harbor.server.features.services.repository.ServiceRepository;
import com.harbor.server.features.tickets.communication.message.model.TicketMessage;
import com.harbor.server.features.tickets.communication.message.repository.TicketMessageRepository;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.model.TicketSla;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import com.harbor.server.features.tickets.sla.repository.TicketSlaRepository;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import com.harbor.server.seed.data.*;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeedDataService {

  private final OrganizationRepository organizationRepository;
  private final UserRepository userRepository;
  private final ServiceTeamRepository serviceTeamRepository;
  private final ServiceTeamMemberRepository serviceTeamMemberRepository;
  private final ServiceRepository serviceRepository;
  private final TicketRepository ticketRepository;
  private final TicketMessageRepository ticketMessageRepository;
  private final TicketSlaRepository ticketSlaRepository;
  private final SlaPolicyRepository slaPolicyRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.seed.enabled}")
  private boolean seedEnabled;

  @Transactional
  public void seed() {
    if (!seedEnabled) {
      throw new IllegalStateException("Database seeding is disabled");
    }

    clearDatabase();

    Organization organization = seedOrganizations();
    Map<TicketPriority, SlaPolicy> slaPoliciesByPriority = seedSlaPolicies(organization);

    List<User> users = seedUsers(organization);
    Map<String, User> usersByEmail =
        users.stream().collect(Collectors.toMap(User::getEmail, user -> user));

    List<ServiceTeam> serviceTeams = seedServiceTeams(organization);
    Map<String, ServiceTeam> serviceTeamsByName =
        serviceTeams.stream()
            .collect(Collectors.toMap(ServiceTeam::getName, serviceTeam -> serviceTeam));

    seedServiceTeamMembers(organization, serviceTeamsByName, usersByEmail);

    List<com.harbor.server.features.services.model.Service> services =
        seedServices(organization, serviceTeamsByName);
    Map<ServiceLookupKey, com.harbor.server.features.services.model.Service> servicesByNameAndTeam =
        services.stream()
            .collect(
                Collectors.toMap(
                    service ->
                        new ServiceLookupKey(service.getName(), service.getServiceTeam().getName()),
                    service -> service));

    Map<String, Ticket> ticketsByKey =
        seedTickets(organization, usersByEmail, servicesByNameAndTeam, serviceTeamsByName);

    seedTicketSlas(organization, ticketsByKey, slaPoliciesByPriority);
    seedTicketMessages(ticketsByKey, usersByEmail);

    System.out.println("Seed data service started...");
  }

  private void clearDatabase() {
    ticketMessageRepository.deleteAllInBatch();
    ticketSlaRepository.deleteAllInBatch();
    ticketRepository.deleteAllInBatch();
    slaPolicyRepository.deleteAllInBatch();
    serviceRepository.deleteAllInBatch();
    serviceTeamMemberRepository.deleteAllInBatch();
    serviceTeamRepository.deleteAllInBatch();
    userRepository.deleteAllInBatch();
    organizationRepository.deleteAllInBatch();
  }

  private Organization seedOrganizations() {
    Organization organization = new Organization(OrganizationSeedData.NAME);
    return organizationRepository.save(organization);
  }

  private Map<TicketPriority, SlaPolicy> seedSlaPolicies(Organization organization) {
    Map<TicketPriority, SlaPolicy> slaPoliciesByPriority =
        new EnumMap<>(TicketPriority.class);

    for (var entry : SLA_DEFAULTS.entrySet()) {
      var defaults = entry.getValue();

      SlaPolicy slaPolicy =
          new SlaPolicy(
              organization,
              policyNameFor(entry.getKey()),
              entry.getKey(),
              defaults.responseTimeMinutes(),
              defaults.resolutionTimeMinutes());

      slaPoliciesByPriority.put(entry.getKey(), slaPolicyRepository.save(slaPolicy));
    }

    return slaPoliciesByPriority;
  }

  private List<User> seedUsers(Organization organization) {
    List<User> users = new ArrayList<>();

    for (UserSeedData.UserSeed seed : UserSeedData.USERS) {

      User user =
          new User(
              seed.firstName(),
              seed.lastName(),
              seed.email(),
              passwordEncoder.encode(seed.password()),
              seed.role(),
              organization);

      users.add(userRepository.save(user));
    }
    return users;
  }

  private List<ServiceTeam> seedServiceTeams(Organization organization) {
    List<ServiceTeam> serviceTeams = new ArrayList<>();

    for (ServiceTeamSeedData.ServiceTeamSeed seed : ServiceTeamSeedData.SERVICE_TEAMS) {

      ServiceTeam serviceTeam = new ServiceTeam(seed.name(), organization, seed.description());

      serviceTeams.add(serviceTeamRepository.save(serviceTeam));
    }
    return serviceTeams;
  }

  private void seedServiceTeamMembers(
      Organization organization,
      Map<String, ServiceTeam> serviceTeamsByName,
      Map<String, User> usersByEmail) {
    for (ServiceTeamMembersSeedData.ServiceTeamMemberSeed seed :
        ServiceTeamMembersSeedData.SERVICE_TEAM_MEMBERS) {

      User user = usersByEmail.get(seed.userEmail());
      ServiceTeam serviceTeam = serviceTeamsByName.get(seed.serviceTeamName());

      ServiceTeamMember serviceTeamMember = new ServiceTeamMember(user, serviceTeam, organization);

      serviceTeamMemberRepository.save(serviceTeamMember);
    }
  }

  private List<com.harbor.server.features.services.model.Service> seedServices(
      Organization organization, Map<String, ServiceTeam> serviceTeamsByName) {
    List<com.harbor.server.features.services.model.Service> services = new ArrayList<>();

    for (ServiceSeedData.ServiceSeed seed : ServiceSeedData.SERVICES) {

      ServiceTeam serviceTeam = serviceTeamsByName.get(seed.serviceTeamName());

      com.harbor.server.features.services.model.Service service =
          new com.harbor.server.features.services.model.Service(
              seed.name(), seed.description(), organization, serviceTeam);

      services.add(serviceRepository.save(service));
    }
    return services;
  }

  private Map<String, Ticket> seedTickets(
      Organization organization,
      Map<String, User> usersByEmail,
      Map<ServiceLookupKey, com.harbor.server.features.services.model.Service>
          servicesByNameAndTeam,
      Map<String, ServiceTeam> serviceTeamsByName) {

    Map<String, Ticket> ticketsByKey = new HashMap<>();

    for (TicketSeedData.TicketSeed seed : TicketSeedData.TICKETS) {

      User requester = usersByEmail.get(seed.requesterEmail());
      ServiceTeam serviceTeam = serviceTeamsByName.get(seed.serviceTeamName());
      com.harbor.server.features.services.model.Service service =
          servicesByNameAndTeam.get(
              new ServiceLookupKey(seed.serviceName(), seed.serviceTeamName()));

      Ticket ticket =
          new Ticket(
              organization,
              serviceTeam,
              service,
              requester,
              seed.subject(),
              seed.description(),
              seed.priority(),
              seed.impact(),
              seed.urgency(),
              seed.businessCriticality(),
              seed.status(),
              seed.createdAt(),
              seed.updatedAt());

      Ticket savedTicket = ticketRepository.save(ticket);

      ticketsByKey.put(seed.key(), savedTicket);
    }

    return ticketsByKey;
  }

  private void seedTicketSlas(
      Organization organization,
      Map<String, Ticket> ticketsByKey,
      Map<TicketPriority, SlaPolicy> slaPoliciesByPriority) {
    for (Ticket ticket : ticketsByKey.values()) {
      SlaPolicy slaPolicy = slaPoliciesByPriority.get(ticket.getPriority());

      if (slaPolicy == null) {
        throw new IllegalStateException(
            "SLA policy for ticket priority " + ticket.getPriority() + " not found");
      }

      TicketSla ticketSla =
          new TicketSla(
              organization,
              ticket,
              slaPolicy,
              ticket.getCreatedAt().plusMinutes(slaPolicy.getResponseTimeMinutes()),
              ticket.getCreatedAt().plusMinutes(slaPolicy.getResolutionTimeMinutes()));

      ticketSlaRepository.save(ticketSla);
    }
  }

  private void seedTicketMessages(
      Map<String, Ticket> ticketsByKey, Map<String, User> usersByEmail) {
    for (TicketMessageSeedData.TicketMessageSeed seed : TicketMessageSeedData.TICKET_MESSAGES) {
      Ticket ticket = ticketsByKey.get(seed.ticketKey());
      User author = usersByEmail.get(seed.authorEmail());

      TicketMessage ticketMessage =
          new TicketMessage(
              ticket,
              ticket.getOrganization(),
              author,
              seed.type(),
              seed.body(),
              seed.createdAt());

      ticketMessageRepository.save(ticketMessage);
    }
  }

  private record ServiceLookupKey(String serviceName, String serviceTeamName) {}
}
