import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

type ServiceTeamHeaderProps = {
  onIsCreating: () => void;
};

export const ServiceTeamHeader = ({ onIsCreating }: ServiceTeamHeaderProps) => {
  return (
    <section className="mb-6 flex flex-col gap-4 lg-6 md:flex-row lg:items-top lg:justify-between">
      <div className="max-w-3xl">
        <h1 className="text-2xl font-semibold tracking-normal">
          Service-Teams
        </h1>
        <p className="mt-2 max-w-2xl text-sm leading-6 text-muted-foreground">
          Organisiere die Teams, die interne Services bearbeiten. Jedes Team
          kann später Mitglieder, Services und eigene Zuständigkeiten erhalten.
        </p>
      </div>

      <Button onClick={onIsCreating}>
        <Plus />
        Team erstellen
      </Button>
    </section>
  );
};
