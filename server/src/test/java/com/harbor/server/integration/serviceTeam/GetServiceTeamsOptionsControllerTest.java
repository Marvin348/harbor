package com.harbor.server.integration.serviceTeam;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import org.junit.jupiter.api.Test;

public class GetServiceTeamsOptionsControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldGetServiceTeamOptionsSortedByName() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();
    ServiceTeam zebraTeam = testDataFactory.createServiceTeam(organization, "Zebra Support");
    ServiceTeam alphaTeam = testDataFactory.createServiceTeam(organization, "Alpha Support");
    ServiceTeam middleTeam = testDataFactory.createServiceTeam(organization, "Middle Support");

    Organization otherOrganization = testDataFactory.createOrganization();
    testDataFactory.createServiceTeam(otherOrganization, "Foreign Team");

    mockMvc
        .perform(get("/service-teams/options").cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(
            jsonPath(
                "$[*].id",
                contains(
                    alphaTeam.getId().intValue(),
                    middleTeam.getId().intValue(),
                    zebraTeam.getId().intValue())))
        .andExpect(
            jsonPath("$[*].name", contains("Alpha Support", "Middle Support", "Zebra Support")));
  }

  @Test
  void shouldReturnEmptyListWhenOrganizationHasNoServiceTeams() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();

    mockMvc
        .perform(get("/service-teams/options").cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }
}
