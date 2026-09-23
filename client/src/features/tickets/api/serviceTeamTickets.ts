import { apiClient } from "@/lib/apiClient.ts";
import type { serviceTeamTicketParams } from "@/features/serviceTeams/schema/serviceTeamTicketParamsSchema.ts";
import { PAGE_LIMIT } from "@/shared/constants/pageLimits.ts";
import type { PageResponseServiceTeamTicketListItemResponse } from "@/api/generated/models/page-response-service-team-ticket-list-item-response.ts";

export const getServiceTeamTickets = async (
  id: number,
  params: serviceTeamTicketParams,
): Promise<PageResponseServiceTeamTicketListItemResponse> => {
  const res = await apiClient.get(`/service-teams/${id}/tickets`, {
    params: {
      page: params.page,
      limit: PAGE_LIMIT.serviceTeamDetails.tickets,
      search: params.search,
      priority: params.priority,
      status: params.status,
    },
  });
  return res.data;
};
