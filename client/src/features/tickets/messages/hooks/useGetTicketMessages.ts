import { useQuery } from "@tanstack/react-query";
import { getTicketMessages } from "@/features/tickets/messages/api/ticketMessages.ts";
import type { TicketMessageResponse } from "@/api/generated/models/ticket-message-response.ts";

export const useGetTicketMessages = (ticketId: number) => {
  const { data, isLoading, error } = useQuery<TicketMessageResponse[], Error>({
    queryFn: () => getTicketMessages(ticketId),
    queryKey: ["tickets", ticketId, "messages"],
  });

  return { ticketMessages: data, isLoading, error };
};
