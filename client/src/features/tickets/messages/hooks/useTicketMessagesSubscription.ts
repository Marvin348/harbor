import { useEffect } from "react";
import { subscribe } from "@/lib/websocket/stompClient.ts";
import { useQueryClient } from "@tanstack/react-query";

export const useTicketMessagesSubscription = (ticketId: number) => {
  const queryClient = useQueryClient();

  useEffect(() => {
    return subscribe(`/topic/tickets.${ticketId}`, () => {
      queryClient.invalidateQueries({
        queryKey: ["tickets", ticketId, "messages"],
      });
    });
  }, [ticketId, queryClient]);
};
