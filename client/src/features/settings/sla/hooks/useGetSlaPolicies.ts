import { useQuery } from "@tanstack/react-query";
import { getSlaPolicies } from "@/features/settings/sla/api/slaPolicy.ts";
import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";

export const useGetSlaPolicies = () => {
  const { data, isLoading, isError, refetch } = useQuery<
    SlaPolicyResponse[],
    Error
  >({
    queryKey: ["sla-policies"],
    queryFn: getSlaPolicies,
  });

  return { slaPolicies: data, isLoading, isError, refetch };
};
