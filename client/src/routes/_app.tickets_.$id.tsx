import { createFileRoute } from "@tanstack/react-router";
import { TicketDetailsLayout } from "@/features/tickets/pages/TicketDetailsLayout.tsx";

export const Route = createFileRoute("/_app/tickets_/$id")({
  component: TicketDetailsLayout,
});
