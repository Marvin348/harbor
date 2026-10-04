package com.harbor.server.features.tickets.sla.service;

import com.harbor.server.features.organization.model.Organization;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.sla.model.SlaPolicy;
import com.harbor.server.features.tickets.sla.repository.SlaPolicyRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDefaultSlaPolicies {
  private final SlaPolicyRepository slaPolicyRepository;

  public void execute(Organization organization) {

    for (var entry : SLA_DEFAULTS.entrySet()) {
      TicketPriority priority = entry.getKey();
      DefaultSlaPolicy slaDefaults = entry.getValue();

      slaPolicyRepository.save(
          new SlaPolicy(
              organization,
              policyNameFor(priority),
              priority,
              slaDefaults.responseTimeMinutes(),
              slaDefaults.resolutionTimeMinutes()));
    }
  }

  public static final Map<TicketPriority, DefaultSlaPolicy> SLA_DEFAULTS =
      Map.of(
          TicketPriority.LOW,
          new DefaultSlaPolicy(240, 2880),
          TicketPriority.MEDIUM,
          new DefaultSlaPolicy(120, 1440),
          TicketPriority.HIGH,
          new DefaultSlaPolicy(60, 480),
          TicketPriority.CRITICAL,
          new DefaultSlaPolicy(30, 240));

  public static String policyNameFor(TicketPriority priority) {
    String value = priority.name().toLowerCase();
    return Character.toUpperCase(value.charAt(0)) + value.substring(1) + " SLA";
  }

  public record DefaultSlaPolicy(int responseTimeMinutes, int resolutionTimeMinutes) {}
}
