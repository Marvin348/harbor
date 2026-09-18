package com.harbor.server.integration.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterControllerTest extends AbstractControllerIntegrationTest {

  @Autowired private OrganizationRepository organizationRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  private String registrationRequest(
      String email, String firstName, String lastName, String companyName, String password) {
    return """
        {
          "email": "%s",
          "firstName": "%s",
          "lastName": "%s",
          "companyName": "%s",
          "password": "%s"
        }
        """
        .formatted(email, firstName, lastName, companyName, password);
  }

  private void expectBadRequest(String requestBody, String message) throws Exception {
    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value(message));
  }

  @Test
  void shouldRegisterUserSuccessfully() throws Exception {
    String requestBody =
        """
        {
          "email": "max.mustermann@example.com",
          "firstName": "Max",
          "lastName": "Mustermann",
          "companyName": "Harbor Test GmbH",
          "password": "Password123!"
        }
        """;

    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").isNumber())
        .andExpect(jsonPath("$.organizationId").isNumber())
        .andExpect(jsonPath("$.email").value("max.mustermann@example.com"))
        .andExpect(jsonPath("$.firstName").value("Max"))
        .andExpect(jsonPath("$.lastName").value("Mustermann"))
        .andExpect(jsonPath("$.organizationName").value("Harbor Test GmbH"))
        .andExpect(jsonPath("$.role").value("ORGANIZATION_ADMIN"));

    assertEquals(1, organizationRepository.count());
    assertEquals(1, userRepository.count());

    User user = userRepository.findByEmail("max.mustermann@example.com").orElseThrow();

    assertEquals("Max", user.getFirstName());
    assertEquals("Mustermann", user.getLastName());
    assertEquals(OrganizationRole.ORGANIZATION_ADMIN, user.getRole());

    assertNotEquals("Password123!", user.getPasswordHash());
    assertTrue(passwordEncoder.matches("Password123!", user.getPasswordHash()));

    assertNotNull(user.getOrganization());
    assertEquals("Harbor Test GmbH", user.getOrganization().getName());
  }

  @Test
  void shouldRejectBlankEmail() throws Exception {
    String requestBody =
        registrationRequest("", "Max", "Mustermann", "Harbor Test GmbH", "Password123!");

    expectBadRequest(requestBody, "Email is required");
  }

  @Test
  void shouldRejectEmailLongerThan100Characters() throws Exception {
    String requestBody =
        registrationRequest(
            "a".repeat(60) + "@" + "b".repeat(30) + "." + "c".repeat(30) + ".com",
            "Max",
            "Mustermann",
            "Harbor Test GmbH",
            "Password123!");

    expectBadRequest(requestBody, "Email must not exceed 100 characters");
  }

  @Test
  void shouldRejectInvalidEmail() throws Exception {
    String requestBody =
        """
        {
          "email": "not-an-email",
          "firstName": "Max",
          "lastName": "Mustermann",
          "companyName": "Harbor Test GmbH",
          "password": "Password123!"
        }
        """;

    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Email must be valid"));
  }

  @Test
  void shouldRejectBlankPassword() throws Exception {
    String requestBody =
        """
        {
          "email": "max.mustermann@example.com",
          "firstName": "Max",
          "lastName": "Mustermann",
          "companyName": "Harbor Test GmbH",
          "password": "        "
        }
        """;

    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Password is required"));
  }

  @Test
  void shouldRejectBlankFirstName() throws Exception {
    String requestBody =
        """
        {
          "email": "max.mustermann@example.com",
          "firstName": "  ",
          "lastName": "Mustermann",
          "companyName": "Harbor Test GmbH",
          "password": "Password123!"
        }
        """;

    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("First name is required"));
  }

  @Test
  void shouldRejectFirstNameShorterThan2Characters() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com", "M", "Mustermann", "Harbor Test GmbH", "Password123!");

    expectBadRequest(requestBody, "First name must be between 2 and 50 characters");
  }

  @Test
  void shouldRejectFirstNameLongerThan50Characters() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com",
            "M".repeat(51),
            "Mustermann",
            "Harbor Test GmbH",
            "Password123!");

    expectBadRequest(requestBody, "First name must be between 2 and 50 characters");
  }

  @Test
  void shouldRejectBlankLastName() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com", "Max", "   ", "Harbor Test GmbH", "Password123!");

    expectBadRequest(requestBody, "Last name is required");
  }

  @Test
  void shouldRejectLastNameShorterThan2Characters() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com", "Max", "M", "Harbor Test GmbH", "Password123!");

    expectBadRequest(requestBody, "Last name must be between 2 and 50 characters");
  }

  @Test
  void shouldRejectLastNameLongerThan50Characters() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com",
            "Max",
            "M".repeat(51),
            "Harbor Test GmbH",
            "Password123!");

    expectBadRequest(requestBody, "Last name must be between 2 and 50 characters");
  }

  @Test
  void shouldRejectBlankCompanyName() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com", "Max", "Mustermann", "   ", "Password123!");

    expectBadRequest(requestBody, "Company name is required");
  }

  @Test
  void shouldRejectCompanyNameShorterThan2Characters() throws Exception {
    String requestBody =
        registrationRequest("max.mustermann@example.com", "Max", "Mustermann", "H", "Password123!");

    expectBadRequest(requestBody, "Company name must be between 2 and 100 characters");
  }

  @Test
  void shouldRejectCompanyNameLongerThan100Characters() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com", "Max", "Mustermann", "H".repeat(101), "Password123!");

    expectBadRequest(requestBody, "Company name must be between 2 and 100 characters");
  }

  @Test
  void shouldRejectPasswordShorterThan8Characters() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com", "Max", "Mustermann", "Harbor Test GmbH", "Pass123");

    expectBadRequest(requestBody, "Password must be between 8 and 72 characters");
  }

  @Test
  void shouldRejectPasswordLongerThan72Characters() throws Exception {
    String requestBody =
        registrationRequest(
            "max.mustermann@example.com", "Max", "Mustermann", "Harbor Test GmbH", "P".repeat(73));

    expectBadRequest(requestBody, "Password must be between 8 and 72 characters");
  }

  @Test
  void shouldRejectDuplicateEmail() throws Exception {
    Organization organization = organizationRepository.save(new Organization("Existing Company"));

    User existingUser =
        userRepository.save(
            new User(
                "Max",
                "Mustermann",
                "existing@harbor.local",
                passwordEncoder.encode("Password123!"),
                OrganizationRole.ORGANIZATION_ADMIN,
                organization));

    userRepository.save(existingUser);

    String requestBody =
        """
        {
          "email": "existing@harbor.local",
          "firstName": "Anna",
          "lastName": "Schmidt",
          "companyName": "Another Company",
          "password": "Password123!"
        }
        """;

    mockMvc
        .perform(
            post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("Conflict"))
        .andExpect(jsonPath("$.message").value("Email already exists"));
  }
}
