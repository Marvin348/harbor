import { Plus, UsersRound } from "lucide-react";

import { Button } from "@/components/ui/button.tsx";

type ServiceTeamsEmptyStateProps = {
  onCreateTeam: () => void;
};

export const ServiceTeamsEmptyState = ({
  onCreateTeam,
}: ServiceTeamsEmptyStateProps) => {
  return (
    <div className="flex min-h-[360px] items-center justify-center px-4 py-12">
      <div className="flex max-w-md flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border border-border bg-muted/40">
          <UsersRound className="size-5 text-muted-foreground" />
        </div>

        <h3 className="mt-5 text-base font-semibold">
          Noch keine Service-Teams
        </h3>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">
          Erstelle dein erstes Service-Team, damit interne Anfragen später klar
          einem verantwortlichen Team zugeordnet werden können.
        </p>

        <Button className="mt-5" onClick={onCreateTeam}>
          <Plus />
          Erstes Team erstellen
        </Button>
      </div>
    </div>
  );
};
