package com.harbor.server.seed.data;

import java.util.List;

public class ServiceTeamMembersSeedData {

  public record ServiceTeamMemberSeed(String userEmail, String serviceTeamName) {}

  public static final List<ServiceTeamMemberSeed> SERVICE_TEAM_MEMBERS =
      List.of(
          new ServiceTeamMemberSeed("jonas.weber@harbor-demo.test", "Workplace Support"),
          new ServiceTeamMemberSeed("lea.schneider@harbor-demo.test", "Workplace Support"),
          new ServiceTeamMemberSeed("nico.braun@harbor-demo.test", "Workplace Support"),
          new ServiceTeamMemberSeed("amira.klein@harbor-demo.test", "Network Operations"),
          new ServiceTeamMemberSeed("felix.wagner@harbor-demo.test", "Network Operations"),
          new ServiceTeamMemberSeed("sofia.hartmann@harbor-demo.test", "Network Operations"),
          new ServiceTeamMemberSeed("david.becker@harbor-demo.test", "Identity Access"),
          new ServiceTeamMemberSeed("mila.hoffmann@harbor-demo.test", "Identity Access"),
          new ServiceTeamMemberSeed("oskar.krause@harbor-demo.test", "Identity Access"),
          new ServiceTeamMemberSeed("emma.lehmann@harbor-demo.test", "Business Applications"),
          new ServiceTeamMemberSeed("paul.neumann@harbor-demo.test", "Business Applications"),
          new ServiceTeamMemberSeed("clara.schulz@harbor-demo.test", "Business Applications"),
          new ServiceTeamMemberSeed("jonas.weber@harbor-demo.test", "Cloud Platform"),
          new ServiceTeamMemberSeed("amira.klein@harbor-demo.test", "Cloud Platform"),
          new ServiceTeamMemberSeed("paul.neumann@harbor-demo.test", "Cloud Platform"),
          new ServiceTeamMemberSeed("sofia.hartmann@harbor-demo.test", "Security Operations"),
          new ServiceTeamMemberSeed("david.becker@harbor-demo.test", "Security Operations"),
          new ServiceTeamMemberSeed("clara.schulz@harbor-demo.test", "Security Operations"),
          new ServiceTeamMemberSeed("moritz.hahn@harbor-demo.test", "Workplace Support"),
          new ServiceTeamMemberSeed("aylin.yilmaz@harbor-demo.test", "Identity Access"),
          new ServiceTeamMemberSeed("maximilian.fuchs@harbor-demo.test", "Business Applications"),
          new ServiceTeamMemberSeed("selin.arslan@harbor-demo.test", "Network Operations"),
          new ServiceTeamMemberSeed("fabian.lorenz@harbor-demo.test", "Security Operations"),
          new ServiceTeamMemberSeed("zoe.berger@harbor-demo.test", "Cloud Platform"),
          new ServiceTeamMemberSeed("jonathan.keller@harbor-demo.test", "Network Operations"));
}
