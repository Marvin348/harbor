package com.harbor.server.features.user.repository;

import com.harbor.server.features.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
