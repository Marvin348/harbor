package com.harbor.server.features.auth.controller;

import com.harbor.server.common.security.CustomUserDetails;
import com.harbor.server.features.auth.dto.request.LoginRequest;
import com.harbor.server.features.auth.dto.response.CurrentUserResponse;
import com.harbor.server.features.auth.dto.response.RegisterResponse;
import com.harbor.server.features.auth.dto.request.RegisterRequest;
import com.harbor.server.features.auth.dto.response.LoginResponse;
import com.harbor.server.features.auth.service.LoginService;
import com.harbor.server.features.auth.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final RegisterService registerService;
  private final LoginService loginService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public RegisterResponse register(@Valid @RequestBody RegisterRequest body) {
    return registerService.register(body);
  }

  @PostMapping("/login")
  public LoginResponse login(
      @Valid @RequestBody LoginRequest body,
      HttpServletRequest request,
      HttpServletResponse response) {
    return loginService.login(body, request, response);
  }

  @GetMapping("/me")
  public CurrentUserResponse me(Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    return new CurrentUserResponse(
        userDetails.getId(),
        userDetails.getEmail(),
        userDetails.getFirstName(),
        userDetails.getLastName(),
        userDetails.getRole(),
        userDetails.getOrganizationId(),
        userDetails.getOrganizationName());
  }
}
