import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createService } from "@/features/services/api/service.ts";
import type { ServiceResponse } from "@/api/generated/models/service-response.ts";
import type { CreateServiceFields } from "@/features/services/schema/createServiceSchema.ts";

export const useCreateService = () => {
  const queryClient = useQueryClient();

  return useMutation<ServiceResponse, Error, CreateServiceFields>({
    mutationFn: createService,

    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["services"],
      });
    },
  });
};
