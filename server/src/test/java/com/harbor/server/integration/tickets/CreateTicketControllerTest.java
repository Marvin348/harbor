package com.harbor.server.integration.tickets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.tickets.model.Ticket;
import com.harbor.server.features.tickets.model.TicketBusinessCriticality;
import com.harbor.server.features.tickets.model.TicketImpact;
import com.harbor.server.features.tickets.model.TicketStatus;
import com.harbor.server.features.tickets.model.TicketUrgency;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class CreateTicketControllerTest extends AbstractControllerIntegrationTest {
  @Autowired private TicketRepository ticketRepository;

  private ResultActions performCreateTicket(Cookie sessionCookie, String requestBody)
      throws Exception {
    return mockMvc.perform(
        post("/tickets")
            .cookie(sessionCookie)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
  }

  private String validRequestBody(Long serviceId) {
    return """
        {
          "serviceId": %d,
          "subject": "Login failed",
          "description": "Unable to sign in since this morning",
          "assessment": {
            "impact": "SINGLE_USER",
            "urgency": "WORK_BLOCKED",
            "businessCriticality": "HIGH"
          }
        }
        """
        .formatted(serviceId);
  }

  @Test
  void shouldCreateTicketSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    Service service =
        testDataFactory.createService(organization, serviceTeam, "Identity Management");

    String requestBody = validRequestBody(service.getId());

    performCreateTicket(auth.sessionCookie(), requestBody)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.subject").value("Login failed"))
        .andExpect(jsonPath("$.status").value("OPEN"))
        .andExpect(jsonPath("$.priority").doesNotExist())
        .andExpect(jsonPath("$.createdAt").isNotEmpty());

    assertEquals(1, ticketRepository.count());

    Ticket ticket = ticketRepository.findAll().getFirst();

    assertEquals("Login failed", ticket.getSubject());
    assertEquals("Unable to sign in since this morning", ticket.getDescription());
    assertEquals(TicketStatus.OPEN, ticket.getStatus());
    assertEquals(TicketImpact.SINGLE_USER, ticket.getImpact());
    assertEquals(TicketUrgency.WORK_BLOCKED, ticket.getUrgency());
    assertEquals(TicketBusinessCriticality.HIGH, ticket.getBusinessCriticality());
    assertEquals(organization.getId(), ticket.getOrganization().getId());
    assertEquals(serviceTeam.getId(), ticket.getServiceTeam().getId());
    assertEquals(service.getId(), ticket.getService().getId());
    assertEquals(auth.user().getId(), ticket.getRequester().getId());
    assertNull(ticket.getAssignedAgent());
    assertNull(ticket.getPriority());
    assertNotNull(ticket.getCreatedAt());
    assertNotNull(ticket.getUpdatedAt());
  }

  @Test
  void shouldRejectServiceFromAnotherOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginRequester();
    Organization otherOrganization = testDataFactory.createOrganization();
    ServiceTeam otherServiceTeam =
        testDataFactory.createServiceTeam(otherOrganization, "External Support");
    Service otherService =
        testDataFactory.createService(otherOrganization, otherServiceTeam, "Foreign Service");

    String requestBody = validRequestBody(otherService.getId());

    performCreateTicket(auth.sessionCookie(), requestBody)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Service not found"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectUnknownService() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    performCreateTicket(sessionCookie, validRequestBody(999999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Service not found"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectBlankSubject() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "serviceId": 1,
          "subject": "   ",
          "description": "Some description",
          "assessment": {
            "impact": "SINGLE_USER",
            "urgency": "WORK_BLOCKED",
            "businessCriticality": "HIGH"
          }
        }
        """;

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Subject is required"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectSubjectLongerThan150Characters() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "serviceId": 1,
          "subject": "%s",
          "description": "Some description",
          "assessment": {
            "impact": "SINGLE_USER",
            "urgency": "WORK_BLOCKED",
            "businessCriticality": "HIGH"
          }
        }
        """
            .formatted("a".repeat(151));

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Subject must not exceed 150 characters"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectBlankDescription() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "serviceId": 1,
          "subject": "Login failed",
          "description": "   ",
          "assessment": {
            "impact": "SINGLE_USER",
            "urgency": "WORK_BLOCKED",
            "businessCriticality": "HIGH"
          }
        }
        """;

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Description is required"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectDescriptionLongerThan1500Characters() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "serviceId": 1,
          "subject": "Login failed",
          "description": "%s",
          "assessment": {
            "impact": "SINGLE_USER",
            "urgency": "WORK_BLOCKED",
            "businessCriticality": "HIGH"
          }
        }
        """
            .formatted("a".repeat(1501));

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Description must not exceed 1500 characters"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectMissingServiceId() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "subject": "Login failed",
          "description": "Some description",
          "assessment": {
            "impact": "SINGLE_USER",
            "urgency": "WORK_BLOCKED",
            "businessCriticality": "HIGH"
          }
        }
        """;

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Service is required"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectMissingAssessment() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "serviceId": 1,
          "subject": "Login failed",
          "description": "Some description"
        }
        """;

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Assessment is required"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectMissingImpact() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "serviceId": 1,
          "subject": "Login failed",
          "description": "Some description",
          "assessment": {
            "urgency": "WORK_BLOCKED",
            "businessCriticality": "HIGH"
          }
        }
        """;

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Impact is required"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectMissingUrgency() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "serviceId": 1,
          "subject": "Login failed",
          "description": "Some description",
          "assessment": {
            "impact": "SINGLE_USER",
            "businessCriticality": "HIGH"
          }
        }
        """;

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Urgency is required"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectMissingBusinessCriticality() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    String requestBody =
        """
        {
          "serviceId": 1,
          "subject": "Login failed",
          "description": "Some description",
          "assessment": {
            "impact": "SINGLE_USER",
            "urgency": "WORK_BLOCKED"
          }
        }
        """;

    performCreateTicket(sessionCookie, requestBody)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Business criticality is required"));

    assertEquals(0, ticketRepository.count());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc
        .perform(
            post("/tickets").contentType(MediaType.APPLICATION_JSON).content(validRequestBody(1L)))
        .andExpect(status().isUnauthorized());

    assertEquals(0, ticketRepository.count());
  }
}
