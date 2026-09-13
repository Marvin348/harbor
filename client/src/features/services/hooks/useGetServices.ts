import { useQuery } from "@tanstack/react-query";
import type { ServiceResponse } from "@/api/generated/models/service-response.ts";
import { getServices } from "@/features/services/api/service.ts";

export const useGetServices = () => {
  const { data, isLoading, isError } = useQuery<ServiceResponse[], Error>({
    queryFn: getServices,
    queryKey: ["services"],
  });

  return { services: data, isLoading, isError };
};
