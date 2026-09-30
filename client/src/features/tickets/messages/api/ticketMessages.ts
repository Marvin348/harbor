import { apiClient } from "@/lib/apiClient.ts";
import type { TicketMessageResponse } from "@/api/generated/models/ticket-message-response.ts";
import type { CreateTicketMessageRequest } from "@/api/generated/models/create-ticket-message-request.ts";

export type CreateTicketMessageVariables = {
  ticketId: number;
  request: CreateTicketMessageRequest;
};

export const getTicketMessages = async (
  ticketId: number,
): Promise<TicketMessageResponse[]> => {
  const res = await apiClient.get(`/tickets/${ticketId}/messages`);
  return res.data;
};

export const createTicketMessage = async ({
  ticketId,
  request,
}: CreateTicketMessageVariables): Promise<void> => {
  await apiClient.post(`/tickets/${ticketId}/messages`, request);
};
