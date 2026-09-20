package com.harbor.server.integration.serviceTeam;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import org.junit.jupiter.api.Test;

public class GetServiceTeamDetailsControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldGetServiceTeamDetailsSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "IT Support");

    testDataFactory.createService(organization, serviceTeam, "Incident Management");
    testDataFactory.createService(organization, serviceTeam, "Access Management");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    User secondAgent =
        testDataFactory.createUser(
            "Alex", "Schmidt", "alex.schmidt@example.com", OrganizationRole.AGENT, organization);
    testDataFactory.createServiceTeamMember(secondAgent, serviceTeam, organization);

    mockMvc
        .perform(get("/service-teams/{id}", serviceTeam.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(serviceTeam.getId()))
        .andExpect(jsonPath("$.name").value("IT Support"))
        .andExpect(jsonPath("$.description").value("Handles internal support requests"))
        .andExpect(jsonPath("$.agentCount").value(2))
        .andExpect(jsonPath("$.serviceCount").value(2));
  }

  @Test
  void shouldReturnNotFoundForUnknownServiceTeam() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/service-teams/{id}", 999999L).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Service Team Not Found"));
  }

  @Test
  void shouldReturnNotFoundForServiceTeamFromAnotherOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization otherOrganization = testDataFactory.createOrganization();
    ServiceTeam otherServiceTeam =
        testDataFactory.createServiceTeam(otherOrganization, "External Support");

    mockMvc
        .perform(get("/service-teams/{id}", otherServiceTeam.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Service Team Not Found"));
  }

  @Test
  void shouldReturnZeroCountsWhenServiceTeamHasNoAgentsOrServices() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "Empty Team");

    mockMvc
        .perform(get("/service-teams/{id}", serviceTeam.getId()).cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(serviceTeam.getId()))
        .andExpect(jsonPath("$.name").value("Empty Team"))
        .andExpect(jsonPath("$.description").value("Handles internal support requests"))
        .andExpect(jsonPath("$.agentCount").value(0))
        .andExpect(jsonPath("$.serviceCount").value(0));
  }

  @Test
  void shouldRejectNonPositiveServiceTeamId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/service-teams/{id}", 0).cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectNonNumericServiceTeamId() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/service-teams/{id}", "invalid").cookie(auth.sessionCookie()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc.perform(get("/service-teams/{id}", 1L)).andExpect(status().isUnauthorized());
  }
}
