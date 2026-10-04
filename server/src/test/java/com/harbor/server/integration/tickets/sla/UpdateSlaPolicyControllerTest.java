package com.harbor.server.integration.tickets.sla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

public class UpdateSlaPolicyControllerTest extends AbstractControllerIntegrationTest {

  @Autowired private SlaPolicyRepository slaPolicyRepository;

  private String validRequestBody(
      String name, int responseTimeMinutes, int resolutionTimeMinutes) {
    return """
        {
          "name": "%s",
          "responseTimeMinutes": %d,
          "resolutionTimeMinutes": %d
        }
        """
        .formatted(name, responseTimeMinutes, resolutionTimeMinutes);
  }

  @Test
  void shouldUpdateSlaPolicySuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(),
            "Critical SLA",
            TicketPriority.CRITICAL,
            30,
            240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("  Updated Critical SLA  ", 15, 120)))
        .andExpect(status().isOk());

    assertEquals(1, slaPolicyRepository.count());

    SlaPolicy updatedPolicy = slaPolicyRepository.findById(policy.getId()).orElseThrow();

    assertEquals("Updated Critical SLA", updatedPolicy.getName());
    assertEquals(TicketPriority.CRITICAL, updatedPolicy.getTicketPriority());
    assertEquals(15, updatedPolicy.getResponseTimeMinutes());
    assertEquals(120, updatedPolicy.getResolutionTimeMinutes());
    assertTrue(updatedPolicy.isEnabled());
    assertEquals(
        auth.user().getOrganization().getId(), updatedPolicy.getOrganization().getId());
  }

  @Test
  void shouldRejectSlaPolicyFromAnotherOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization otherOrganization = testDataFactory.createOrganization();
    SlaPolicy otherPolicy =
        testDataFactory.createSlaPolicy(
            otherOrganization, "Foreign Critical SLA", TicketPriority.CRITICAL, 30, 240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", otherPolicy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Changed SLA", 15, 120)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("SLA policy not found"));

    SlaPolicy unchangedPolicy = slaPolicyRepository.findById(otherPolicy.getId()).orElseThrow();
    assertEquals("Foreign Critical SLA", unchangedPolicy.getName());
    assertEquals(30, unchangedPolicy.getResponseTimeMinutes());
    assertEquals(240, unchangedPolicy.getResolutionTimeMinutes());
  }

  @Test
  void shouldRejectUnknownSlaPolicy() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(
            patch("/sla-policies/{id}", 999999L)
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", 15, 120)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("SLA policy not found"));
  }

  @Test
  void shouldRejectResolutionTimeEqualToResponseTime() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", 15, 15)))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.message").value("Resolution time must be greater than response time"));

    SlaPolicy unchangedPolicy = slaPolicyRepository.findById(policy.getId()).orElseThrow();
    assertEquals(30, unchangedPolicy.getResponseTimeMinutes());
    assertEquals(240, unchangedPolicy.getResolutionTimeMinutes());
  }

  @Test
  void shouldRejectBlankName() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("   ", 15, 120)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Name is required"));
  }

  @Test
  void shouldRejectNameLongerThan100Characters() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("a".repeat(101), 15, 120)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Name must not exceed 100 characters"));
  }

  @Test
  void shouldUpdateOnlyResolutionTime() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    String requestBody =
        """
        {
          "resolutionTimeMinutes": 300
        }
        """;

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk());

    SlaPolicy updatedPolicy = slaPolicyRepository.findById(policy.getId()).orElseThrow();
    assertEquals("Critical SLA", updatedPolicy.getName());
    assertEquals(30, updatedPolicy.getResponseTimeMinutes());
    assertEquals(300, updatedPolicy.getResolutionTimeMinutes());
  }

  @Test
  void shouldRejectNonPositiveResponseTime() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", 0, 120)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("must be greater than 0"));
  }

  @Test
  void shouldUpdateOnlyResponseTime() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    String requestBody =
        """
        {
          "responseTimeMinutes": 20
        }
        """;

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk());

    SlaPolicy updatedPolicy = slaPolicyRepository.findById(policy.getId()).orElseThrow();
    assertEquals("Critical SLA", updatedPolicy.getName());
    assertEquals(20, updatedPolicy.getResponseTimeMinutes());
    assertEquals(240, updatedPolicy.getResolutionTimeMinutes());
  }

  @Test
  void shouldUpdateOnlyName() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"  Updated SLA  \"}"))
        .andExpect(status().isOk());

    SlaPolicy updatedPolicy = slaPolicyRepository.findById(policy.getId()).orElseThrow();
    assertEquals("Updated SLA", updatedPolicy.getName());
    assertEquals(30, updatedPolicy.getResponseTimeMinutes());
    assertEquals(240, updatedPolicy.getResolutionTimeMinutes());
  }

  @Test
  void shouldRejectEmptyUpdate() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("At least one field must be provided"));
  }

  @Test
  void shouldRejectNonPositiveResolutionTime() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy policy =
        testDataFactory.createSlaPolicy(
            auth.user().getOrganization(), "Critical SLA", TicketPriority.CRITICAL, 30, 240);

    mockMvc
        .perform(
            patch("/sla-policies/{id}", policy.getId())
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", 15, 0)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("must be greater than 0"));
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc
        .perform(
            patch("/sla-policies/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", 15, 120)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldRejectAgent() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAgent().sessionCookie();

    mockMvc
        .perform(
            patch("/sla-policies/{id}", 1L)
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", 15, 120)))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectRequester() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    mockMvc
        .perform(
            patch("/sla-policies/{id}", 1L)
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", 15, 120)))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldNotExposeCreateRoute() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(
            post("/sla-policies")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody("Critical SLA", 15, 120)))
        .andExpect(status().isMethodNotAllowed());
  }
}
