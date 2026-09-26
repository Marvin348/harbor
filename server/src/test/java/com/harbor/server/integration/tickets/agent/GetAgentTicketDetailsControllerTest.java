package com.harbor.server.integration.tickets.agent;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import org.junit.jupiter.api.Test;

public class GetAgentTicketDetailsControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldGetAgentTicketDetailsSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    Service service = testDataFactory.createService(organization, serviceTeam, "Workplace IT");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    User requester =
        testDataFactory.createUser(
            "Anna",
            "Schneider",
            "anna.schneider@example.com",
            OrganizationRole.REQUESTER,
            organization);
    Ticket ticket =
        testDataFactory.createTicket(
            requester,
            service,
            "VPN connection fails regularly",
            TicketStatus.OPEN,
            TicketPriority.HIGH);

    mockMvc
        .perform(get("/tickets/{id}/agent", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ticket.getId()))
        .andExpect(jsonPath("$.subject").value("VPN connection fails regularly"))
        .andExpect(jsonPath("$.description").value("This is the description for the ticket"))
        .andExpect(jsonPath("$.status").value("OPEN"))
        .andExpect(jsonPath("$.priority").value("HIGH"))
        .andExpect(jsonPath("$.impact").value("SINGLE_USER"))
        .andExpect(jsonPath("$.urgency").value("WORK_DEGRADED"))
        .andExpect(jsonPath("$.businessCriticality").value("NORMAL"))
        .andExpect(jsonPath("$.requesterId").value(requester.getId()))
        .andExpect(jsonPath("$.requesterName").value("Anna Schneider"))
            .andExpect(jsonPath("$.requesterEmail").value("anna.schneider@example.com"))
        .andExpect(jsonPath("$.serviceId").value(service.getId()))
        .andExpect(jsonPath("$.serviceName").value("Workplace IT"))
        .andExpect(jsonPath("$.serviceTeamId").value(serviceTeam.getId()))
        .andExpect(jsonPath("$.serviceTeamName").value("IT Support"))
        .andExpect(jsonPath("$.assignedAgentName").doesNotExist())
        .andExpect(jsonPath("$.createdAt").isNotEmpty())
        .andExpect(jsonPath("$.updatedAt").isNotEmpty());
  }

  @Test
  void shouldAllowOrganizationAdminWithoutServiceTeamMembership() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    Service service = testDataFactory.createService(organization, serviceTeam, "Workplace IT");
    User requester =
        testDataFactory.createUser(
            "Anna",
            "Schneider",
            "admin-ticket.requester@example.com",
            OrganizationRole.REQUESTER,
            organization);
    Ticket ticket =
        testDataFactory.createTicket(
            requester, service, "Admin visible ticket", TicketStatus.OPEN, TicketPriority.MEDIUM);

    mockMvc
        .perform(get("/tickets/{id}/agent", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ticket.getId()))
        .andExpect(jsonPath("$.subject").value("Admin visible ticket"));
  }

  @Test
  void shouldReturnNotFoundForUnknownTicket() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/agent", 9_999_999L).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Ticket Not Found"));
  }

  @Test
  void shouldHideTicketFromAnotherOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization otherOrganization = testDataFactory.createOrganization();
    ServiceTeam otherServiceTeam =
        testDataFactory.createServiceTeam(otherOrganization, "Other Organization Team");
    Service otherService =
        testDataFactory.createService(otherOrganization, otherServiceTeam, "Other Service");
    User otherRequester =
        testDataFactory.createUser(
            "Foreign",
            "Requester",
            "foreign.agent-details@example.com",
            OrganizationRole.REQUESTER,
            otherOrganization);
    Ticket otherTicket =
        testDataFactory.createTicket(
            otherRequester,
            otherService,
            "Foreign ticket",
            TicketStatus.OPEN,
            TicketPriority.MEDIUM);

    mockMvc
        .perform(get("/tickets/{id}/agent", otherTicket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Ticket Not Found"));
  }

  @Test
  void shouldHideTicketWhenAgentIsNotServiceTeamMember() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Restricted Team");
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
            TicketPriority.MEDIUM);

    mockMvc
        .perform(get("/tickets/{id}/agent", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Ticket Not Found"));
  }

  @Test
  void shouldRejectRequester() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();

    mockMvc
        .perform(get("/tickets/{id}/agent", 1L).cookie(auth.sessionCookie()))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectNonPositiveTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/agent", 0).cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectNonNumericTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/agent", "invalid").cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc.perform(get("/tickets/{id}/agent", 1L)).andExpect(status().isUnauthorized());
  }
}
