import {
  AgentTicketHeader,
  AgentTicketWorkflowBar,
  AgentTicketWorkspace,
} from "@/features/tickets/agent/component";
import { useGetAgentTicketDetails } from "@/features/tickets/agent/hooks/useGetAgentTicketDetails.ts";
import { AgentTicketDetailsErrorState } from "@/features/tickets/agent/component/state/AgentTicketDetailsErrorState.tsx";
import { AgentTicketDetailsLoadingState } from "@/features/tickets/agent/component/state/AgentTicketDetailsLoadingState.tsx";
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
      <AgentTicketHeader agentTicketDetails={agentTicketDetails} />

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
