package com.harbor.server.features.tickets.service;

import com.harbor.server.features.tickets.dto.request.TicketAssessmentRequest;
import com.harbor.server.features.tickets.model.TicketBusinessCriticality;
import com.harbor.server.features.tickets.model.TicketImpact;
import com.harbor.server.features.tickets.model.TicketPriority;
import com.harbor.server.features.tickets.model.TicketUrgency;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketPriorityCalculatorTest {

  private final TicketPriorityCalculator calculator = new TicketPriorityCalculator();

  @ParameterizedTest
  @MethodSource("priorityCases")
  void shouldCalculatePriority(
      TicketImpact impact,
      TicketUrgency urgency,
      TicketBusinessCriticality criticality,
      TicketPriority expected) {

    TicketAssessmentRequest assessment = new TicketAssessmentRequest(impact, urgency, criticality);

    TicketPriority actual = calculator.calculate(assessment);

    assertEquals(expected, actual);
  }

  static Stream<Arguments> priorityCases() {
    return Stream.of(
        Arguments.of(
            TicketImpact.SINGLE_USER,
            TicketUrgency.WORK_AROUND_AVAILABLE,
            TicketBusinessCriticality.NORMAL,
            TicketPriority.LOW),
        Arguments.of(
            TicketImpact.SINGLE_USER,
            TicketUrgency.WORK_DEGRADED,
            TicketBusinessCriticality.HIGH,
            TicketPriority.HIGH),
        Arguments.of(
            TicketImpact.MULTIPLE_TEAMS,
            TicketUrgency.WORK_BLOCKED,
            TicketBusinessCriticality.NORMAL,
            TicketPriority.CRITICAL),
        Arguments.of(
            TicketImpact.SINGLE_USER,
            TicketUrgency.WORK_AROUND_AVAILABLE,
            TicketBusinessCriticality.NORMAL,
            TicketPriority.LOW),
        Arguments.of(
            TicketImpact.MULTIPLE_TEAMS,
            TicketUrgency.WORK_AROUND_AVAILABLE,
            TicketBusinessCriticality.LOW,
            TicketPriority.LOW),
        Arguments.of(
            TicketImpact.MULTIPLE_TEAMS,
            TicketUrgency.WORK_AROUND_AVAILABLE,
            TicketBusinessCriticality.HIGH,
            TicketPriority.HIGH),
        Arguments.of(
            TicketImpact.MULTIPLE_TEAMS,
            TicketUrgency.WORK_AROUND_AVAILABLE,
            TicketBusinessCriticality.MISSION_CRITICAL,
            TicketPriority.CRITICAL),
        Arguments.of(
            TicketImpact.TEAM,
            TicketUrgency.WORK_AROUND_AVAILABLE,
            TicketBusinessCriticality.LOW,
            TicketPriority.LOW),
        Arguments.of(
            TicketImpact.TEAM,
            TicketUrgency.WORK_BLOCKED,
            TicketBusinessCriticality.HIGH,
            TicketPriority.CRITICAL),
        Arguments.of(
            TicketImpact.TEAM,
            TicketUrgency.WORK_DEGRADED,
            TicketBusinessCriticality.LOW,
            TicketPriority.MEDIUM),
        Arguments.of(
            TicketImpact.ORGANIZATION,
            TicketUrgency.WORK_AROUND_AVAILABLE,
            TicketBusinessCriticality.NORMAL,
            TicketPriority.HIGH),
        Arguments.of(
            TicketImpact.ORGANIZATION,
            TicketUrgency.WORK_DEGRADED,
            TicketBusinessCriticality.LOW,
            TicketPriority.HIGH),
        Arguments.of(
            TicketImpact.ORGANIZATION,
            TicketUrgency.WORK_BLOCKED,
            TicketBusinessCriticality.HIGH,
            TicketPriority.CRITICAL),
        Arguments.of(
            TicketImpact.ORGANIZATION,
            TicketUrgency.WORK_DEGRADED,
            TicketBusinessCriticality.MISSION_CRITICAL,
            TicketPriority.CRITICAL));
  }
}
