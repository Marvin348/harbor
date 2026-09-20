import { useQuery } from "@tanstack/react-query";
import type { ServiceTeamDetailsResponse } from "@/api/generated/models/service-team-details-response.ts";
import { getServiceTeamDetails } from "@/features/serviceTeams/api/serviceTeam.ts";

export const useGetServiceTeamDetails = (id: number) => {
  const { data, isLoading, isError, refetch } = useQuery<
    ServiceTeamDetailsResponse,
    Error
  >({
    queryFn: () => getServiceTeamDetails(id),
    queryKey: ["service-team", id],
  });

  return { serviceTeamDetails: data, isLoading, isError, refetch };
};
