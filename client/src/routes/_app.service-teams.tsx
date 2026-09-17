import { createFileRoute } from "@tanstack/react-router";
import { ServiceTeamsPage } from "@/features/serviceTeams/pages/ServiceTeamsPage.tsx";
import { serviceTeamParamsSchema } from "@/features/serviceTeams/schema/serviceTeamParamsSchema.ts";

export const Route = createFileRoute("/_app/service-teams")({
  validateSearch: serviceTeamParamsSchema,
  component: ServiceTeamsPage,
});
