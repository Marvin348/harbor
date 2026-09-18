package com.harbor.server.integration.serviceTeam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class CreateServiceTeamControllerTest extends AbstractControllerIntegrationTest {
  @Autowired private ServiceTeamRepository serviceTeamRepository;

  @Test
  void shouldCreateServiceTeamSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();

    String requestBody =
        """
        {
          "name": "  Test Team  ",
          "description": "  Some description  "
        }
        """;

    mockMvc
        .perform(
            post("/service-teams")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value("Test Team"))
        .andExpect(jsonPath("$.description").value("Some description"));

    assertEquals(1, serviceTeamRepository.count());

    ServiceTeam serviceTeam = serviceTeamRepository.findAll().getFirst();

    assertEquals("Test Team", serviceTeam.getName());
    assertEquals("Some description", serviceTeam.getDescription());
    assertEquals(auth.user().getOrganization().getId(), serviceTeam.getOrganization().getId());
    assertNotNull(serviceTeam.getCreatedAt());
  }

  @Test
  void shouldRejectDuplicateNameInSameOrganizationIgnoringCase() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    ServiceTeam existingTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "IT Support");

    String requestBody =
        """
        {
          "name": "  it support  ",
          "description": "Another description"
        }
        """;

    mockMvc
        .perform(
            post("/service-teams")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("Conflict"))
        .andExpect(jsonPath("$.message").value("Service team already exists"));

    assertEquals(1, serviceTeamRepository.count());
    assertEquals(existingTeam.getId(), serviceTeamRepository.findAll().getFirst().getId());
  }

  @Test
  void shouldAllowSameNameInDifferentOrganization() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization otherOrganization = testDataFactory.createOrganization();
    testDataFactory.createServiceTeam(otherOrganization, "IT Support");

    String requestBody =
        """
        {
          "name": "IT Support",
          "description": "Support for this organization"
        }
        """;

    mockMvc
        .perform(
            post("/service-teams")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("IT Support"));

    assertEquals(2, serviceTeamRepository.count());
    assertEquals(
        1,
        serviceTeamRepository
            .findByOrganizationId(auth.user().getOrganization().getId())
            .size());
  }

  @Test
  void shouldRejectBlankName() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "   ",
          "description": "Some description"
        }
        """;

    mockMvc
        .perform(
            post("/service-teams")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must not be blank"));

    assertEquals(0, serviceTeamRepository.count());
  }

  @Test
  void shouldRejectNameLongerThan100Characters() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "%s",
          "description": "Some description"
        }
        """
            .formatted("a".repeat(101));

    mockMvc
        .perform(
            post("/service-teams")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("size must be between 0 and 100"));

    assertEquals(0, serviceTeamRepository.count());
  }

  @Test
  void shouldRejectBlankDescription() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "IT Support",
          "description": "   "
        }
        """;

    mockMvc
        .perform(
            post("/service-teams")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must not be blank"));

    assertEquals(0, serviceTeamRepository.count());
  }

  @Test
  void shouldRejectDescriptionLongerThan250Characters() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "IT Support",
          "description": "%s"
        }
        """
            .formatted("a".repeat(251));

    mockMvc
        .perform(
            post("/service-teams")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("size must be between 0 and 250"));

    assertEquals(0, serviceTeamRepository.count());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    String requestBody =
        """
        {
          "name": "IT Support",
          "description": "Some description"
        }
        """;

    mockMvc
        .perform(
            post("/service-teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isUnauthorized());

    assertEquals(0, serviceTeamRepository.count());
  }

  @Test
  void shouldRejectAgent() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAgent().sessionCookie();

    String requestBody =
        """
        {
          "name": "IT Support",
          "description": "Some description"
        }
        """;

    mockMvc
        .perform(
            post("/service-teams")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isForbidden());

    assertEquals(0, serviceTeamRepository.count());
  }
}
