package com.harbor.server.integration.tickets.agent;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

public class GetAgentTicketHeaderControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldGetAgentTicketHeaderSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    Ticket ticket = createTicket(organization, "anna.header@example.com");
    testDataFactory.createServiceTeamMember(
        auth.user(), ticket.getServiceTeam(), organization);

    mockMvc
        .perform(patch("/tickets/{id}/claim", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get("/tickets/{id}/header", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ticket.getId()))
        .andExpect(jsonPath("$.subject").value("VPN connection fails regularly"))
        .andExpect(jsonPath("$.status").value("OPEN"))
        .andExpect(jsonPath("$.priority").value("HIGH"))
        .andExpect(jsonPath("$.assignedAgentName").value("Erika Musterfrau"))
        .andExpect(jsonPath("$.description").doesNotExist())
        .andExpect(jsonPath("$.requesterName").doesNotExist())
        .andExpect(jsonPath("$.serviceName").doesNotExist());
  }

  @Test
  void shouldReturnHeaderWithoutAssignedAgent() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    Ticket ticket = createTicket(organization, "unassigned.header@example.com");
    testDataFactory.createServiceTeamMember(
        auth.user(), ticket.getServiceTeam(), organization);

    mockMvc
        .perform(get("/tickets/{id}/header", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ticket.getId()))
        .andExpect(jsonPath("$.assignedAgentName").doesNotExist());
  }

  @Test
  void shouldAllowOrganizationAdminWithoutServiceTeamMembership() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Ticket ticket =
        createTicket(auth.user().getOrganization(), "admin.header.requester@example.com");

    mockMvc
        .perform(get("/tickets/{id}/header", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ticket.getId()))
        .andExpect(jsonPath("$.subject").value("VPN connection fails regularly"));
  }

  @Test
  void shouldReturnNotFoundForUnknownTicket() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/header", 9_999_999L).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Ticket not found"));
  }

  @Test
  void shouldHideTicketFromAnotherOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization otherOrganization = testDataFactory.createOrganization();
    Ticket otherTicket =
        createTicket(otherOrganization, "foreign.header.requester@example.com");

    mockMvc
        .perform(get("/tickets/{id}/header", otherTicket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Ticket not found"));
  }

  @Test
  void shouldHideTicketWhenAgentIsNotMemberOfTheTicketsServiceTeam() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam otherServiceTeam =
        testDataFactory.createServiceTeam(organization, "Other Support Team");
    testDataFactory.createServiceTeamMember(auth.user(), otherServiceTeam, organization);
    Ticket ticket = createTicket(organization, "restricted.header.requester@example.com");

    mockMvc
        .perform(get("/tickets/{id}/header", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Ticket not found"));
  }

  @Test
  void shouldRejectRequester() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();

    mockMvc
        .perform(get("/tickets/{id}/header", 1L).cookie(auth.sessionCookie()))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectNonPositiveTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/header", 0).cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectNonNumericTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/tickets/{id}/header", "invalid").cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc.perform(get("/tickets/{id}/header", 1L)).andExpect(status().isUnauthorized());
  }

  private Ticket createTicket(Organization organization, String requesterEmail) {
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    Service service = testDataFactory.createService(organization, serviceTeam, "Workplace IT");
    User requester =
        testDataFactory.createUser(
            "Anna",
            "Schneider",
            requesterEmail,
            OrganizationRole.REQUESTER,
            organization);

    return testDataFactory.createTicket(
        requester,
        service,
        "VPN connection fails regularly",
        TicketStatus.OPEN,
        TicketPriority.HIGH);
  }
}
