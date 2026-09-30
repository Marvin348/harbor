import { TicketMessageComposer } from "@/features/tickets/messages/components/TicketMessageComposer.tsx";
import { TicketMessageList } from "@/features/tickets/messages/components/TicketMessageList.tsx";
import { Route } from "@/routes/_app.tickets_.$id.tsx";
import type { TicketMessageResponse } from "@/api/generated/models/ticket-message-response.ts";

type AgentTicketConversationPanelProps = {
  ticketMessages: TicketMessageResponse[];
};

export const AgentTicketConversationPanel = ({
  ticketMessages,
}: AgentTicketConversationPanelProps) => {
  const { id } = Route.useParams();

  return (
    <section className="flex min-h-0 min-w-0 flex-col">
      <TicketMessageList ticketMessages={ticketMessages} />
      <TicketMessageComposer ticketId={Number(id)} />
    </section>
  );
};
