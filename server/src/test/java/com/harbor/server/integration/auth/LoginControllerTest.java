package com.harbor.server.integration.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
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
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class LoginControllerTest extends AbstractControllerIntegrationTest {

  @Autowired private StringRedisTemplate redisTemplate;
  @Autowired private OrganizationRepository organizationRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void shouldLoginSuccessfully() throws Exception {
    Organization organization = organizationRepository.save(new Organization("Harbor Test GmbH"));

    User user =
        userRepository.save(
            new User(
                "Max",
                "Mustermann",
                "max.mustermann@example.com",
                passwordEncoder.encode("Password123!"),
                OrganizationRole.ORGANIZATION_ADMIN,
                organization));

    String requestBody =
        """
        {
          "email": "MAX.MUSTERMANN@EXAMPLE.COM",
          "password": "Password123!"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andExpect(cookie().exists("SESSION"))
        .andExpect(jsonPath("$.id").value(user.getId()))
        .andExpect(jsonPath("$.email").value("max.mustermann@example.com"))
        .andExpect(jsonPath("$.firstName").value("Max"))
        .andExpect(jsonPath("$.lastName").value("Mustermann"))
        .andExpect(jsonPath("$.role").value("ORGANIZATION_ADMIN"));

    Set<String> sessionKeys = redisTemplate.keys("spring:session:sessions:*");

    assertNotNull(sessionKeys);

    boolean sessionExists =
        sessionKeys.stream().anyMatch(key -> redisTemplate.type(key) == DataType.HASH);

    assertTrue(sessionExists);
  }

  @Test
  void shouldRejectBlankPassword() throws Exception {
    String requestBody =
        """
        {
          "email": "max.mustermann@example.com",
          "password": ""
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Password is required"));
  }

  @Test
  void shouldRejectWrongPassword() throws Exception {
    Organization organization = organizationRepository.save(new Organization("Harbor Test GmbH"));

    User user =
        userRepository.save(
            new User(
                "Max",
                "Mustermann",
                "max.mustermann@example.com",
                passwordEncoder.encode("Password123!"),
                OrganizationRole.ORGANIZATION_ADMIN,
                organization));

    String requestBody =
        """
         {
           "email": "max.mustermann@example.com",
           "password": "wrongPassword"
         }
         """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  void shouldRejectInvalidEmail() throws Exception {
    Organization organization = organizationRepository.save(new Organization("Harbor Test GmbH"));

    User user =
        userRepository.save(
            new User(
                "Max",
                "Mustermann",
                "max.mustermann@example.com",
                passwordEncoder.encode("Password123!"),
                OrganizationRole.ORGANIZATION_ADMIN,
                organization));

    String requestBody =
        """
        {
          "email": "wrong-email.com",
          "password": "Password123!"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Email must be valid"));
  }

  @Test
  void shouldRejectUnknownEmail() throws Exception {
    Organization organization = organizationRepository.save(new Organization("Harbor Test GmbH"));

    User user =
        userRepository.save(
            new User(
                "Max",
                "Mustermann",
                "max.mustermann@example.com",
                passwordEncoder.encode("Password123!"),
                OrganizationRole.ORGANIZATION_ADMIN,
                organization));

    String requestBody =
        """
        {
          "email": "wrong@email.com",
          "password": "Password123!"
        }
        """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }
}
