import { apiClient } from "@/lib/apiClient.ts";
import type { TicketMessageResponse } from "@/api/generated/models/ticket-message-response.ts";

export const getTicketMessages = async (
  ticketId: number,
): Promise<TicketMessageResponse[]> => {
  const res = await apiClient.get(`/tickets/${ticketId}/messages`);
  return res.data;
};
