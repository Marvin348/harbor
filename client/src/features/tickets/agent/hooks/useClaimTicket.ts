import { useMutation, useQueryClient } from "@tanstack/react-query";
import { claimTicket } from "@/features/tickets/agent/api/agentTickets.ts";

export const useClaimTicket = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: claimTicket,

    onSuccess: (_, ticketId) => {
      queryClient.invalidateQueries({
        queryKey: ["tickets", ticketId, "agent"],
      });
    },
  });
};
