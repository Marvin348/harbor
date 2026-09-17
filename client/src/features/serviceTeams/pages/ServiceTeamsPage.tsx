import { useState } from "react";
import { Sheet } from "@/components/ui/sheet.tsx";
import { ServiceTeamHeader } from "@/features/serviceTeams/components/ServiceTeamHeader.tsx";
import { CreateServiceTeamPanel } from "@/features/serviceTeams/components/CreateServiceTeamPanel.tsx";
import { ServiceTeamOverview } from "@/features/serviceTeams/components/overview/ServiceTeamOverview.tsx";

export const ServiceTeamsPage = () => {
  const [isCreating, setIsCreating] = useState(false);
  return (
    <div>
      <ServiceTeamHeader onIsCreating={() => setIsCreating(true)} />

      <ServiceTeamOverview onCreateTeam={() => setIsCreating(true)} />

      <Sheet
        open={isCreating}
        onOpenChange={(open, eventDetails) => {
          if (eventDetails.reason === "outside-press") {
            return;
          }

          setIsCreating(open);
        }}
      >
        <CreateServiceTeamPanel onClosePanel={() => setIsCreating(false)} />
      </Sheet>
    </div>
  );
};
