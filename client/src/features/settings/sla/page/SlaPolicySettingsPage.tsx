import { useState } from "react";
import { CreateSlaPolicyDialog } from "@/features/settings/sla/components/CreateSlaPolicyDialog.tsx";
import { SlaPolicyOverview } from "@/features/settings/sla/components/SlaPolicyOverview.tsx";
import { SlaPolicySettingsHeader } from "@/features/settings/sla/components/SlaPolicySettingsHeader.tsx";

export const SlaPolicySettingsPage = () => {
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);

  return (
    <div className="space-y-6">
      <SlaPolicySettingsHeader
        onCreatePolicy={() => setIsCreateDialogOpen(true)}
      />

      <SlaPolicyOverview
        onCreatePolicy={() => setIsCreateDialogOpen(true)}
      />

      <CreateSlaPolicyDialog
        open={isCreateDialogOpen}
        onOpenChange={setIsCreateDialogOpen}
      />
    </div>
  );
};
