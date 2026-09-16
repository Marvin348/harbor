import { Route } from "@/routes/_app.tickets";
import type { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";

export const useRequesterTicketParams = () => {
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

  const setStatus = (status?: TicketResponseStatusEnum) => {
    updateParams({
      status,
      page: 1,
    });
  };

  const setSearch = (search?: string) => {
    updateParams({
      search,
      page: 1,
    });
  };

  return {
    params,
    setPage,
    setStatus,
    setSearch,
  };
};
