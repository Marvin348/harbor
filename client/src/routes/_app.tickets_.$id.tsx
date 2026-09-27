import { createFileRoute } from "@tanstack/react-router";
import { TicketDetailsRoleGate } from "@/features/tickets/pages/TicketDetailsRoleGate.tsx";

export const Route = createFileRoute("/_app/tickets_/$id")({
  component: TicketDetailsRoleGate,
});
