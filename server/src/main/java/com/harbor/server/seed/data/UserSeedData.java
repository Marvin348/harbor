package com.harbor.server.seed.data;

import com.harbor.server.features.user.model.OrganizationRole;

import java.util.List;

public class UserSeedData {

  public record UserSeed(
      String firstName, String lastName, String email, String password, OrganizationRole role) {}

  public static final List<UserSeed> USERS =
      List.of(
          new UserSeed(
              "Mara",
              "Fischer",
              "mara.fischer@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.ORGANIZATION_ADMIN),
          new UserSeed(
              "Jonas",
              "Weber",
              "jonas.weber@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Lea",
              "Schneider",
              "lea.schneider@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Nico",
              "Braun",
              "nico.braun@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Amira",
              "Klein",
              "amira.klein@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Felix",
              "Wagner",
              "felix.wagner@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Sofia",
              "Hartmann",
              "sofia.hartmann@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "David",
              "Becker",
              "david.becker@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Mila",
              "Hoffmann",
              "mila.hoffmann@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Oskar",
              "Krause",
              "oskar.krause@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Emma",
              "Lehmann",
              "emma.lehmann@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Paul",
              "Neumann",
              "paul.neumann@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Clara",
              "Schulz",
              "clara.schulz@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Lukas",
              "Bauer",
              "lukas.bauer@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Hannah",
              "Richter",
              "hannah.richter@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Ben",
              "Wolf",
              "ben.wolf@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Lina",
              "Schmitt",
              "lina.schmitt@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Noah",
              "Zimmermann",
              "noah.zimmermann@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Marie",
              "Meyer",
              "marie.meyer@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Elias",
              "Koch",
              "elias.koch@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Laura",
              "Krueger",
              "laura.krueger@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Tim",
              "Lang",
              "tim.lang@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Sarah",
              "Peters",
              "sarah.peters@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Jan",
              "Vogel",
              "jan.vogel@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Nina",
              "Sommer",
              "nina.sommer@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Julia",
              "Otto",
              "julia.otto@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Moritz",
              "Hahn",
              "moritz.hahn@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Aylin",
              "Yilmaz",
              "aylin.yilmaz@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Maximilian",
              "Fuchs",
              "maximilian.fuchs@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Selin",
              "Arslan",
              "selin.arslan@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Fabian",
              "Lorenz",
              "fabian.lorenz@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Zoe",
              "Berger",
              "zoe.berger@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Jonathan",
              "Keller",
              "jonathan.keller@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.AGENT),
          new UserSeed(
              "Melina",
              "Franke",
              "melina.franke@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER),
          new UserSeed(
              "Daniel",
              "Scholz",
              "daniel.scholz@harbor-demo.test",
              "HarborDemo123!",
              OrganizationRole.REQUESTER));
}
