import { createFileRoute } from "@tanstack/react-router";
import { ServiceTeamsPage } from "@/features/serviceTeams/pages/ServiceTeamsPage.tsx";

export const Route = createFileRoute("/_app/service-teams")({
  component: ServiceTeamsPage,
});
