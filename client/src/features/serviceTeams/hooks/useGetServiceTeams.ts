import { useQuery } from "@tanstack/react-query";
import { getServiceTeams } from "@/features/serviceTeams/api/serviceTeam.ts";
import type { ServiceTeamResponse } from "@/api/generated/models/service-team-response.ts";

export const useGetServiceTeams = () => {
  const { data, isLoading, isError } = useQuery<ServiceTeamResponse[], Error>({
    queryFn: getServiceTeams,
    queryKey: ["service-team"],
  });

  return { serviceTeams: data, isLoading, isError };
};
