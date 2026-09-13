import { Plus, Ticket } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

type TicketOverviewEmptyStateProps = {
  onCreateTicket: () => void;
};

export const TicketOverviewEmptyState = ({
  onCreateTicket,
}: TicketOverviewEmptyStateProps) => {
  return (
    <div className="flex min-h-[400px] items-center justify-center px-4 py-12">
      <div className="flex max-w-md flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border border-border bg-muted/40">
          <Ticket className="size-5 text-muted-foreground" />
        </div>

        <h3 className="mt-5 text-base font-semibold">Noch keine Anfragen</h3>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">
          Erstelle deine erste Anfrage, damit das passende Service-Team sie
          bearbeiten kann.
        </p>

        <Button className="mt-5" onClick={onCreateTicket}>
          <Plus />
          Neue Anfrage
        </Button>
      </div>
    </div>
  );
};
