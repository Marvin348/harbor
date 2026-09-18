package com.harbor.server.integration.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.user.model.User;
import com.harbor.server.integration.AbstractControllerIntegrationTest;
import com.harbor.server.integration.helper.AuthenticatedTestUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;

public class GetCurrentUserControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldGetCurrentUserSuccessfully() throws Exception {
    AuthenticatedTestUser authenticatedUser = testAuthHelper.loginAdmin();
    User user = authenticatedUser.user();
    Cookie sessionCookie = authenticatedUser.sessionCookie();

    mockMvc
        .perform(get("/auth/me").cookie(sessionCookie))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(user.getId()))
        .andExpect(jsonPath("$.email").value(user.getEmail()))
        .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(user.getLastName()))
        .andExpect(jsonPath("$.role").value(user.getRole().name()))
        .andExpect(jsonPath("$.organizationId").value(user.getOrganization().getId()))
        .andExpect(jsonPath("$.organizationName").value(user.getOrganization().getName()));
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc.perform(get("/auth/me")).andExpect(status().isUnauthorized());
  }
}
