import { AgentTicketWorkflowBar } from "@/features/tickets/agent/components/details/AgentTicketWorkflowBar.tsx";
import { AgentTicketWorkspace } from "@/features/tickets/agent/components/details/AgentTicketWorkspace.tsx";
import { useGetAgentTicketDetails } from "@/features/tickets/agent/hooks/useGetAgentTicketDetails.ts";
import { AgentTicketDetailsErrorState } from "@/features/tickets/agent/components/state/AgentTicketDetailsErrorState.tsx";
import { AgentTicketDetailsLoadingState } from "@/features/tickets/agent/components/state/AgentTicketDetailsLoadingState.tsx";
import { Route } from "@/routes/_app.tickets_.$id.tsx";

export const AgentTicketDetailsPage = () => {
  const { id } = Route.useParams();
  const { agentTicketDetails, isLoading, isError, refetch } =
    useGetAgentTicketDetails(Number(id));

  if (isLoading) return <AgentTicketDetailsLoadingState />;
  if (isError || !agentTicketDetails)
    return <AgentTicketDetailsErrorState onRetry={() => void refetch()} />;

  return (
    <div className="space-y-6">
      <AgentTicketWorkflowBar
        workflowDetails={{
          status: agentTicketDetails.status,
          priority: agentTicketDetails.priority,
          serviceTeamName: agentTicketDetails.serviceTeamName,
          assignedAgentName: agentTicketDetails.assignedAgentName,
        }}
      />

      <AgentTicketWorkspace agentTicketDetails={agentTicketDetails} />
    </div>
  );
};
