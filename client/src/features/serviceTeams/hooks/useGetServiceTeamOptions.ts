import type { ServiceTeamOptionResponse } from "@/api/generated/models/service-team-option-response.ts";
import { useQuery } from "@tanstack/react-query";
import { getServiceTeamOptions } from "@/features/serviceTeams/api/serviceTeam.ts";

export const useGetServiceTeamOptions = () => {
  const { data, isLoading, isError } = useQuery<
    ServiceTeamOptionResponse[],
    Error
  >({
    queryFn: getServiceTeamOptions,
    queryKey: ["service-team-options"],
  });

  return { serviceTeamOptions: data, isLoading, isError };
};
