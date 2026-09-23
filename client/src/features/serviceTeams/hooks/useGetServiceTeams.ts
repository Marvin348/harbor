import { useQuery } from "@tanstack/react-query";
import { getServiceTeams } from "@/features/serviceTeams/api/serviceTeam.ts";
import type { PageResponseServiceTeamListItemResponse } from "@/api/generated/models/page-response-service-team-list-item-response";
import { useServiceTeamParams } from "@/features/serviceTeams/hooks/useServiceTeamParams.ts";

export const useGetServiceTeams = () => {
  const { params } = useServiceTeamParams();

  const { data, isLoading, isError } = useQuery<
    PageResponseServiceTeamListItemResponse,
    Error
  >({
    queryFn: () => getServiceTeams(params),
    queryKey: ["service-team", params],
  });

  return {
    serviceTeams: data?.content,
    totalElements: data?.totalElements,
    totalPages: data?.totalPages,
    currentPage: data ? data.number + 1 : 1,
    isLoading,
    isError,
  };
};
