package com.harbor.server.features.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

  public void logout(HttpServletRequest httpRequest) {
    HttpSession session = httpRequest.getSession(false);

    if (session != null) {
      session.invalidate();
    }

    SecurityContextHolder.clearContext();
  }
}
