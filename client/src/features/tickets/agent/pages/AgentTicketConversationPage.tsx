import { AgentTicketConversationAside } from "@/features/tickets/agent/components/conversation/AgentTicketConversationAside.tsx";
import { AgentTicketConversationPanel } from "@/features/tickets/agent/components/conversation/AgentTicketConversationPanel.tsx";
import { useGetTicketMessages } from "@/features/tickets/messages/hooks/useGetTicketMessages.ts";
import { Route } from "@/routes/_app.tickets_.$id.tsx";
import { TicketMessagesLoadingState } from "@/features/tickets/messages/components/state/TicketMessagesLoadingState.tsx";
import { TicketMessagesErrorState } from "@/features/tickets/messages/components/state/TicketMessagesErrorState.tsx";

export const AgentTicketConversationPage = () => {
  const { id } = Route.useParams();
  const { ticketMessages, isLoading, isError, refetch } = useGetTicketMessages(
    Number(id),
  );

  if (isLoading) return <TicketMessagesLoadingState />;
  if (isError || !ticketMessages)
    return <TicketMessagesErrorState onRetry={() => void refetch()} />;

  return (
    <div className="grid min-h-0 flex-1 grid-cols-[minmax(0,1fr)_3rem] gap-10 overflow-hidden rounded-lg">
      <AgentTicketConversationPanel ticketMessages={ticketMessages} />
      <AgentTicketConversationAside />
    </div>
  );
};
