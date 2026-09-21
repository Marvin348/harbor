package com.harbor.server.features.tickets.service;

import com.harbor.server.features.tickets.dto.request.TicketAssessmentRequest;
import com.harbor.server.features.tickets.model.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TicketPriorityCalculator {

  public TicketPriority calculate(TicketAssessmentRequest assessment) {

    TicketPriority basePriority =
        BASE_PRIORITY_MATRIX.get(assessment.impact()).get(assessment.urgency());

    return switch (assessment.businessCriticality()) {
      case LOW -> decreasePriority(basePriority);
      case NORMAL -> basePriority;
      case HIGH -> increasePriority(basePriority);
      case MISSION_CRITICAL -> increasePriority(increasePriority(basePriority));
    };
  }

  private TicketPriority increasePriority(TicketPriority current) {
    return switch (current) {
      case LOW -> TicketPriority.MEDIUM;
      case MEDIUM -> TicketPriority.HIGH;
      case HIGH, CRITICAL -> TicketPriority.CRITICAL;
    };
  }

  private TicketPriority decreasePriority(TicketPriority current) {
    return switch (current) {
      case LOW -> TicketPriority.LOW;
      case MEDIUM -> TicketPriority.LOW;
      case HIGH -> TicketPriority.MEDIUM;
      case CRITICAL -> TicketPriority.HIGH;
    };
  }

  private static final Map<TicketImpact, Map<TicketUrgency, TicketPriority>> BASE_PRIORITY_MATRIX =
      Map.of(
          TicketImpact.SINGLE_USER,
          Map.of(
              TicketUrgency.WORK_AROUND_AVAILABLE, TicketPriority.LOW,
              TicketUrgency.WORK_DEGRADED, TicketPriority.MEDIUM,
              TicketUrgency.WORK_BLOCKED, TicketPriority.HIGH),
          TicketImpact.TEAM,
          Map.of(
              TicketUrgency.WORK_AROUND_AVAILABLE, TicketPriority.MEDIUM,
              TicketUrgency.WORK_DEGRADED, TicketPriority.HIGH,
              TicketUrgency.WORK_BLOCKED, TicketPriority.HIGH),
          TicketImpact.MULTIPLE_TEAMS,
          Map.of(
              TicketUrgency.WORK_AROUND_AVAILABLE, TicketPriority.MEDIUM,
              TicketUrgency.WORK_DEGRADED, TicketPriority.HIGH,
              TicketUrgency.WORK_BLOCKED, TicketPriority.CRITICAL),
          TicketImpact.ORGANIZATION,
          Map.of(
              TicketUrgency.WORK_AROUND_AVAILABLE, TicketPriority.HIGH,
              TicketUrgency.WORK_DEGRADED, TicketPriority.CRITICAL,
              TicketUrgency.WORK_BLOCKED, TicketPriority.CRITICAL));
}
