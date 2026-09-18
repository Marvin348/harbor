package com.harbor.server.integration.helper;

import com.harbor.server.features.user.model.User;
import jakarta.servlet.http.Cookie;

public record AuthenticatedTestUser(User user, Cookie sessionCookie) {}
