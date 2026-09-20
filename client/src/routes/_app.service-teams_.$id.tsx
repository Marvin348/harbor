import { createFileRoute } from "@tanstack/react-router";
import { ServiceTeamDetailsLayout } from "@/features/serviceTeams/pages/ServiceTeamDetailsLayout.tsx";

export const Route = createFileRoute("/_app/service-teams_/$id")({
  component: ServiceTeamDetailsLayout,
});
