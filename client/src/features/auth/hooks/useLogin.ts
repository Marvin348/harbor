import { useMutation, useQueryClient } from "@tanstack/react-query";
import { login } from "@/features/auth/api/auth.ts";

export const useLogin = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: login,

    onSuccess: (user) => {
      queryClient.setQueryData(["auth", "me"], user);
    },
  });
};
