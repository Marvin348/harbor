import { useSearchParams } from "react-router-dom";
import { requesterTicketParamsSchema } from "@/features/tickets/schema/requesterTicketParamsSchema.ts";
import type { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";

export const useRequesterTicketParams = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const params = requesterTicketParamsSchema.parse(
    Object.fromEntries(searchParams.entries()),
  );

  const updateParams = (updates: Partial<typeof params>) => {
    const updated = {
      ...params,
      ...updates,
    };

    const newSearchParams = new URLSearchParams();

    if (updated.page !== 1) {
      newSearchParams.set("page", String(updated.page));
    }

    if (updated.status) {
      newSearchParams.set("status", updated.status);
    }

    if (updated.search) {
      newSearchParams.set("search", updated.search);
    }

    setSearchParams(newSearchParams);
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
