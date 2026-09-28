import { AgentTicketMessageComposer } from "@/features/tickets/agent/components/conversation/AgentTicketMessageComposer.tsx";
import { AgentTicketMessageList } from "@/features/tickets/agent/components/conversation/AgentTicketMessageList.tsx";
import { useGetTicketMessages } from "@/features/tickets/messages/hooks/useGetTicketMessages.ts";
import { Route } from "@/routes/_app.tickets_.$id.tsx";

export const AgentTicketConversationPanel = () => {
  const { id } = Route.useParams();
  const {
    ticketMessages = [],
    isLoading,
    error,
  } = useGetTicketMessages(Number(id));

  if (isLoading) return <div>loading</div>;
  if (error) return <div>error</div>;

  return (
    <section className="flex min-w-0 flex-col">
      <AgentTicketMessageList ticketMessages={ticketMessages} />
      <AgentTicketMessageComposer />
    </section>
  );
};
