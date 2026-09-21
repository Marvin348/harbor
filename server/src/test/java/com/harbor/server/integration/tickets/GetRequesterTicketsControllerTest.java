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

public class GetRequesterTicketsControllerTest extends AbstractControllerIntegrationTest {

  private Service createService(Organization organization) {
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    return testDataFactory.createService(organization, serviceTeam, "IT Service");
  }

  @Test
  void shouldGetRequesterTicketsSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Organization organization = auth.user().getOrganization();
    Service service = createService(organization);
    Ticket ownTicket =
        testDataFactory.createTicket(
            auth.user(), service, "Login failed", TicketStatus.OPEN, TicketPriority.MEDIUM);

    User otherRequester =
        testDataFactory.createUser(
            "Other",
            "Requester",
            "other.requester@example.com",
            OrganizationRole.REQUESTER,
            organization);
    testDataFactory.createTicket(
        otherRequester,
        service,
        "Other requester ticket",
        TicketStatus.OPEN,
        TicketPriority.MEDIUM);

    Organization otherOrganization = testDataFactory.createOrganization();
    User foreignRequester =
        testDataFactory.createUser(
            "Foreign",
            "Requester",
            "foreign.requester@example.com",
            OrganizationRole.REQUESTER,
            otherOrganization);
    Service foreignService = createService(otherOrganization);
    testDataFactory.createTicket(
        foreignRequester,
        foreignService,
        "Foreign ticket",
        TicketStatus.OPEN,
        TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/tickets").cookie(auth.sessionCookie()).param("page", "1").param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(ownTicket.getId()))
        .andExpect(jsonPath("$.content[0].subject").value("Login failed"))
        .andExpect(jsonPath("$.content[0].serviceName").value("IT Service"))
        .andExpect(jsonPath("$.content[0].status").value("OPEN"))
        .andExpect(jsonPath("$.content[0].priority").value("MEDIUM"))
        .andExpect(jsonPath("$.content[0].assignedAgentName").doesNotExist())
        .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
        .andExpect(jsonPath("$.content[0].updatedAt").isNotEmpty())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.number").value(0))
        .andExpect(jsonPath("$.numberOfElements").value(1))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true))
        .andExpect(jsonPath("$.empty").value(false));
  }

  @Test
  void shouldReturnEmptyPageWhenRequesterHasNoTickets() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();

    mockMvc
        .perform(
            get("/tickets").cookie(auth.sessionCookie()).param("page", "1").param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)))
        .andExpect(jsonPath("$.totalElements").value(0))
        .andExpect(jsonPath("$.totalPages").value(0))
        .andExpect(jsonPath("$.numberOfElements").value(0))
        .andExpect(jsonPath("$.empty").value(true));
  }

  @Test
  void shouldFilterTicketsBySubjectIgnoringCase() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Service service = createService(auth.user().getOrganization());
    Ticket matchingTicket =
        testDataFactory.createTicket(
            auth.user(),
            service,
            "VPN connection failed",
            TicketStatus.OPEN,
            TicketPriority.MEDIUM);
    testDataFactory.createTicket(
        auth.user(),
        service,
        "Printer is offline",
        TicketStatus.OPEN,
        TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/tickets")
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
  void shouldFilterTicketsByStatus() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Service service = createService(auth.user().getOrganization());
    Ticket closedTicket =
        testDataFactory.createTicket(
            auth.user(),
            service,
            "Resolved incident",
            TicketStatus.CLOSED,
            TicketPriority.MEDIUM);
    testDataFactory.createTicket(
        auth.user(), service, "Open incident", TicketStatus.OPEN, TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/tickets")
                .cookie(auth.sessionCookie())
                .param("status", "CLOSED")
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(closedTicket.getId()))
        .andExpect(jsonPath("$.content[0].status").value("CLOSED"))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  void shouldPaginateTicketsNewestFirst() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Service service = createService(auth.user().getOrganization());
    Ticket oldestTicket =
        testDataFactory.createTicket(
            auth.user(),
            service,
            "Oldest ticket",
            TicketStatus.OPEN,
            TicketPriority.MEDIUM);
    Ticket middleTicket =
        testDataFactory.createTicket(
            auth.user(),
            service,
            "Middle ticket",
            TicketStatus.OPEN,
            TicketPriority.MEDIUM);
    Ticket newestTicket =
        testDataFactory.createTicket(
            auth.user(),
            service,
            "Newest ticket",
            TicketStatus.OPEN,
            TicketPriority.MEDIUM);

    mockMvc
        .perform(
            get("/tickets").cookie(auth.sessionCookie()).param("page", "1").param("limit", "2"))
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
            get("/tickets").cookie(auth.sessionCookie()).param("page", "2").param("limit", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(oldestTicket.getId()))
        .andExpect(jsonPath("$.number").value(1))
        .andExpect(jsonPath("$.numberOfElements").value(1))
        .andExpect(jsonPath("$.first").value(false))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void shouldRejectPageBelowOne() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();

    mockMvc
        .perform(
            get("/tickets").cookie(auth.sessionCookie()).param("page", "0").param("limit", "10"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must be greater than or equal to 1"));
  }

  @Test
  void shouldRejectLimitBelowOne() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();

    mockMvc
        .perform(
            get("/tickets").cookie(auth.sessionCookie()).param("page", "1").param("limit", "0"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must be greater than or equal to 1"));
  }

  @Test
  void shouldRejectLimitAbove100() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();

    mockMvc
        .perform(
            get("/tickets").cookie(auth.sessionCookie()).param("page", "1").param("limit", "101"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must be less than or equal to 100"));
  }
}
