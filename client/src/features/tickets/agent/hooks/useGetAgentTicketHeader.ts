import { useQuery } from "@tanstack/react-query";
import type { AgentTicketHeaderResponse } from "@/api/generated/models/agent-ticket-header-response.ts";
import { getAgentTicketHeader } from "@/features/tickets/agent/api/agentTickets.ts";

export const useGetAgentTicketHeader = (ticketId: number) => {
  const { data, isLoading, isError, refetch } = useQuery<
    AgentTicketHeaderResponse,
    Error
  >({
    queryFn: () => getAgentTicketHeader(ticketId),
    queryKey: ["tickets", ticketId, "header"],
    enabled: Number.isInteger(ticketId) && ticketId > 0,
  });

  return { agentTicketHeader: data, isLoading, isError, refetch };
};
