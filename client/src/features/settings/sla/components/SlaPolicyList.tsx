import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";
import { SlaPolicyItem } from "@/features/settings/sla/components/SlaPolicyItem.tsx";

type SlaPolicyListProps = {
  policies: SlaPolicyResponse[];
  onEditPolicy: (policy: SlaPolicyResponse) => void;
};

export const SlaPolicyList = ({
  policies,
  onEditPolicy,
}: SlaPolicyListProps) => {
  return (
    <div className="divide-y divide-border">
      {policies.map((policy) => (
        <SlaPolicyItem key={policy.id} policy={policy} onEdit={onEditPolicy} />
      ))}
    </div>
  );
};
