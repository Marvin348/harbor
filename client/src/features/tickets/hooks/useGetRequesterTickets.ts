import { useQuery } from "@tanstack/react-query";
import { getRequesterTickets } from "@/features/tickets/api/ticket.ts";
import type { PageRequesterTicketItemResponse } from "@/api/generated/models/page-requester-ticket-item-response.ts";
import { useRequesterTicketParams } from "@/features/tickets/hooks/useRequesterTicketParams.ts";

export const useGetRequesterTickets = () => {
  const { params } = useRequesterTicketParams();

  const { data, isLoading, isError } = useQuery<
    PageRequesterTicketItemResponse,
    Error
  >({
    queryFn: () => getRequesterTickets(params),
    queryKey: ["tickets", params],
  });

  return { requesterTickets: data, isLoading, isError };
};
