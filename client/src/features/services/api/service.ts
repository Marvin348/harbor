import { apiClient } from "@/lib/apiClient.ts";
import type { ServiceResponse } from "@/api/generated/models/service-response.ts";
import type { CreateServiceFields } from "@/features/services/schema/createServiceSchema.ts";

export const createService = async (
  data: CreateServiceFields,
): Promise<ServiceResponse> => {
  const res = await apiClient.post("/services", data);
  return res.data;
};

export const getServices = async (): Promise<ServiceResponse[]> => {
  const res = await apiClient.get("/services");
  return res.data;
};
