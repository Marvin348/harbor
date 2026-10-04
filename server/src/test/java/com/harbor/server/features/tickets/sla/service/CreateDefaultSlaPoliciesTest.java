package com.harbor.server.features.tickets.sla.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateDefaultSlaPoliciesTest {

  @Mock private SlaPolicyRepository slaPolicyRepository;
  @InjectMocks private CreateDefaultSlaPolicies createDefaultSlaPolicies;

  @Test
  void shouldCreateDefaultPolicyForEveryTicketPriority() {
    Organization organization = new Organization("Harbor Test GmbH");

    createDefaultSlaPolicies.execute(organization);

    ArgumentCaptor<SlaPolicy> policyCaptor = ArgumentCaptor.forClass(SlaPolicy.class);
    verify(slaPolicyRepository, times(4)).save(policyCaptor.capture());

    List<SlaPolicy> policies = policyCaptor.getAllValues();

    assertDefaultPolicy(
        policies, organization, TicketPriority.LOW, "Low SLA", 240, 2880);
    assertDefaultPolicy(
        policies, organization, TicketPriority.MEDIUM, "Medium SLA", 120, 1440);
    assertDefaultPolicy(
        policies, organization, TicketPriority.HIGH, "High SLA", 60, 480);
    assertDefaultPolicy(
        policies, organization, TicketPriority.CRITICAL, "Critical SLA", 30, 240);
  }

  private void assertDefaultPolicy(
      List<SlaPolicy> policies,
      Organization organization,
      TicketPriority priority,
      String name,
      int responseTimeMinutes,
      int resolutionTimeMinutes) {
    SlaPolicy policy =
        policies.stream()
            .filter(candidate -> candidate.getTicketPriority() == priority)
            .findFirst()
            .orElseThrow();

    assertSame(organization, policy.getOrganization());
    assertEquals(name, policy.getName());
    assertEquals(responseTimeMinutes, policy.getResponseTimeMinutes());
    assertEquals(resolutionTimeMinutes, policy.getResolutionTimeMinutes());
    assertTrue(policy.isEnabled());
  }
}
