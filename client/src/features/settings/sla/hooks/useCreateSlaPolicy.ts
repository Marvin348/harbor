import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createSlaPolicy } from "@/features/settings/sla/api/slaPolicy.ts";
import type { CreateSlaPolicyFields } from "@/features/settings/sla/schema/createSlaPolicySchema.ts";

export const useCreateSlaPolicy = () => {
  const queryClient = useQueryClient();

  return useMutation<void, Error, CreateSlaPolicyFields>({
    mutationFn: createSlaPolicy,
    onSuccess: () => {
      void queryClient.invalidateQueries({ queryKey: ["sla-policies"] });
    },
  });
};
