package com.harbor.server.integration.services;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;

public class GetServicesControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldReturnServicesFromAuthenticatedOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    Organization organization = auth.user().getOrganization();

    ServiceTeam supportTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    ServiceTeam platformTeam = testDataFactory.createServiceTeam(organization, "Platform");
    Service incidentService =
        testDataFactory.createService(organization, supportTeam, "Incident Management");
    Service monitoringService =
        testDataFactory.createService(organization, platformTeam, "Monitoring");

    Organization otherOrganization = testDataFactory.createOrganization();
    ServiceTeam otherTeam =
        testDataFactory.createServiceTeam(otherOrganization, "External Support");
    testDataFactory.createService(otherOrganization, otherTeam, "Foreign Service");

    mockMvc
        .perform(get("/services").cookie(auth.sessionCookie()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(
            jsonPath(
                "$[*].id",
                containsInAnyOrder(
                    incidentService.getId().intValue(), monitoringService.getId().intValue())))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("Incident Management", "Monitoring")))
        .andExpect(
            jsonPath("$[*].description", everyItem(is("This is the description for the service"))))
        .andExpect(jsonPath("$[*].status", everyItem(is("DRAFT"))))
        .andExpect(jsonPath("$[*].createdAt", everyItem(not(emptyOrNullString()))));
  }

  @Test
  void shouldReturnEmptyListWhenOrganizationHasNoServices() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(get("/services").cookie(sessionCookie))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }
}
