import { AgentTicketMessageComposer } from "@/features/tickets/agent/components/conversation/AgentTicketMessageComposer.tsx";
import { AgentTicketMessageList } from "@/features/tickets/agent/components/conversation/AgentTicketMessageList.tsx";

export const AgentTicketConversationPanel = () => {
  return (
    <section className="flex min-w-0 flex-col">
      <AgentTicketMessageList />
      <AgentTicketMessageComposer />
    </section>
  );
};
