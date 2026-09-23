package com.harbor.server.integration.tickets;

import static org.hamcrest.Matchers.hasSize;
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

public class GetServiceTeamTicketsControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldGetServiceTeamTicketsSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    Service service = testDataFactory.createService(organization, serviceTeam, "Test Service");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    User requester =
        testDataFactory.createUser(
            "Paul", "Klopp", "paul.klopp@example.com", OrganizationRole.REQUESTER, organization);

    Ticket ticket =
        testDataFactory.createTicket(
            requester, service, "Login failed", TicketStatus.OPEN, TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(ticket.getId()))
        .andExpect(jsonPath("$.content[0].subject").value("Login failed"))
        .andExpect(jsonPath("$.content[0].priority").value("MEDIUM"))
        .andExpect(jsonPath("$.content[0].status").value("OPEN"))
        .andExpect(jsonPath("$.content[0].requesterName").value("Paul Klopp"))
        .andExpect(jsonPath("$.content[0].serviceName").value("Test Service"))
        .andExpect(jsonPath("$.content[0].assignedAgentId").doesNotExist())
        .andExpect(jsonPath("$.content[0].assignedAgentName").doesNotExist())
        .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.number").value(0))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void shouldReturnEmptyPageWhenServiceTeamTicketsHasNoTickets() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    User requester =
        testDataFactory.createUser(
            "Paul", "Klopp", "paul.klopp@example.com", OrganizationRole.REQUESTER, organization);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)))
        .andExpect(jsonPath("$.totalElements").value(0))
        .andExpect(jsonPath("$.totalPages").value(0));
  }

  @Test
  void shouldFilterServiceTeamTicketsByStatus() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    Service service = testDataFactory.createService(organization, serviceTeam, "Test Service");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    User requester =
        testDataFactory.createUser(
            "Paul", "Klopp", "paul.klopp@example.com", OrganizationRole.REQUESTER, organization);

    Ticket closedTicket =
        testDataFactory.createTicket(
            requester, service, "Resolved incident", TicketStatus.CLOSED, TicketPriority.MEDIUM);
    testDataFactory.createTicket(
        requester, service, "Open incident", TicketStatus.OPEN, TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10")
                .param("status", "CLOSED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(closedTicket.getId()))
        .andExpect(jsonPath("$.content[0].status").value("CLOSED"))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  void shouldFilterServiceTeamTicketsByPriority() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    Service service = testDataFactory.createService(organization, serviceTeam, "Test Service");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    User requester =
        testDataFactory.createUser(
            "Paul", "Klopp", "paul.klopp@example.com", OrganizationRole.REQUESTER, organization);

    Ticket criticalTicket =
        testDataFactory.createTicket(
            requester,
            service,
            "Production unavailable",
            TicketStatus.OPEN,
            TicketPriority.CRITICAL);
    testDataFactory.createTicket(
        requester, service, "Printer degraded", TicketStatus.OPEN, TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10")
                .param("priority", "CRITICAL"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(criticalTicket.getId()))
        .andExpect(jsonPath("$.content[0].priority").value("CRITICAL"))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  void shouldSearchServiceTeamTicketsBySubjectIgnoringCase() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    Service service = testDataFactory.createService(organization, serviceTeam, "Test Service");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    User requester =
        testDataFactory.createUser(
            "Paul", "Klopp", "paul.klopp@example.com", OrganizationRole.REQUESTER, organization);

    Ticket matchingTicket =
        testDataFactory.createTicket(
            requester, service, "VPN connection failed", TicketStatus.OPEN, TicketPriority.MEDIUM);
    testDataFactory.createTicket(
        requester, service, "Printer is offline", TicketStatus.OPEN, TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("search", "CONNECTION")
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(matchingTicket.getId()))
        .andExpect(jsonPath("$.content[0].subject").value("VPN connection failed"))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  void shouldPaginateServiceTeamTicketsNewestFirst() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    Service service = testDataFactory.createService(organization, serviceTeam, "Test Service");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    User requester =
        testDataFactory.createUser(
            "Paul", "Klopp", "paul.klopp@example.com", OrganizationRole.REQUESTER, organization);

    Ticket oldestTicket =
        testDataFactory.createTicket(
            requester, service, "Oldest ticket", TicketStatus.OPEN, TicketPriority.MEDIUM);
    Ticket middleTicket =
        testDataFactory.createTicket(
            requester, service, "Middle ticket", TicketStatus.OPEN, TicketPriority.MEDIUM);
    Ticket newestTicket =
        testDataFactory.createTicket(
            requester, service, "Newest ticket", TicketStatus.OPEN, TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].id").value(newestTicket.getId()))
        .andExpect(jsonPath("$.content[1].id").value(middleTicket.getId()))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.number").value(0))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(false));

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "2")
                .param("limit", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(oldestTicket.getId()))
        .andExpect(jsonPath("$.number").value(1))
        .andExpect(jsonPath("$.first").value(false))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void shouldRejectUnknownServiceTeam() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", 9999999L)
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Service team not found"));
  }

  @Test
  void shouldHideServiceTeamFromAnotherOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization otherOrganization = testDataFactory.createOrganization();
    ServiceTeam otherServiceTeam =
        testDataFactory.createServiceTeam(otherOrganization, "Other Organization Team");

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", otherServiceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Service team not found"));
  }

  @Test
  void shouldRejectAgentWithoutServiceTeamMembership() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "Restricted Team");

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403))
        .andExpect(jsonPath("$.error").value("Forbidden"))
        .andExpect(jsonPath("$.message").value("You do not have access to this service team"));
  }

  @Test
  void shouldAllowOrganizationAdminWithoutServiceTeamMembership() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "Admin Accessible Team");

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)))
        .andExpect(jsonPath("$.totalElements").value(0));
  }

  @Test
  void shouldRejectRequester() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "Requester Team");

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectNonPositiveServiceTeamId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", 0)
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectNonNumericServiceTeamId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", "invalid")
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectPageBelowOne() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "0")
                .param("limit", "10"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectLimitBelowOne() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectLimitAbove100() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Test Service-Team");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", serviceTeam.getId())
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "101"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc
        .perform(
            get("/service-teams/{id}/tickets", 1L).param("page", "1").param("limit", "10"))
        .andExpect(status().isUnauthorized());
  }
}
