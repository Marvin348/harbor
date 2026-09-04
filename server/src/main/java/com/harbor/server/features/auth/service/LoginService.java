package com.harbor.server.features.auth.service;

import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.auth.dto.request.LoginRequest;
import com.harbor.server.features.auth.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;

  public LoginResponse login(
      LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

    String email = request.email().trim().toLowerCase();

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, request.password()));

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);

    securityContextRepository.saveContext(context, httpRequest, httpResponse);

    return new LoginResponse(
        userDetails.getId(),
        userDetails.getEmail(),
        userDetails.getFirstName(),
        userDetails.getLastName(),
        userDetails.getRole());
  }
}
