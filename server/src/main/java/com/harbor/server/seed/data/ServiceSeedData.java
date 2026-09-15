package com.harbor.server.seed.data;

import java.util.List;

public class ServiceSeedData {

  public record ServiceSeed(String name, String description, String serviceTeamName) {}

  public static final List<ServiceSeed> SERVICES =
      List.of(
          new ServiceSeed(
              "Employee Onboarding",
              "Bereitstellung von Geraeten, Basissoftware und Arbeitsplatzzugang fuer neue Mitarbeitende.",
              "Workplace Support"),
          new ServiceSeed(
              "Connectivity Support",
              "Stoerungsannahme und Bearbeitung fuer VPN, WLAN, LAN und Internetzugang.",
              "Network Operations"),
          new ServiceSeed(
              "Access Management",
              "Beantragung, Aenderung und Entzug von Benutzerrechten in Unternehmenssystemen.",
              "Identity Access"),
          new ServiceSeed(
              "CRM Support",
              "Support fuer Vertriebsprozesse, Kundendaten und CRM-Workflows.",
              "Business Applications"),
          new ServiceSeed(
              "Cloud Hosting",
              "Betrieb und Support fuer gehostete Anwendungen, Umgebungen und Deployments.",
              "Cloud Platform"),
          new ServiceSeed(
              "Security Incident Response",
              "Analyse und Bearbeitung von Sicherheitsvorfaellen und verdachtigen Aktivitaeten.",
              "Security Operations"));
}
