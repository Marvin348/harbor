import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createTicketMessage,
  type CreateTicketMessageVariables,
} from "@/features/tickets/messages/api/ticketMessages.ts";

export const useCreateTicketMessage = () => {
  const queryClient = useQueryClient();

  return useMutation<void, Error, CreateTicketMessageVariables>({
    mutationFn: createTicketMessage,

    onSuccess: (_, { ticketId }) => {
      void queryClient.invalidateQueries({
        queryKey: ["tickets", ticketId, "messages"],
      });
    },
  });
};
