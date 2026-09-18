package com.harbor.server.integration.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.serviceTeam.model.ServiceTeam;
import com.harbor.server.features.services.model.Service;
import com.harbor.server.features.services.model.ServiceStatus;
import com.harbor.server.features.services.repository.ServiceRepository;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class CreateServiceControllerTest extends AbstractControllerIntegrationTest {

  @Autowired private ServiceRepository serviceRepository;

  @Test
  void shouldCreateServiceSuccessfully() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Cookie sessionCookie = auth.sessionCookie();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "IT Support");

    String requestBody =
        """
        {
          "name": "  Incident Management  ",
          "description": "  Handles incidents from creation to resolution  ",
          "serviceTeamId": %d
        }
        """
            .formatted(serviceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value("Incident Management"))
        .andExpect(jsonPath("$.description").value("Handles incidents from creation to resolution"))
        .andExpect(jsonPath("$.status").value("DRAFT"))
        .andExpect(jsonPath("$.createdAt").isNotEmpty());

    assertEquals(1, serviceRepository.count());

    Service service = serviceRepository.findAll().getFirst();

    assertEquals("Incident Management", service.getName());
    assertEquals("Handles incidents from creation to resolution", service.getDescription());
    assertEquals(ServiceStatus.DRAFT, service.getStatus());
    assertEquals(auth.user().getOrganization().getId(), service.getOrganization().getId());
    assertEquals(serviceTeam.getId(), service.getServiceTeam().getId());
    assertNotNull(service.getCreatedAt());
  }

  @Test
  void shouldRejectServiceTeamFromAnotherOrganization() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();
    Organization otherOrganization = testDataFactory.createOrganization();
    ServiceTeam otherServiceTeam =
        testDataFactory.createServiceTeam(otherOrganization, "IT Support");

    String requestBody =
        """
        {
          "name": "Incident Management",
          "description": "Handles incidents",
          "serviceTeamId": %d
        }
        """
            .formatted(otherServiceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Service team not found"));

    assertEquals(0, serviceRepository.count());
  }

  @Test
  void shouldRejectDuplicateNameInSameServiceTeamIgnoringCase() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Cookie sessionCookie = auth.sessionCookie();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "IT Support");
    Service existingService =
        testDataFactory.createService(auth.user().getOrganization(), serviceTeam, "Test Service");

    String requestBody =
        """
        {
          "name": "test service",
          "description": "Another description",
          "serviceTeamId": %d
        }
        """
            .formatted(serviceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("Conflict"))
        .andExpect(jsonPath("$.message").value("Service name already exists"));

    assertEquals(1, serviceRepository.count());
    assertEquals(existingService.getId(), serviceRepository.findAll().getFirst().getId());
  }

  @Test
  void shouldAllowSameNameInDifferentServiceTeam() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    Organization organization = auth.user().getOrganization();
    ServiceTeam existingServiceTeam = testDataFactory.createServiceTeam(organization, "IT Support");
    ServiceTeam otherServiceTeam =
        testDataFactory.createServiceTeam(organization, "Customer Support");
    testDataFactory.createService(organization, existingServiceTeam, "Test Service");

    String requestBody =
        """
        {
          "name": "test service",
          "description": "Same name, different team",
          "serviceTeamId": %d
        }
        """
            .formatted(otherServiceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("test service"));

    assertEquals(2, serviceRepository.count());
  }

  @Test
  void shouldStoreBlankDescriptionAsNull() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "IT Support");

    String requestBody =
        """
        {
          "name": "Incident Management",
          "description": "   ",
          "serviceTeamId": %d
        }
        """
            .formatted(serviceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.description").doesNotExist());

    assertNull(serviceRepository.findAll().getFirst().getDescription());
  }

  @Test
  void shouldRejectBlankName() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "IT Support");

    String requestBody =
        """
        {
          "name": "   ",
          "description": "Handles incidents",
          "serviceTeamId": %d
        }
        """
            .formatted(serviceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must not be blank"));

    assertEquals(0, serviceRepository.count());
  }

  @Test
  void shouldRejectNameLongerThan100Characters() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "IT Support");

    String requestBody =
        """
        {
          "name": "%s",
          "description": "Handles incidents",
          "serviceTeamId": %d
        }
        """
            .formatted("a".repeat(101), serviceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("size must be between 0 and 100"));

    assertEquals(0, serviceRepository.count());
  }

  @Test
  void shouldRejectDescriptionLongerThan250Characters() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAdmin();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), "IT Support");

    String requestBody =
        """
        {
          "name": "Incident Management",
          "description": "%s",
          "serviceTeamId": %d
        }
        """
            .formatted("a".repeat(251), serviceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("size must be between 0 and 250"));

    assertEquals(0, serviceRepository.count());
  }

  @Test
  void shouldRejectMissingServiceTeamId() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "Incident Management",
          "description": "Handles incidents"
        }
        """;

    mockMvc
        .perform(
            post("/services")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("must not be null"));

    assertEquals(0, serviceRepository.count());
  }

  @Test
  void shouldRejectUnknownServiceTeam() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    String requestBody =
        """
        {
          "name": "Incident Management",
          "description": "Handles incidents",
          "serviceTeamId": 999999
        }
        """;

    mockMvc
        .perform(
            post("/services")
                .cookie(sessionCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value("Service team not found"));

    assertEquals(0, serviceRepository.count());
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    String requestBody =
        """
        {
          "name": "Incident Management",
          "description": "Handles incidents",
          "serviceTeamId": 1
        }
        """;

    mockMvc
        .perform(post("/services").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isUnauthorized());

    assertEquals(0, serviceRepository.count());
  }

  @Test
  void shouldRejectAgent() throws Exception {
    AuthenticatedTestUser auth = testAuthHelper.loginAgent();
    ServiceTeam serviceTeam =
        testDataFactory.createServiceTeam(auth.user().getOrganization(), " IT Support");

    String requestBody =
        """
        {
          "name": "Incident Management",
          "description": "Handles incidents",
          "serviceTeamId": %d
        }
        """
            .formatted(serviceTeam.getId());

    mockMvc
        .perform(
            post("/services")
                .cookie(auth.sessionCookie())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isForbidden());

    assertEquals(0, serviceRepository.count());
  }
}
