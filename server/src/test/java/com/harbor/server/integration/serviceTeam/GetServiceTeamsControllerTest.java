package com.harbor.server.integration.serviceTeam;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import org.junit.jupiter.api.Test;

public class GetServiceTeamsControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldGetServiceTeamsSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam serviceTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    testDataFactory.createService(organization, serviceTeam, "Incident Management");
    testDataFactory.createServiceTeamMember(auth.user(), serviceTeam, organization);

    Organization otherOrganization = testDataFactory.createOrganization();
    testDataFactory.createServiceTeam(otherOrganization, "Foreign Team");

    mockMvc
        .perform(
            get("/service-teams")
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(serviceTeam.getId()))
        .andExpect(jsonPath("$.content[0].name").value("IT Support"))
        .andExpect(
            jsonPath("$.content[0].description").value("Handles internal support requests"))
        .andExpect(jsonPath("$.content[0].agentCount").value(1))
        .andExpect(jsonPath("$.content[0].serviceCount").value(1))
        .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.number").value(0))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void shouldReturnEmptyPageWhenOrganizationHasNoServiceTeams() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(
            get("/service-teams")
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)))
        .andExpect(jsonPath("$.totalElements").value(0))
        .andExpect(jsonPath("$.totalPages").value(0))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void shouldFilterServiceTeamsByNameIgnoringCase() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam matchingTeam =
        testDataFactory.createServiceTeam(organization, "IT Support");
    testDataFactory.createServiceTeam(organization, "Platform Engineering");

    Organization otherOrganization = testDataFactory.createOrganization();
    testDataFactory.createServiceTeam(otherOrganization, "External Support");

    mockMvc
        .perform(
            get("/service-teams")
                .cookie(auth.sessionCookie())
                .param("search", "SUPPORT")
                .param("page", "1")
                .param("limit", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(matchingTeam.getId()))
        .andExpect(jsonPath("$.content[0].name").value("IT Support"))
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  void shouldPaginateServiceTeamsNewestFirst() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam oldestTeam = testDataFactory.createServiceTeam(organization, "Oldest Team");
    ServiceTeam middleTeam = testDataFactory.createServiceTeam(organization, "Middle Team");
    ServiceTeam newestTeam = testDataFactory.createServiceTeam(organization, "Newest Team");

    mockMvc
        .perform(
            get("/service-teams")
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].id").value(newestTeam.getId()))
        .andExpect(jsonPath("$.content[1].id").value(middleTeam.getId()))
        .andExpect(jsonPath("$.totalElements").value(3))
        .andExpect(jsonPath("$.totalPages").value(2))
        .andExpect(jsonPath("$.number").value(0))
        .andExpect(jsonPath("$.first").value(true))
        .andExpect(jsonPath("$.last").value(false));

    mockMvc
        .perform(
            get("/service-teams")
                .cookie(auth.sessionCookie())
                .param("page", "2")
                .param("limit", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id").value(oldestTeam.getId()))
        .andExpect(jsonPath("$.number").value(1))
        .andExpect(jsonPath("$.first").value(false))
        .andExpect(jsonPath("$.last").value(true));
  }

  @Test
  void shouldRejectPageBelowOne() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(
            get("/service-teams")
                .cookie(auth.sessionCookie())
                .param("page", "0")
                .param("limit", "10"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must be greater than or equal to 1"));
  }

  @Test
  void shouldRejectLimitBelowOne() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(
            get("/service-teams")
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "0"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must be greater than or equal to 1"));
  }

  @Test
  void shouldRejectLimitAbove100() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(
            get("/service-teams")
                .cookie(auth.sessionCookie())
                .param("page", "1")
                .param("limit", "101"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must be less than or equal to 100"));
  }
}
