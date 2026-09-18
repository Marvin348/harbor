package com.harbor.server.integration.auth;

import com.harbor.server.features.auth.dto.request.RegisterRequest;
import com.harbor.server.features.auth.service.RegisterService;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import com.harbor.server.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

public class RegisterServiceTransactionTest extends AbstractIntegrationTest {

  @MockitoSpyBean private UserRepository userRepository;
  @Autowired private OrganizationRepository organizationRepository;
  @Autowired private RegisterService registerService;

  @Test
  void shouldRollbackRegistrationWhenUserCreationFails() {
    RegisterRequest request =
        new RegisterRequest(
            "max.mustermann@example.com", "Max", "Mustermann", "Harbor Test GmbH", "Password123!");

    doThrow(new IllegalStateException("User creation failed"))
        .when(userRepository)
        .save(any(User.class));

    assertThrows(IllegalStateException.class, () -> registerService.register(request));

    assertEquals(0, organizationRepository.count());
  }
}
