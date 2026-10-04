import { useState } from "react";
import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";
import { SlaPolicyOverview } from "@/features/settings/sla/components/SlaPolicyOverview.tsx";
import { SlaPolicySettingsHeader } from "@/features/settings/sla/components/SlaPolicySettingsHeader.tsx";
import { UpdateSlaPolicyDialog } from "@/features/settings/sla/components/UpdateSlaPolicyDialog.tsx";

export const SlaPolicySettingsPage = () => {
  const [selectedPolicy, setSelectedPolicy] =
    useState<SlaPolicyResponse | null>(null);

  return (
    <div className="space-y-6">
      <SlaPolicySettingsHeader />

      <SlaPolicyOverview onEditPolicy={setSelectedPolicy} />

      {selectedPolicy && (
        <UpdateSlaPolicyDialog
          policy={selectedPolicy}
          open
          onOpenChange={(open) => {
            if (!open) {
              setSelectedPolicy(null);
            }
          }}
        />
      )}
    </div>
  );
};
