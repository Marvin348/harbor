import { createFileRoute } from "@tanstack/react-router";
import { TicketsPage } from "@/features/tickets/pages/TicketsPage.tsx";
import { requesterTicketParamsSchema } from "@/features/tickets/schema/requesterTicketParamsSchema.ts";

export const Route = createFileRoute("/_app/tickets")({
  validateSearch: requesterTicketParamsSchema,
  component: TicketsPage,
});
