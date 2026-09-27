package com.harbor.server.integration.tickets.agent;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketStatus;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ClaimTicketControllerTest extends AbstractControllerIntegrationTest {

  @Autowired private TicketRepository ticketRepository;

  private Ticket createClaimableTicket(AuthenticatedTestUser auth) {
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
    return testDataFactory.createTicket(
        requester,
        service,
        "VPN connection fails regularly",
        TicketStatus.OPEN,
        TicketPriority.HIGH);
  }

  @Test
  void shouldClaimTheTicketSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createClaimableTicket(auth);

    mockMvc
        .perform(patch("/tickets/{id}/claim", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNoContent());

    Ticket claimedTicket = ticketRepository.findById(ticket.getId()).orElseThrow();

    assertThat(claimedTicket.getAssignedAgent()).isNotNull();
    assertThat(claimedTicket.getAssignedAgent().getId()).isEqualTo(auth.user().getId());
  }

  @Test
  void shouldReturnConflictWhenTicketIsAlreadyClaimed() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createClaimableTicket(auth);

    mockMvc
        .perform(patch("/tickets/{id}/claim", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(patch("/tickets/{id}/claim", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("Conflict"))
        .andExpect(jsonPath("$.message").value("Ticket could not be claimed"));

    Ticket claimedTicket = ticketRepository.findById(ticket.getId()).orElseThrow();
    assertThat(claimedTicket.getAssignedAgent().getId()).isEqualTo(auth.user().getId());
  }

  @Test
  void shouldReturnNotFoundForUnknownTicket() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(patch("/tickets/{id}/claim", 9_999_999L).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Ticket not found"));
  }

  @Test
  void shouldHideTicketFromAnotherOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization otherOrganization = testDataFactory.createOrganization();
    ServiceTeam otherServiceTeam =
        testDataFactory.createServiceTeam(otherOrganization, "Other Organization Team");
    Service otherService =
        testDataFactory.createService(otherOrganization, otherServiceTeam, "Other Service");
    User otherRequester =
        testDataFactory.createUser(
            "Foreign",
            "Requester",
            "foreign.claim.requester@example.com",
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
        .perform(patch("/tickets/{id}/claim", otherTicket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Ticket not found"));

    assertThat(ticketRepository.findById(otherTicket.getId()).orElseThrow().getAssignedAgent())
        .isNull();
  }

  @Test
  void shouldHideTicketWhenAgentIsNotServiceTeamMember() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "Restricted Team");
    Service service =
        testDataFactory.createService(organization, serviceTeam, "Restricted Service");
    User requester =
        testDataFactory.createUser(
            "Restricted",
            "Requester",
            "restricted.claim.requester@example.com",
            OrganizationRole.REQUESTER,
            organization);
    Ticket ticket =
        testDataFactory.createTicket(
            requester, service, "Restricted ticket", TicketStatus.OPEN, TicketPriority.MEDIUM);

    mockMvc
        .perform(patch("/tickets/{id}/claim", ticket.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Ticket not found"));

    assertThat(ticketRepository.findById(ticket.getId()).orElseThrow().getAssignedAgent()).isNull();
  }

  @Test
  void shouldRejectOrganizationAdmin() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();

    mockMvc
        .perform(patch("/tickets/{id}/claim", 1L).cookie(auth.sessionCookie()))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectRequester() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();

    mockMvc
        .perform(patch("/tickets/{id}/claim", 1L).cookie(auth.sessionCookie()))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectNonPositiveTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(patch("/tickets/{id}/claim", 0).cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectNonNumericTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(patch("/tickets/{id}/claim", "invalid").cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc.perform(patch("/tickets/{id}/claim", 1L)).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldAllowOnlyOneAgentToClaimTicketWhenRequestsRace() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createClaimableTicket(auth);

    CountDownLatch requestsReady = new CountDownLatch(2);
    CountDownLatch startRequests = new CountDownLatch(1);
    ExecutorService executor = Executors.newFixedThreadPool(2);

    try {
      Future<Integer> firstResponse =
          executor.submit(
              () -> {
                requestsReady.countDown();
                assertThat(startRequests.await(5, SECONDS)).isTrue();

                return mockMvc
                    .perform(
                        patch("/tickets/{id}/claim", ticket.getId()).cookie(auth.sessionCookie()))
                    .andReturn()
                    .getResponse()
                    .getStatus();
              });
      Future<Integer> secondResponse =
          executor.submit(
              () -> {
                requestsReady.countDown();
                assertThat(startRequests.await(5, SECONDS)).isTrue();

                return mockMvc
                    .perform(
                        patch("/tickets/{id}/claim", ticket.getId()).cookie(auth.sessionCookie()))
                    .andReturn()
                    .getResponse()
                    .getStatus();
              });

      assertThat(requestsReady.await(5, SECONDS)).isTrue();
      startRequests.countDown();

      List<Integer> responseStatuses =
          List.of(firstResponse.get(5, SECONDS), secondResponse.get(5, SECONDS));
      assertThat(responseStatuses).containsExactlyInAnyOrder(204, 409);

      Ticket claimedTicket = ticketRepository.findById(ticket.getId()).orElseThrow();

      assertThat(claimedTicket.getAssignedAgent()).isNotNull();
      assertThat(claimedTicket.getAssignedAgent().getId()).isEqualTo(auth.user().getId());
    } finally {
      startRequests.countDown();
      executor.shutdownNow();
    }
  }
}
