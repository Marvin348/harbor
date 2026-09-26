import type { AgentTicketDetailsResponse } from "@/api/generated/models/agent-ticket-details-response.ts";
import { TICKET_STATUS_LABELS } from "@/features/tickets/constants/ticketStatusLabels.ts";
import { TICKET_PRIORITY_LABELS } from "@/features/tickets/constants/ticketPriorityLabels.ts";

export type AgentTicketWorkflowDetails = {
  status: AgentTicketDetailsResponse["status"];
  priority: AgentTicketDetailsResponse["priority"];
  serviceTeamName: string;
  assignedAgentName?: string;
};

export const mapAgentTicketWorkflowFields = (
  details: AgentTicketWorkflowDetails,
) => [
  { label: "Status", value: TICKET_STATUS_LABELS[details.status] },
  {
    label: "Zugewiesener Agent",
    value: details.assignedAgentName ?? "Nicht zugewiesen",
  },
  { label: "Service-Team", value: details.serviceTeamName },
  { label: "Priorität", value: TICKET_PRIORITY_LABELS[details.priority] },
];
