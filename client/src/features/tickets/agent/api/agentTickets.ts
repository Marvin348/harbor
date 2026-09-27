import { apiClient } from "@/lib/apiClient.ts";
import type { AgentTicketDetailsResponse } from "@/api/generated/models/agent-ticket-details-response.ts";

export const getAgentTicketDetails = async (
  ticketId: number,
): Promise<AgentTicketDetailsResponse> => {
  const res = await apiClient.get(`/tickets/${ticketId}/agent`);
  return res.data;
};

export const claimTicket = async (ticketId: number) => {
  const res = await apiClient.patch(`/tickets/${ticketId}/claim`);
  return res.data;
};
