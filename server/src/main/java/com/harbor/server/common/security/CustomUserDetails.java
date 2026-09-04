package com.harbor.server.common.security;

import com.harbor.server.features.user.model.OrganizationRole;
import com.harbor.server.features.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

  private final Long id;
  private final String email;
  private final String password;
  private final String firstName;
  private final String lastName;
  private final OrganizationRole role;
  private final Long organizationId;
  private final String organizationName;

  public CustomUserDetails(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.password = user.getPasswordHash();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.role = user.getRole();

    this.organizationId = user.getOrganization().getId();
    this.organizationName = user.getOrganization().getName();
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public OrganizationRole getRole() {
    return role;
  }

  public Long getOrganizationId() {
    return organizationId;
  }

  public String getOrganizationName() {
    return organizationName;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }
}
