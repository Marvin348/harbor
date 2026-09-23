import { Route } from "@/routes/_app.service-teams_.$id.tickets";
import type { TicketResponsePriorityEnum } from "@/api/generated/models/ticket-response.ts";
import { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";

export const useServiceTeamTicketParams = () => {
  const params = Route.useSearch();
  const navigate = Route.useNavigate();

  const updateParams = (updates: Partial<typeof params>) => {
    navigate({
      search: (prev) => ({
        ...prev,
        ...updates,
      }),
    });
  };

  const setPage = (page: number) => {
    updateParams({ page });
  };

  const setSearch = (search?: string) => {
    updateParams({ search, page: 1 });
  };

  const setPriority = (priority?: TicketResponsePriorityEnum) => {
    updateParams({ priority, page: 1 });
  };

  const setStatus = (status?: TicketResponseStatusEnum) => {
    updateParams({ status, page: 1 });
  };

  return {
    params,
    setPage,
    setSearch,
    setPriority,
    setStatus,
  };
};
