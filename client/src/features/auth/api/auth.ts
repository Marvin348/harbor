import { apiClient } from "@/lib/apiClient.ts";
import type { RegisterFields } from "@/features/auth/schema/registerSchema.ts";
import type { LoginFields } from "@/features/auth/schema/loginSchema.ts";

export const register = async (data: RegisterFields) => {
  const res = await apiClient.post("auth/register", data);
  return res.data;
};

export const login = async (data: LoginFields) => {
  const res = await apiClient.post("auth/login", data);
  return res.data;
};
