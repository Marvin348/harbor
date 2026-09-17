import { createFileRoute } from "@tanstack/react-router";
import { TicketDetails } from "@/features/tickets/pages/TicketDetails.tsx";

export const Route = createFileRoute("/_app/tickets_/$id")({
  component: TicketDetails,
});
