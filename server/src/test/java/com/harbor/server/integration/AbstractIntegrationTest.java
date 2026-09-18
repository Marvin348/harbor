package com.harbor.server.integration;

import com.harbor.server.features.organization.repository.OrganizationRepository;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamMemberRepository;
import com.harbor.server.features.serviceTeam.repository.ServiceTeamRepository;
import com.harbor.server.features.services.repository.ServiceRepository;
import com.harbor.server.features.tickets.repository.TicketRepository;
import com.harbor.server.features.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
public abstract class AbstractIntegrationTest {

  static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

  static final GenericContainer<?> redis =
      new GenericContainer<>("redis:7.4-alpine").withExposedPorts(6379);

  static {
    postgres.start();
    redis.start();
  }

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);

    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
  }

  @Autowired private TicketRepository ticketRepository;
  @Autowired private ServiceTeamRepository serviceTeamRepository;
  @Autowired private ServiceRepository serviceRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ServiceTeamMemberRepository serviceTeamMemberRepository;
  @Autowired private OrganizationRepository organizationRepository;

  @BeforeEach
  protected void cleanDatabase() {
    ticketRepository.deleteAllInBatch();
    serviceRepository.deleteAllInBatch();
    serviceTeamMemberRepository.deleteAllInBatch();
    serviceTeamRepository.deleteAllInBatch();
    userRepository.deleteAllInBatch();
    organizationRepository.deleteAllInBatch();
  }
}
