import { useQuery } from "@tanstack/react-query";
import { getServiceTeams } from "@/features/serviceTeams/api/serviceTeam.ts";
import type { PageServiceTeamListItemResponse } from "@/api/generated/models/page-service-team-list-item-response.ts";
import { useServiceTeamParams } from "@/features/serviceTeams/hooks/useServiceTeamParams.ts";

export const useGetServiceTeams = () => {
  const { params } = useServiceTeamParams();

  const { data, isLoading, isError } = useQuery<
    PageServiceTeamListItemResponse,
    Error
  >({
    queryFn: () => getServiceTeams(params),
    queryKey: ["service-team", params],
  });

  return {
    serviceTeams: data?.content ?? [],
    totalElements: data?.totalElements ?? 0,
    totalPages: data?.totalPages ?? 1,
    currentPage: (data?.number ?? 0) + 1,
    isLoading,
    isError,
  };
};
