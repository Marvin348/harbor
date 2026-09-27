import { createFileRoute } from "@tanstack/react-router";
import { AgentTicketDetailsPage } from "@/features/tickets/agent/pages/AgentTicketDetailsPage.tsx";

export const Route = createFileRoute("/_app/tickets_/$id/details")({
  component: AgentTicketDetailsPage,
});
