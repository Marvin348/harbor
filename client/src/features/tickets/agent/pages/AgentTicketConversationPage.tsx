import { AgentTicketConversationAside } from "@/features/tickets/agent/components/conversation/AgentTicketConversationAside.tsx";
import { AgentTicketConversationPanel } from "@/features/tickets/agent/components/conversation/AgentTicketConversationPanel.tsx";

export const AgentTicketConversationPage = () => {
  return (
    <div className="grid grid-cols-[minmax(0,1fr)_3rem] gap-10 justify-between overflow-hidden rounded-lg">
      <AgentTicketConversationPanel />
      <AgentTicketConversationAside />
    </div>
  );
};
