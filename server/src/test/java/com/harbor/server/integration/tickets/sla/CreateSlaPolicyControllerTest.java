package com.harbor.server.integration.tickets.sla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class CreateSlaPolicyControllerTest extends AbstractControllerIntegrationTest {

  @Autowired private SlaPolicyRepository slaPolicyRepository;

  private String validRequestBody(
      String name, String ticketPriority, int responseTimeMinutes, int resolutionTimeMinutes) {
    return """
        {
          "name": "%s",
          "ticketPriority": "%s",
          "responseTimeMinutes": %d,
          "resolutionTimeMinutes": %d
        }
        """
        .formatted(name, ticketPriority, responseTimeMinutes, resolutionTimeMinutes);
  }

  @Test
  void shouldCreateSlaPolicySuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("  Critical SLA  ", "CRITICAL", 15, 120)))
        .andExpect(status().isCreated());

    assertEquals(1, slaPolicyRepository.count());

    SlaPolicy policy = slaPolicyRepository.findAll().getFirst();

    assertEquals("Critical SLA", policy.getName());
    assertEquals(TicketPriority.CRITICAL, policy.getTicketPriority());
    assertEquals(15, policy.getResponseTimeMinutes());
    assertEquals(120, policy.getResolutionTimeMinutes());
    assertTrue(policy.isEnabled());
    assertEquals(auth.user().getOrganization().getId(), policy.getOrganization().getId());
  }

  @Test
  void shouldRejectDuplicatePriorityInSameOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization organization = auth.user().getOrganization();
    SlaPolicy existingPolicy =
        slaPolicyRepository.save(
            new SlaPolicy(organization, "Existing Critical SLA", TicketPriority.CRITICAL, 10, 60));

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("New Critical SLA", "CRITICAL", 15, 120)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("Conflict"))
        .andExpect(jsonPath("$.message").value("SLA policy for this priority already exists"));

    assertEquals(1, slaPolicyRepository.count());
    assertEquals(existingPolicy.getId(), slaPolicyRepository.findAll().getFirst().getId());
  }

  @Test
  void shouldAllowSamePriorityInDifferentOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization otherOrganization = testDataFactory.createOrganization();
    slaPolicyRepository.save(
        new SlaPolicy(otherOrganization, "Other Critical SLA", TicketPriority.CRITICAL, 10, 60));

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", "CRITICAL", 15, 120)))
        .andExpect(status().isCreated());

    assertEquals(2, slaPolicyRepository.count());
    assertTrue(
        slaPolicyRepository.existsByOrganizationIdAndTicketPriority(
            auth.user().getOrganization().getId(), TicketPriority.CRITICAL));
  }

  @Test
  void shouldRejectResolutionTimeEqualToResponseTime() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", "CRITICAL", 15, 15)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(
            jsonPath("$.message").value("Resolution time must be greater than response time"));

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectBlankName() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("   ", "CRITICAL", 15, 120)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Name is required"));

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectNameLongerThan100Characters() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("a".repeat(101), "CRITICAL", 15, 120)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Name must not exceed 100 characters"));

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectMissingTicketPriority() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "Critical SLA",
          "responseTimeMinutes": 15,
          "resolutionTimeMinutes": 120
        }
        """;

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("TicketPriority is required"));

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectMissingResponseTime() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "Critical SLA",
          "ticketPriority": "CRITICAL",
          "resolutionTimeMinutes": 120
        }
        """;

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("must not be null"));

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectNonPositiveResponseTime() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", "CRITICAL", 0, 120)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("must be greater than 0"));

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectMissingResolutionTime() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "Critical SLA",
          "ticketPriority": "CRITICAL",
          "responseTimeMinutes": 15
        }
        """;

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("must not be null"));

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectNonPositiveResolutionTime() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", "CRITICAL", 15, 0)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("must be greater than 0"));

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc
        .perform(
            post("/sla-policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", "CRITICAL", 15, 120)))
        .andExpect(status().isUnauthorized());

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectAgent() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAgent().sessionCookie();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", "CRITICAL", 15, 120)))
        .andExpect(status().isForbidden());

    assertEquals(0, slaPolicyRepository.count());
  }

  @Test
  void shouldRejectRequester() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", "CRITICAL", 15, 120)))
        .andExpect(status().isForbidden());

    assertEquals(0, slaPolicyRepository.count());
  }
}
