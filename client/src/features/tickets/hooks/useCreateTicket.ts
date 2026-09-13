import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createTicket } from "@/features/tickets/api/ticket.ts";

export const useCreateTicket = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createTicket,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["tickets"],
      });
    },
  });
};
