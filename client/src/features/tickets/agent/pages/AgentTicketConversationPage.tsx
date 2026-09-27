import { AgentTicketConversationAside } from "@/features/tickets/agent/components/conversation/AgentTicketConversationAside.tsx";
import { AgentTicketConversationPanel } from "@/features/tickets/agent/components/conversation/AgentTicketConversationPanel.tsx";

export const AgentTicketConversationPage = () => {
  return (
    <div className="grid gap-20 grid-cols-[minmax(0,1fr)_3rem] overflow-hidden rounded-lg">
      <AgentTicketConversationPanel />
      <AgentTicketConversationAside />
    </div>
  );
};
