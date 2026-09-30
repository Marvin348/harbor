package com.harbor.server.integration.tickets.communication.messages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class CreateTicketMessageControllerTest extends AbstractControllerIntegrationTest {

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

  private ResultActions performCreateMessage(Cookie sessionCookie, Object ticketId, String body)
      throws Exception {
    return mockMvc.perform(
        post("/tickets/{id}/messages", ticketId)
            .cookie(sessionCookie)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body));
  }

  private String validRequestBody(TicketMessageType type, String body) {
    return """
        {
          "body": "%s",
          "type": "%s"
        }
        """
        .formatted(body, type);
  }

  @Test
  void shouldCreateAgentReplySuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createTicket(auth);

    performCreateMessage(
            auth.sessionCookie(),
            ticket.getId(),
            validRequestBody(TicketMessageType.REPLY, "I am checking the VPN logs now."))
        .andExpect(status().isCreated());

    assertThat(ticketMessageRepository.count()).isEqualTo(1);

    TicketMessage message = ticketMessageRepository.findAll().getFirst();
    assertThat(message.getTicket().getId()).isEqualTo(ticket.getId());
    assertThat(message.getOrganization().getId()).isEqualTo(auth.user().getOrganization().getId());
    assertThat(message.getAuthor().getId()).isEqualTo(auth.user().getId());
    assertThat(message.getType()).isEqualTo(TicketMessageType.REPLY);
    assertThat(message.getBody()).isEqualTo("I am checking the VPN logs now.");
    assertThat(message.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldCreateAgentInternalNoteSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createTicket(auth);

    performCreateMessage(
            auth.sessionCookie(),
            ticket.getId(),
            validRequestBody(
                TicketMessageType.INTERNAL_NOTE, "The gateway logs contain repeated timeouts."))
        .andExpect(status().isCreated());

    TicketMessage message = ticketMessageRepository.findAll().getFirst();
    assertThat(message.getAuthor().getId()).isEqualTo(auth.user().getId());
    assertThat(message.getType()).isEqualTo(TicketMessageType.INTERNAL_NOTE);
    assertThat(message.getBody()).isEqualTo("The gateway logs contain repeated timeouts.");
  }

  @Test
  void shouldCreateRequesterReplyForOwnTicketSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Ticket ticket = createTicket(auth);

    performCreateMessage(
            auth.sessionCookie(),
            ticket.getId(),
            validRequestBody(TicketMessageType.REPLY, "The connection failed again at 10:30."))
        .andExpect(status().isCreated());

    TicketMessage message = ticketMessageRepository.findAll().getFirst();
    assertThat(message.getAuthor().getId()).isEqualTo(auth.user().getId());
    assertThat(message.getType()).isEqualTo(TicketMessageType.REPLY);
  }

  @Test
  void shouldCreateAdminInternalNoteSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Ticket ticket = createTicket(auth);

    performCreateMessage(
            auth.sessionCookie(),
            ticket.getId(),
            validRequestBody(
                TicketMessageType.INTERNAL_NOTE, "Keep the network team informed."))
        .andExpect(status().isCreated());

    TicketMessage message = ticketMessageRepository.findAll().getFirst();
    assertThat(message.getAuthor().getId()).isEqualTo(auth.user().getId());
    assertThat(message.getType()).isEqualTo(TicketMessageType.INTERNAL_NOTE);
  }

  @Test
  void shouldRejectInternalNoteFromRequester() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Ticket ticket = createTicket(auth);

    performCreateMessage(
            auth.sessionCookie(),
            ticket.getId(),
            validRequestBody(TicketMessageType.INTERNAL_NOTE, "This must remain internal."))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403))
        .andExpect(jsonPath("$.error").value("Forbidden"))
        .andExpect(jsonPath("$.message").value("Requester cannot create internal notes"));

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldHideAnotherRequestersTicket() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Ticket ownTicket = createTicket(auth);
    User otherRequester =
        testDataFactory.createUser(
            "Other",
            "Requester",
            "other.message.requester@example.com",
            OrganizationRole.REQUESTER,
            auth.user().getOrganization());
    Ticket otherTicket =
        testDataFactory.createTicket(
            otherRequester,
            ownTicket.getService(),
            "Other requester ticket",
            TicketStatus.OPEN,
            TicketPriority.MEDIUM);

    performCreateMessage(
            auth.sessionCookie(),
            otherTicket.getId(),
            validRequestBody(TicketMessageType.REPLY, "I must not be able to add this."))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Ticket not found"));

    assertThat(ticketMessageRepository.count()).isZero();
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
            "foreign.message.requester@example.com",
            OrganizationRole.REQUESTER,
            otherOrganization);
    Ticket otherTicket =
        testDataFactory.createTicket(
            otherRequester,
            otherService,
            "Foreign ticket",
            TicketStatus.OPEN,
            TicketPriority.HIGH);

    performCreateMessage(
            auth.sessionCookie(),
            otherTicket.getId(),
            validRequestBody(TicketMessageType.REPLY, "Cross-tenant message."))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Ticket not found"));

    assertThat(ticketMessageRepository.count()).isZero();
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
            "restricted.message.requester@example.com",
            OrganizationRole.REQUESTER,
            organization);
    Ticket ticket =
        testDataFactory.createTicket(
            requester, service, "Restricted ticket", TicketStatus.OPEN, TicketPriority.HIGH);

    performCreateMessage(
            auth.sessionCookie(),
            ticket.getId(),
            validRequestBody(TicketMessageType.REPLY, "Unauthorized team message."))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Ticket not found"));

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldRejectBlankMessageBody() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createTicket(auth);

    String requestBody =
        """
        {
          "body": "   ",
          "type": "REPLY"
        }
        """;

    performCreateMessage(auth.sessionCookie(), ticket.getId(), requestBody)
        .andExpect(status().isBadRequest());

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldRejectMissingMessageBody() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createTicket(auth);

    String requestBody =
        """
        {
          "type": "REPLY"
        }
        """;

    performCreateMessage(auth.sessionCookie(), ticket.getId(), requestBody)
        .andExpect(status().isBadRequest());

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldRejectMissingMessageType() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createTicket(auth);

    String requestBody =
        """
        {
          "body": "Test message"
        }
        """;

    performCreateMessage(auth.sessionCookie(), ticket.getId(), requestBody)
        .andExpect(status().isBadRequest());

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldRejectInvalidMessageType() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createTicket(auth);

    String requestBody =
        """
        {
          "body": "Test message",
          "type": "WRONG_TYPE"
        }
        """;

    performCreateMessage(auth.sessionCookie(), ticket.getId(), requestBody)
        .andExpect(status().isBadRequest());

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldRejectMalformedRequestBody() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Ticket ticket = createTicket(auth);

    performCreateMessage(auth.sessionCookie(), ticket.getId(), "{not-valid-json")
        .andExpect(status().isBadRequest());

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldRejectNonPositiveTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    performCreateMessage(
            auth.sessionCookie(),
            0,
            validRequestBody(TicketMessageType.REPLY, "Valid message body"))
        .andExpect(status().isBadRequest());

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldRejectNonNumericTicketId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    performCreateMessage(
            auth.sessionCookie(),
            "invalid",
            validRequestBody(TicketMessageType.REPLY, "Valid message body"))
        .andExpect(status().isBadRequest());

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldReturnNotFoundForUnknownTicket() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    performCreateMessage(
            auth.sessionCookie(),
            99_999_999L,
            validRequestBody(TicketMessageType.REPLY, "Valid message body"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Ticket not found"));

    assertThat(ticketMessageRepository.count()).isZero();
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc
        .perform(
            post("/tickets/{id}/messages", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody(TicketMessageType.REPLY, "Valid message body")))
        .andExpect(status().isUnauthorized());

    assertThat(ticketMessageRepository.count()).isZero();
  }
}
