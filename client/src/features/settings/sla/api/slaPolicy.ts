import { apiClient } from "@/lib/apiClient.ts";
import type { UpdateSlaPolicyFields } from "@/features/settings/sla/schema/updateSlaPolicySchema.ts";
import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";

export const getSlaPolicies = async (): Promise<SlaPolicyResponse[]> => {
  const res = await apiClient.get("/sla-policies");
  return res.data;
};

export const updateSlaPolicy = async (
  id: number,
  data: UpdateSlaPolicyFields,
): Promise<void> => {
  await apiClient.patch(`/sla-policies/${id}`, data);
};
