package com.harbor.server.integration.helper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harbor.server.features.user.model.User;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class TestAuthHelper {

  private final MockMvc mockMvc;
  private final TestDataFactory testDataFactory;

  public AuthenticatedTestUser loginAdmin() throws Exception {
    User user = testDataFactory.createAdmin();

    return login(user);
  }

  public AuthenticatedTestUser loginAgent() throws Exception {
    User user = testDataFactory.createAgent();

    return login(user);
  }

  public AuthenticatedTestUser loginRequester() throws Exception {
    User user = testDataFactory.createRequester();

    return login(user);
  }

  private AuthenticatedTestUser login(User user) throws Exception {
    String requestBody =
        """
         {
           "email": "%s",
           "password": "Password123!"
         }
         """
            .formatted(user.getEmail());

    MvcResult result =
        mockMvc
            .perform(
                post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isOk())
            .andReturn();

    Cookie sessionCookie = result.getResponse().getCookie("SESSION");

    return new AuthenticatedTestUser(user, sessionCookie);
  }
}
