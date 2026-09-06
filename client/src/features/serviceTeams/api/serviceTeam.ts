import { apiClient } from "@/lib/apiClient.ts";
import type { ServiceTeamResponse } from "@/api/generated/models/service-team-response.ts";
import type { CreateServiceTeamFields } from "@/features/serviceTeams/schema/createServiceTeamSchema.ts";

export const createServiceTeam = async (
  data: CreateServiceTeamFields,
): Promise<ServiceTeamResponse> => {
  const res = await apiClient.post("service-teams", data);
  return res.data;
};

export const getServiceTeams = async (): Promise<ServiceTeamResponse[]> => {
  const res = await apiClient.get("service-teams");
  return res.data;
};
