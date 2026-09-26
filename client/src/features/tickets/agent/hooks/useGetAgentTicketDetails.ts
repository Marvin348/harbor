import { useQuery } from "@tanstack/react-query";
import { getAgentTicketDetails } from "@/features/tickets/agent/api/agentTickets.ts";
import type { AgentTicketDetailsResponse } from "@/api/generated/models/agent-ticket-details-response.ts";

export const useGetAgentTicketDetails = (ticketId: number) => {
  const { data, isLoading, isError, refetch } = useQuery<
    AgentTicketDetailsResponse,
    Error
  >({
    queryFn: () => getAgentTicketDetails(ticketId),
    queryKey: ["tickets", ticketId, "agent"],
  });

  return { agentTicketDetails: data, isLoading, isError, refetch };
};
