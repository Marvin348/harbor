import { createFileRoute } from "@tanstack/react-router";
import { ServiceTeamDetailsTicketsPage } from "@/features/serviceTeams/pages/ServiceTeamDetailsTicketsPage.tsx";
import { serviceTeamTicketParamsSchema } from "@/features/serviceTeams/schema/serviceTeamTicketParamsSchema.ts";

export const Route = createFileRoute("/_app/service-teams_/$id/tickets")({
  validateSearch: serviceTeamTicketParamsSchema,
  component: ServiceTeamDetailsTicketsPage,
});
