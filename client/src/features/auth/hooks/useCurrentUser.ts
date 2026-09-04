import { useQuery } from "@tanstack/react-query";
import { getCurrentUser } from "@/features/auth/api/auth.ts";

export const useCurrentUser = () => {
  const { data, isLoading, error } = useQuery({
    queryKey: ["auth", "me"],
    queryFn: getCurrentUser,
  });

  return { user: data, isLoading, error };
};
