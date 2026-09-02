import { apiClient } from "@/lib/apiClient.ts";
import type { RegisterFields } from "@/features/auth/schema/registerSchema.ts";
import type { LoginFields } from "@/features/auth/schema/loginSchema.ts";
import type { RegisterResponse } from "@/api/generated/models/register-response.ts";
import type { LoginResponse } from "@/api/generated/models/login-response.ts";

export const register = async (
  data: RegisterFields,
): Promise<RegisterResponse> => {
  const res = await apiClient.post("auth/register", data);
  return res.data;
};

export const login = async (data: LoginFields): Promise<LoginResponse> => {
  const res = await apiClient.post("auth/login", data);
  return res.data;
};
