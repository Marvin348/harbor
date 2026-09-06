import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createServiceTeam } from "@/features/serviceTeams/api/serviceTeam.ts";
import type { CreateServiceTeamRequest } from "@/api/generated/models/create-service-team-request.ts";
import type { ServiceTeamResponse } from "@/api/generated/models/service-team-response.ts";

export const useCreateServiceTeam = () => {
  const queryClient = useQueryClient();

  return useMutation<ServiceTeamResponse, Error, CreateServiceTeamRequest>({
    mutationFn: createServiceTeam,

    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["service-team"],
      });
    },
  });
};
