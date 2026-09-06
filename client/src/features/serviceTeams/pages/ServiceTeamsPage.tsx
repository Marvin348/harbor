import { useState } from "react";
import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Sheet } from "@/components/ui/sheet.tsx";
import { ServiceTeamHeader } from "@/features/serviceTeams/components/ServiceTeamHeader.tsx";
import { ServiceTeamList } from "@/features/serviceTeams/components/ServiceTeamList.tsx";
import { CreateServiceTeamPanel } from "@/features/serviceTeams/components/CreateServiceTeamPanel.tsx";
import { useGetServiceTeams } from "@/features/serviceTeams/hooks/useGetServiceTeams.ts";
import { ServiceTeamsEmptyState } from "@/features/serviceTeams/components/ServiceTeamsEmptyState.tsx";

export const ServiceTeamsPage = () => {
  const [isCreating, setIsCreating] = useState(false);

  const { serviceTeams = [], isLoading, isError } = useGetServiceTeams();

  return (
    <div>
      <ServiceTeamHeader />

      <Sheet
        open={isCreating}
        onOpenChange={(open, eventDetails) => {
          if (eventDetails.reason === "outside-press") {
            return;
          }

          setIsCreating(open);
        }}
      >
        <section className="rounded-md border border-border bg-background">
          <div className="flex flex-col gap-3 border-b border-border px-4 py-4 md:flex-row md:items-center md:justify-between">
            <div>
              <h2 className="text-base font-semibold">Service-Teams</h2>
              <p className="mt-1 text-sm text-muted-foreground">
                Bestehende Teams und ihre aktuellen Service-Zuordnungen.
              </p>
            </div>

            <Button onClick={() => setIsCreating(true)}>
              <Plus />
              Team erstellen
            </Button>
          </div>

          {isLoading ? (
            <div className="px-4 py-10 text-sm text-muted-foreground">
              Service-Teams werden geladen...
            </div>
          ) : isError ? (
            <div className="px-4 py-10 text-sm text-destructive">
              Service-Teams konnten nicht geladen werden.
            </div>
          ) : serviceTeams.length === 0 ? (
            <ServiceTeamsEmptyState onCreateTeam={() => setIsCreating(true)} />
          ) : (
            <div className="flex min-w-0 items-stretch">
              <ServiceTeamList serviceTeams={serviceTeams} />
            </div>
          )}
        </section>

        <CreateServiceTeamPanel onClosePanel={() => setIsCreating(false)} />
      </Sheet>
    </div>
  );
};
