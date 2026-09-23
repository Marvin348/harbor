import { useQuery, keepPreviousData } from "@tanstack/react-query";
import { getServiceTeamTickets } from "@/features/tickets/api/serviceTeamTickets.ts";
import { useServiceTeamTicketParams } from "@/features/serviceTeams/hooks/useServcieTeamTicketParams.ts";
import type { PageResponseServiceTeamTicketListItemResponse } from "@/api/generated/models/page-response-service-team-ticket-list-item-response.ts";

export const useGetServiceTeamTickets = (id: number) => {
  const { params } = useServiceTeamTicketParams();

  const { data, isLoading, error } = useQuery<
    PageResponseServiceTeamTicketListItemResponse,
    Error
  >({
    queryFn: () => getServiceTeamTickets(id, params),
    queryKey: ["tickets", id, "tickets", params],
    placeholderData: keepPreviousData,
  });

  return {
    data,
    isLoading,
    error,
  };
};
