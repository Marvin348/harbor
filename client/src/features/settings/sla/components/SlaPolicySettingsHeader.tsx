import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

type SlaPolicySettingsHeaderProps = {
  onCreatePolicy: () => void;
};

export const SlaPolicySettingsHeader = ({
  onCreatePolicy,
}: SlaPolicySettingsHeaderProps) => {
  return (
    <header className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
      <div className="max-w-2xl">
        <h2 className="text-lg font-semibold">SLA-Richtlinien</h2>
        <p className="mt-1 text-sm leading-6 text-muted-foreground">
          Lege fest, wie schnell Tickets je Priorität beantwortet und gelöst
          werden müssen.
        </p>
      </div>

      <Button onClick={onCreatePolicy} className="shrink-0">
        <Plus />
        Richtlinie erstellen
      </Button>
    </header>
  );
};
