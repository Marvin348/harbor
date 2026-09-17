import { apiClient } from "@/lib/apiClient.ts";
import type { ServiceTeamResponse } from "@/api/generated/models/service-team-response.ts";
import type { CreateServiceTeamFields } from "@/features/serviceTeams/schema/createServiceTeamSchema.ts";
import type { PageServiceTeamListItemResponse } from "@/api/generated/models/page-service-team-list-item-response.ts";
import type { ServiceTeamParams } from "@/features/serviceTeams/schema/serviceTeamParamsSchema.ts";
import { PAGE_LIMIT } from "@/shared/constants/pageLimits.ts";
import type { ServiceTeamOptionResponse } from "@/api/generated/models/service-team-option-response.ts";

export const createServiceTeam = async (
  data: CreateServiceTeamFields,
): Promise<ServiceTeamResponse> => {
  const res = await apiClient.post("service-teams", data);
  return res.data;
};

export const getServiceTeams = async (
  params: ServiceTeamParams,
): Promise<PageServiceTeamListItemResponse> => {
  const res = await apiClient.get("service-teams", {
    params: {
      page: params.page,
      limit: PAGE_LIMIT.serviceTeams,
      search: params.search,
    },
  });
  return res.data;
};

export const getServiceTeamOptions = async (): Promise<
  ServiceTeamOptionResponse[]
> => {
  const res = await apiClient.get("service-teams/options");
  return res.data;
};
