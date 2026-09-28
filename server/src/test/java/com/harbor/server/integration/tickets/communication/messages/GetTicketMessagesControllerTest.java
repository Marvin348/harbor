package com.harbor.server.integration.tickets.communication.messages;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.tickets.communication.message.model.TicketMessage;
import com.harbor.server.features.tickets.communication.message.model.TicketMessageType;
import com.harbor.server.features.tickets.communication.message.repository.TicketMessageRepository;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GetTicketMessagesControllerTest extends AbstractControllerIntegrationTest {

  @Autowired private TicketMessageRepository ticketMessageRepository;

  private Ticket createTicket(AuthenticatedTestUser auth) {
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    Service service = testDataFactory.createService(organization, serviceTeam, "Workplace IT");

    if (auth.user().getRole() == OrganizationRole.AGENT) {
      testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);
    }

    User requester =
        auth.user().getRole() == OrganizationRole.REQUESTER
            ? auth.user()
            : testDataFactory.createUser(
                "Anna",
                "Schneider",
                "anna.schneider@example.com",
                OrganizationRole.REQUESTER,
                organization);

    return testDataFactory.createTicket(
        requester,
        service,
        "VPN connection fails regularly",
        TicketStatus.OPEN,
        TicketPriority.HIGH);
  }

  @Test
  void shouldGetRequesterTicketMessagesSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Organization organization = auth.user().getOrganization();
    Ticket ticket = createTicket(auth);
    Ticket otherTicket =
        testDataFactory.createTicket(
            auth.user(),
            ticket.getService(),
            "Printer is offline",
            TicketStatus.OPEN,
            TicketPriority.MEDIUM);
    User agent =
        testDataFactory.createUser(
            "Daniel",
            "Weber",
            "daniel.weber@example.com",
            OrganizationRole.AGENT,
            organization);

    TicketMessage firstReply =
        ticketMessageRepository.save(
            new TicketMessage(
                ticket,
                organization,
                auth.user(),
                TicketMessageType.REPLY,
                "The VPN disconnects every few minutes."));
    ticketMessageRepository.save(
        new TicketMessage(
            ticket,
            organization,
            agent,
            TicketMessageType.INTERNAL_NOTE,
            "Check the gateway logs before responding."));
    TicketMessage secondReply =
        ticketMessageRepository.save(
            new TicketMessage(
                ticket,
                organization,
                agent,
                TicketMessageType.REPLY,
                "I am checking the connection logs now."));
    ticketMessageRepository.save(
        new TicketMessage(
            otherTicket,
            organization,
            auth.user(),
            TicketMessageType.REPLY,
            "This message belongs to another ticket."));

    mockMvc
        .perform(get("/tickets/{id}/messages", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(firstReply.getId()))
        .andExpect(jsonPath("$[0].authorId").value(auth.user().getId()))
        .andExpect(jsonPath("$[0].authorName").value("Rita Requester"))
        .andExpect(jsonPath("$[0].type").value("REPLY"))
        .andExpect(jsonPath("$[0].body").value("The VPN disconnects every few minutes."))
        .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
        .andExpect(jsonPath("$[1].id").value(secondReply.getId()))
        .andExpect(jsonPath("$[1].authorId").value(agent.getId()))
        .andExpect(jsonPath("$[1].authorName").value("Daniel Weber"))
        .andExpect(jsonPath("$[1].type").value("REPLY"))
        .andExpect(jsonPath("$[1].body").value("I am checking the connection logs now."));
  }

  @Test
  void shouldGetAgentTicketMessagesSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    Ticket ticket = createTicket(auth);
    Ticket otherTicket =
        testDataFactory.createTicket(
            ticket.getRequester(),
            ticket.getService(),
            "Laptop does not start",
            TicketStatus.OPEN,
            TicketPriority.HIGH);

    TicketMessage requesterReply =
        ticketMessageRepository.save(
            new TicketMessage(
                ticket,
                organization,
                ticket.getRequester(),
                TicketMessageType.REPLY,
                "The problem also occurs over a mobile hotspot."));
    TicketMessage internalNote =
        ticketMessageRepository.save(
            new TicketMessage(
                ticket,
                organization,
                auth.user(),
                TicketMessageType.INTERNAL_NOTE,
                "Possible gateway issue. Review recent incidents."));
    TicketMessage agentReply =
        ticketMessageRepository.save(
            new TicketMessage(
                ticket,
                organization,
                auth.user(),
                TicketMessageType.REPLY,
                "Thank you. I will review the gateway logs."));
    ticketMessageRepository.save(
        new TicketMessage(
            otherTicket,
            organization,
            ticket.getRequester(),
            TicketMessageType.REPLY,
            "This message must not be returned."));

    mockMvc
        .perform(get("/tickets/{id}/messages", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].id").value(requesterReply.getId()))
        .andExpect(jsonPath("$[0].authorName").value("Anna Schneider"))
        .andExpect(jsonPath("$[0].type").value("REPLY"))
        .andExpect(jsonPath("$[1].id").value(internalNote.getId()))
        .andExpect(jsonPath("$[1].authorId").value(auth.user().getId()))
        .andExpect(jsonPath("$[1].authorName").value("Erika Musterfrau"))
        .andExpect(jsonPath("$[1].type").value("INTERNAL_NOTE"))
        .andExpect(jsonPath("$[1].body").value("Possible gateway issue. Review recent incidents."))
        .andExpect(jsonPath("$[2].id").value(agentReply.getId()))
        .andExpect(jsonPath("$[2].type").value("REPLY"));
  }

  @Test
  void shouldGetAdminTicketMessagesSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization organization = auth.user().getOrganization();
    Ticket ticket = createTicket(auth);
    Ticket otherTicket =
        testDataFactory.createTicket(
            ticket.getRequester(),
            ticket.getService(),
            "Monitor remains black",
            TicketStatus.OPEN,
            TicketPriority.LOW);

    TicketMessage requesterReply =
        ticketMessageRepository.save(
            new TicketMessage(
                ticket,
                organization,
                ticket.getRequester(),
                TicketMessageType.REPLY,
                "The VPN issue still occurs."));
    TicketMessage internalNote =
        ticketMessageRepository.save(
            new TicketMessage(
                ticket,
                organization,
                auth.user(),
                TicketMessageType.INTERNAL_NOTE,
                "Escalate if the next reconnect fails."));
    ticketMessageRepository.save(
        new TicketMessage(
            otherTicket,
            organization,
            ticket.getRequester(),
            TicketMessageType.REPLY,
            "Message from another ticket."));

    mockMvc
        .perform(get("/tickets/{id}/messages", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(requesterReply.getId()))
        .andExpect(jsonPath("$[0].type").value("REPLY"))
        .andExpect(jsonPath("$[1].id").value(internalNote.getId()))
        .andExpect(jsonPath("$[1].authorName").value("Max Mustermann"))
        .andExpect(jsonPath("$[1].type").value("INTERNAL_NOTE"));
  }

  @Test
  void shouldReturnEmptyListWhenAccessibleTicketHasNoMessages() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Ticket ticket = createTicket(auth);

    mockMvc
        .perform(get("/tickets/{id}/messages", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void shouldHideAnotherRequestersTicket() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Ticket ownTicket = createTicket(auth);
    User otherRequester =
        testDataFactory.createUser(
            "Other",
            "Requester",
            "other.requester@example.com",
            OrganizationRole.REQUESTER,
            auth.user().getOrganization());
    Ticket otherTicket =
        testDataFactory.createTicket(
            otherRequester,
            ownTicket.getService(),
            "Other requester ticket",
            TicketStatus.OPEN,
            TicketPriority.MEDIUM);

    mockMvc
        .perform(get("/tickets/{id}/messages", otherTicket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldHideTicketFromAnotherOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    createTicket(auth);
    Organization otherOrganization = testDataFactory.createOrganization();
    ServiceTeam otherTeam = testDataFactory.createServiceTeam(otherOrganization, "Other IT Support");
    Service otherService =
        testDataFactory.createService(otherOrganization, otherTeam, "Other Workplace IT");
    User otherRequester =
        testDataFactory.createUser(
            "Foreign",
            "Requester",
            "foreign.requester@example.com",
            OrganizationRole.REQUESTER,
            otherOrganization);
    Ticket otherTicket =
        testDataFactory.createTicket(
            otherRequester,
            otherService,
            "Foreign organization ticket",
            TicketStatus.OPEN,
            TicketPriority.HIGH);

    mockMvc
        .perform(get("/tickets/{id}/messages", otherTicket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldHideTicketWhenAgentIsNotServiceTeamMember() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Restricted Support");
    Service service = testDataFactory.createService(organization, serviceTeam, "Restricted Service");
    User requester =
        testDataFactory.createUser(
            "Restricted",
            "Requester",
            "restricted.requester@example.com",
            OrganizationRole.REQUESTER,
            organization);
    Ticket ticket =
        testDataFactory.createTicket(
            requester,
            service,
            "Restricted ticket",
            TicketStatus.OPEN,
            TicketPriority.HIGH);

    mockMvc
        .perform(get("/tickets/{id}/messages", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldRejectNonPositiveTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/messages", 0).cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectNonNumericTicket() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/messages", "invalid").cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnNotFoundForUnknownTicket() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/messages", 99_999_999L).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc.perform(get("/tickets/{id}/messages", 1L)).andExpect(status().isUnauthorized());
  }
}
