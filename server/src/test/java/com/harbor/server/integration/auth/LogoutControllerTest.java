package com.harbor.server.integration.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.integration.AbstractControllerIntegrationTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;

public class LogoutControllerTest extends AbstractControllerIntegrationTest {

  @Test
  void shouldLogoutSuccessfully() throws Exception {
    Cookie sessionCookie = testAuthHelper.loginAdmin().sessionCookie();

    mockMvc
        .perform(post("/auth/logout").cookie(sessionCookie))
        .andExpect(status().isNoContent())
        .andExpect(cookie().value("SESSION", ""))
        .andExpect(cookie().maxAge("SESSION", 0));
  }

  @Test
  void shouldRejectLogoutWithoutAuthentication() throws Exception {
    mockMvc.perform(post("/auth/logout")).andExpect(status().isUnauthorized());
  }
}
