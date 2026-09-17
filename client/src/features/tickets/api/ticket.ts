import { apiClient } from "@/lib/apiClient.ts";
import type { TicketResponse } from "@/api/generated/models/ticket-response.ts";
import type { CreateTicketFields } from "@/features/tickets/schema/createTicketSchema.ts";
import type { RequesterTicketParams } from "@/features/tickets/schema/requesterTicketParamsSchema.ts";
import type { PageRequesterTicketItemResponse } from "@/api/generated/models/page-requester-ticket-item-response.ts";
import { PAGE_LIMIT } from "@/shared/constants/pageLimits.ts";

export const createTicket = async (
  data: CreateTicketFields,
): Promise<TicketResponse> => {
  const res = await apiClient.post("/tickets", data);
  return res.data;
};

export const getRequesterTickets = async (
  params: RequesterTicketParams,
): Promise<PageRequesterTicketItemResponse> => {
  const res = await apiClient.get("/tickets", {
    params: {
      page: params.page,
      limit: PAGE_LIMIT.tickets,
      search: params.search,
      status: params.status,
    },
  });
  return res.data;
};
