package com.harbor.server.seed.data;

import java.util.List;

public class ServiceTeamSeedData {

  public record ServiceTeamSeed(String name, String description) {}

  public static final List<ServiceTeamSeed> SERVICE_TEAMS =
      List.of(
          new ServiceTeamSeed(
              "Workplace Support",
              "Unterstuetzt Mitarbeitende bei Arbeitsplatzgeraeten, Standardsoftware und Druckern."),
          new ServiceTeamSeed(
              "Network Operations",
              "Betreibt interne Netzwerke, VPN, WLAN und Standortkonnektivitaet."),
          new ServiceTeamSeed(
              "Identity Access",
              "Verwaltet Benutzerkonten, Berechtigungen, Rollen und Login-Probleme."),
          new ServiceTeamSeed(
              "Business Applications",
              "Betreut zentrale Fachanwendungen wie CRM, ERP und Reporting-Tools."),
          new ServiceTeamSeed(
              "Cloud Platform",
              "Verantwortet Cloud-Infrastruktur, Laufzeitplattformen und Deployments."),
          new ServiceTeamSeed(
              "Security Operations",
              "Bearbeitet Sicherheitsereignisse, Phishing-Meldungen und Risikoanalysen."));
}
