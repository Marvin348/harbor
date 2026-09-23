import { useQuery } from "@tanstack/react-query";
import { getRequesterTickets } from "@/features/tickets/api/ticket.ts";
import type { PageResponseRequesterTicketItemResponse } from "@/api/generated/models/page-response-requester-ticket-item-response.ts";
import { useRequesterTicketParams } from "@/features/tickets/hooks/useRequesterTicketParams.ts";

export const useGetRequesterTickets = () => {
  const { params } = useRequesterTicketParams();

  const { data, isLoading, isError } = useQuery<
    PageResponseRequesterTicketItemResponse,
    Error
  >({
    queryFn: () => getRequesterTickets(params),
    queryKey: ["tickets", params],
  });

  return { requesterTickets: data, isLoading, isError };
};
