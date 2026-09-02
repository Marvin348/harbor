package com.harbor.server.features.auth.service;

import com.harbor.server.common.exception.ConflictException;
import com.harbor.server.features.auth.dto.request.RegisterRequest;
import com.harbor.server.features.auth.dto.response.RegisterResponse;
import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import com.harbor.server.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService {
  private final OrganizationRepository organizationRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public RegisterResponse register(RegisterRequest request) {

    String email = request.email().trim().toLowerCase();

    if (userRepository.existsByEmail(email)) {
      throw new ConflictException("Email already exists");
    }

    Organization organization = new Organization(request.companyName());
    organizationRepository.save(organization);

    String passwordHash = passwordEncoder.encode(request.password());

    User user =
        new User(
            request.firstName(),
            request.lastName(),
            email,
            passwordHash,
            OrganizationRole.ORGANIZATION_ADMIN,
            organization);

    userRepository.save(user);

    return new RegisterResponse(
        user.getId(),
        organization.getId(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        organization.getName(),
        user.getRole());
  }
}
