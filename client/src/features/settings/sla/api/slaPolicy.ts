import { apiClient } from "@/lib/apiClient.ts";
import type { CreateSlaPolicyFields } from "@/features/settings/sla/schema/createSlaPolicySchema.ts";
import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";

export const getSlaPolicies = async (): Promise<SlaPolicyResponse[]> => {
  const res = await apiClient.get("/sla-policies");
  return res.data;
};

export const createSlaPolicy = async (
  data: CreateSlaPolicyFields,
): Promise<void> => {
  await apiClient.post("/sla-policies", data);
};
