package com.harbor.server.features.auth.service;

import com.harbor.server.common.exception.UnauthorizedException;
import com.harbor.server.features.auth.dto.request.LoginRequest;
import com.harbor.server.features.auth.dto.response.LoginResponse;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public LoginResponse login(LoginRequest request) {

    String email = request.email().trim().toLowerCase();

    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

    boolean isPasswordValid = passwordEncoder.matches(request.password(), user.getPasswordHash());

    if (!isPasswordValid) {
      throw new UnauthorizedException("Invalid credentials");
    }

    return new LoginResponse(
        user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole());
  }
}
