import { Clock3, Plus } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

type SlaPolicyListEmptyStateProps = {
  onCreatePolicy: () => void;
};

export const SlaPolicyListEmptyState = ({
  onCreatePolicy,
}: SlaPolicyListEmptyStateProps) => {
  return (
    <div className="flex min-h-72 items-center justify-center px-4 py-10">
      <div className="flex max-w-md flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border bg-muted/40">
          <Clock3 className="size-5 text-muted-foreground" />
        </div>
        <h3 className="mt-5 text-base font-semibold">
          Noch keine SLA-Richtlinien
        </h3>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">
          Erstelle die erste Richtlinie, um Reaktions- und Lösungszeiten für
          eine Ticket-Priorität festzulegen.
        </p>
        <Button className="mt-5" onClick={onCreatePolicy}>
          <Plus />
          Erste Richtlinie erstellen
        </Button>
      </div>
    </div>
  );
};
