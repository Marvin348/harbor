import { useMutation } from "@tanstack/react-query";
import { login } from "@/features/auth/api/auth.ts";

export const useLogin = () => {
  return useMutation({
    mutationFn: login,
  });
};
