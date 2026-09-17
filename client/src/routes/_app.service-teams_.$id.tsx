import { createFileRoute } from "@tanstack/react-router";
import { ServiceTeamDetailsPage } from "@/features/serviceTeams/pages/ServiceTeamDetailsPage.tsx";

export const Route = createFileRoute("/_app/service-teams_/$id")({
  component: ServiceTeamDetailsPage,
});
