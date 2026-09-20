import { createFileRoute } from "@tanstack/react-router";
import { ServiceTeamOverviewPage } from "@/features/serviceTeams/pages/ServiceTeamOverviewPage.tsx";

export const Route = createFileRoute("/_app/service-teams_/$id/")({
  component: ServiceTeamOverviewPage,
});
