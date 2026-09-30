import { Outlet } from "@tanstack/react-router";
import { AgentTicketHeader } from "@/features/tickets/agent/components/AgentTicketHeader.tsx";
import { Route } from "@/routes/_app.tickets_.$id.tsx";
import { useGetAgentTicketHeader } from "@/features/tickets/agent/hooks/useGetAgentTicketHeader.ts";
import { AgentTicketDetailsLoadingState } from "@/features/tickets/agent/components/state/AgentTicketDetailsLoadingState.tsx";
import { AgentTicketDetailsErrorState } from "@/features/tickets/agent/components/state/AgentTicketDetailsErrorState.tsx";

export const AgentTicketLayout = () => {
  const { id } = Route.useParams();
  const { agentTicketHeader, isLoading, isError, refetch } =
    useGetAgentTicketHeader(Number(id));

  if (isLoading) return <AgentTicketDetailsLoadingState />;
  if (isError || !agentTicketHeader)
    return <AgentTicketDetailsErrorState onRetry={() => void refetch()} />;

  return (
    <div className="flex min-h-0 flex-1 flex-col">
      <AgentTicketHeader agentTicketHeader={agentTicketHeader} />
      <div className="mt-6 flex min-h-0 flex-1 flex-col">
        <Outlet />
      </div>
    </div>
  );
};
