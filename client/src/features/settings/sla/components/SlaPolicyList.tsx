import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";
import { SlaPolicyItem } from "@/features/settings/sla/components/SlaPolicyItem.tsx";

type SlaPolicyListProps = {
  policies: SlaPolicyResponse[];
};

export const SlaPolicyList = ({ policies }: SlaPolicyListProps) => {
  return (
    <div className="divide-y divide-border">
      {policies.map((policy) => (
        <SlaPolicyItem key={policy.id} policy={policy} />
      ))}
    </div>
  );
};
