import { Button } from "@/components/ui/button.tsx";
import { Link } from "@tanstack/react-router";
import { ArrowLeft, CircleAlert, RefreshCw } from "lucide-react";

type AgentTicketDetailsErrorStateProps = {
  onRetry: () => void;
};

export const AgentTicketDetailsErrorState = ({
  onRetry,
}: AgentTicketDetailsErrorStateProps) => {
  return (
    <div className="flex min-h-[calc(100vh-7.5rem)] w-full items-center justify-center py-16">
      <div className="flex max-w-lg flex-col items-center text-center">
        <div className="flex size-14 items-center justify-center rounded-md border border-destructive/30 bg-destructive/10">
          <CircleAlert className="size-6 text-destructive" />
        </div>

        <p className="mt-6 text-xs font-medium tracking-wide text-muted-foreground uppercase">
          Ticket
        </p>
        <h1 className="mt-2 text-xl font-semibold tracking-tight">
          Ticket nicht verfügbar
        </h1>
        <p className="mt-3 text-sm leading-6 text-muted-foreground">
          Das Ticket konnte nicht geöffnet werden. Es wurde möglicherweise
          entfernt, du hast keinen Zugriff darauf oder die Verbindung zum Server
          ist fehlgeschlagen.
        </p>

        <div className="mt-7 flex flex-col-reverse gap-2 sm:flex-row">
          <Button type="button" variant="outline" onClick={onRetry}>
            <RefreshCw />
            Erneut versuchen
          </Button>
          <Button render={<Link to="/tickets" search={{ page: 1 }} />}>
            <ArrowLeft />
            Zurück zu Tickets
          </Button>
        </div>
      </div>
    </div>
  );
};
