import { apiClient } from "@/lib/apiClient.ts";
import type { RegisterFields } from "@/features/auth/schema/registerSchema.ts";

export const register = async (data: RegisterFields) => {
  const res = await apiClient.post("/auth/register", data);
  return res.data;
};
