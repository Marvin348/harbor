import { createFileRoute } from "@tanstack/react-router";
import { AgentTicketConversationPage } from "@/features/tickets/agent/pages/AgentTicketConversationPage.tsx";

export const Route = createFileRoute("/_app/tickets_/$id/")({
  component: AgentTicketConversationPage,
});
