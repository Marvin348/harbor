import { AgentTicketActivitySection } from "@/features/tickets/agent/components/details/AgentTicketActivitySection.tsx";
import { AgentTicketClassificationSection } from "@/features/tickets/agent/components/details/AgentTicketClassificationSection.tsx";
import { AgentTicketConversationSection } from "@/features/tickets/agent/components/details/AgentTicketConversationSection.tsx";
import { AgentTicketCopyIdAction } from "@/features/tickets/agent/components/details/AgentTicketCopyIdAction.tsx";
import { AgentTicketInternalWorkSection } from "@/features/tickets/agent/components/details/AgentTicketInternalWorkSection.tsx";
import { AgentTicketRequesterSection } from "@/features/tickets/agent/components/details/AgentTicketRequesterSection.tsx";
import { AgentTicketRequestSection } from "@/features/tickets/agent/components/details/AgentTicketRequestSection.tsx";
import { AgentTicketSlaSection } from "@/features/tickets/agent/components/details/AgentTicketSlaSection.tsx";
import type { AgentTicketDetailsResponse } from "@/api/generated/models/agent-ticket-details-response.ts";

type AgentTicketWorkspaceProps = {
  agentTicketDetails: AgentTicketDetailsResponse;
};

export const AgentTicketWorkspace = ({
  agentTicketDetails,
}: AgentTicketWorkspaceProps) => {
  return (
    <div className="grid items-start gap-6 xl:grid-cols-[minmax(0,1.65fr)_minmax(300px,0.75fr)]">
      <div className="space-y-6">
        <AgentTicketRequestSection
          description={agentTicketDetails.description}
        />
        <AgentTicketConversationSection
          requesterName={agentTicketDetails.requesterName}
          createdAt={agentTicketDetails.createdAt}
          updatedAt={agentTicketDetails.updatedAt}
        />
        <AgentTicketActivitySection
          createdAt={agentTicketDetails.createdAt}
          updatedAt={agentTicketDetails.updatedAt}
        />
      </div>

      <div className="space-y-6">
        <AgentTicketSlaSection />
        <AgentTicketClassificationSection
          impact={agentTicketDetails.impact}
          urgency={agentTicketDetails.urgency}
          businessCriticality={agentTicketDetails.businessCriticality}
        />
        <AgentTicketRequesterSection
          requesterDetails={{
            requester: {
              name: agentTicketDetails.requesterName,
              email: agentTicketDetails.requesterEmail,
            },
            service: {
              name: agentTicketDetails.serviceName,
              teamName: agentTicketDetails.serviceTeamName,
            },
            assignedAgentName: agentTicketDetails.assignedAgentName,
          }}
        />
        <AgentTicketInternalWorkSection />
        <AgentTicketCopyIdAction ticketId={agentTicketDetails.id} />
      </div>
    </div>
  );
};
