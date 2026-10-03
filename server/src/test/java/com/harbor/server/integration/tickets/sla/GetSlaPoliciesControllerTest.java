package com.harbor.server.integration.tickets.sla;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

public class GetSlaPoliciesControllerTest extends AbstractControllerIntegrationTest {

  @Autowired private SlaPolicyRepository slaPolicyRepository;

  @Test
  void shouldGetSlaPoliciesSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization organization = auth.user().getOrganization();
    SlaPolicy lowPolicy =
        createSlaPolicy(organization, "Standard SLA", TicketPriority.LOW, 240, 1440);
    SlaPolicy criticalPolicy =
        createSlaPolicy(organization, "Critical SLA", TicketPriority.CRITICAL, 15, 120);

    mockMvc
        .perform(get("/sla-policies").cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(lowPolicy.getId()))
        .andExpect(jsonPath("$[0].name").value("Standard SLA"))
        .andExpect(jsonPath("$[0].ticketPriority").value("LOW"))
        .andExpect(jsonPath("$[0].responseTimeMinutes").value(240))
        .andExpect(jsonPath("$[0].resolutionTimeMinutes").value(1440))
        .andExpect(jsonPath("$[0].enabled").value(true))
        .andExpect(jsonPath("$[1].id").value(criticalPolicy.getId()))
        .andExpect(jsonPath("$[1].name").value("Critical SLA"))
        .andExpect(jsonPath("$[1].ticketPriority").value("CRITICAL"))
        .andExpect(jsonPath("$[1].responseTimeMinutes").value(15))
        .andExpect(jsonPath("$[1].resolutionTimeMinutes").value(120))
        .andExpect(jsonPath("$[1].enabled").value(true));
  }

  @Test
  void shouldReturnEmptyListWhenOrganizationHasNoSlaPolicies() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(get("/sla-policies").cookie(sessionCookie))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void shouldOnlyReturnSlaPoliciesFromAuthenticatedOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    SlaPolicy ownPolicy =
        createSlaPolicy(
            auth.user().getOrganization(), "Own Critical SLA", TicketPriority.CRITICAL, 15, 120);

    Organization otherOrganization = testDataFactory.createOrganization();
    createSlaPolicy(
        otherOrganization, "Foreign Critical SLA", TicketPriority.CRITICAL, 30, 180);

    mockMvc
        .perform(get("/sla-policies").cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(ownPolicy.getId()))
        .andExpect(jsonPath("$[0].name").value("Own Critical SLA"));
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc.perform(get("/sla-policies")).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldRejectAgent() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAgent().sessionCookie();

    mockMvc
        .perform(get("/sla-policies").cookie(sessionCookie))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectRequester() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginRequester().sessionCookie();

    mockMvc
        .perform(get("/sla-policies").cookie(sessionCookie))
        .andExpect(status().isForbidden());
  }

  private SlaPolicy createSlaPolicy(
      Organization organization,
      String name,
      TicketPriority ticketPriority,
      int responseTimeMinutes,
      int resolutionTimeMinutes) {
    return slaPolicyRepository.save(
        new SlaPolicy(
            organization,
            name,
            ticketPriority,
            responseTimeMinutes,
            resolutionTimeMinutes));
  }
}
