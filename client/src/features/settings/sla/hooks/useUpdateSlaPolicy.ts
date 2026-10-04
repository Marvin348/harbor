import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateSlaPolicy } from "@/features/settings/sla/api/slaPolicy.ts";
import type { UpdateSlaPolicyFields } from "@/features/settings/sla/schema/updateSlaPolicySchema.ts";

type UpdateSlaPolicyVariables = {
  id: number;
  data: UpdateSlaPolicyFields;
};

export const useUpdateSlaPolicy = () => {
  const queryClient = useQueryClient();

  return useMutation<void, Error, UpdateSlaPolicyVariables>({
    mutationFn: ({ id, data }) => updateSlaPolicy(id, data),
    onSuccess: () => {
      void queryClient.invalidateQueries({ queryKey: ["sla-policies"] });
    },
  });
};
