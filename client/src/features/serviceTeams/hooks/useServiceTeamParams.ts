import { Route } from "@/routes/_app.service-teams";

export const useServiceTeamParams = () => {
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

  return {
    params,
    setPage,
    setSearch,
  };
};
