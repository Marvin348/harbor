package com.harbor.server.features.auth.controller;

import com.harbor.server.features.auth.dto.response.RegisterResponse;
import com.harbor.server.features.auth.dto.request.RegisterRequest;
import com.harbor.server.features.auth.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final RegisterService registerService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public RegisterResponse register(@Valid @RequestBody RegisterRequest body) {
    return registerService.register(body);
  }
}
